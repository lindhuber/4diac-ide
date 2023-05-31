/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University, Linz
 *               2021 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Daniel Lindhuber, Bianca Wiesmayr
 *     - initial API and implementation and/or initial documentation
 *   Muttenthaler Benjamin
 *     - fixed reload of view if file on file system did change
 *     - use new saveType method of AbstractTypeExporter
 *     - replaced DataTypeListener by AdapterImpl
 *     - keep a copy of the datatype object in the view, otherwise the content of the file is changed even the save button was not pressed
 *   Lukas Wais
 *     - enabled Save As
 *******************************************************************************/

package org.eclipse.fordiac.ide.datatypeeditor.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.datatypeedito.wizards.SaveAsStructTypeWizard;
import org.eclipse.fordiac.ide.datatypeeditor.Messages;
import org.eclipse.fordiac.ide.datatypeeditor.widgets.StructViewingComposite;
import org.eclipse.fordiac.ide.model.commands.change.ChangeStructCommand;
import org.eclipse.fordiac.ide.model.commands.change.ChangeSubAppPinCommand;
import org.eclipse.fordiac.ide.model.data.StructuredType;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.model.libraryElement.INamedElement;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage;
import org.eclipse.fordiac.ide.model.libraryElement.StructManipulator;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.model.libraryElement.SubAppType;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;
import org.eclipse.fordiac.ide.model.search.dialog.FBUpdateDialog;
import org.eclipse.fordiac.ide.model.search.types.InstanceSearch;
import org.eclipse.fordiac.ide.model.search.types.StructDataTypeSearch;
import org.eclipse.fordiac.ide.model.typelibrary.DataTypeEntry;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibraryManager;
import org.eclipse.fordiac.ide.model.ui.editors.HandlerHelper;
import org.eclipse.fordiac.ide.systemmanagement.changelistener.IEditorFileChangeListener;
import org.eclipse.fordiac.ide.ui.FordiacLogHelper;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class DataTypeEditor extends EditorPart implements CommandStackEventListener,
ITabbedPropertySheetPageContributor, ISelectionListener, IEditorFileChangeListener {

	private final CommandStack commandStack = new CommandStack();
	private StructViewingComposite structComposite;
	private Composite errorComposite;
	private boolean importFailed;
	private boolean outsideWorkspace;
	private static final int DEFAULT_BUTTON_INDEX = 0; // This would be the "Save" button
	private static final int SAVE_AS_BUTTON_INDEX = 1;
	private static final int CANCEL_BUTTON_INDEX = 2;

	private ActionRegistry actionRegistry;
	private final List<String> selectionActions = new ArrayList<>();
	private final List<String> stackActions = new ArrayList<>();
	private final List<String> propertyActions = new ArrayList<>();

	private DataTypeEntry dataTypeEntry;
	private FBUpdateDialog structSaveDialog;

	private final Adapter adapter = new AdapterImpl() {

		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			final Object feature = notification.getFeature();
			if ((null != feature)
					&& (LibraryElementPackage.LIBRARY_ELEMENT__NAME == notification.getFeatureID(feature.getClass()))) {
				Display.getDefault().asyncExec(() -> {
					if (null != dataTypeEntry) {
						setPartName(dataTypeEntry.getFile().getName());
						setInput(new FileEditorInput(dataTypeEntry.getFile()));
					}
				});
			}
		}
	};

	@Override
	public void stackChanged(final CommandStackEvent event) {
		updateActions(stackActions);
		firePropertyChange(IEditorPart.PROP_DIRTY);
		structComposite.refresh();
	}

	@Override
	public String getContributorId() {
		return "org.eclipse.fordiac.ide.datatypeeditor.editors.DataTypeEditor"; //$NON-NLS-1$
	}

	@Override
	public void dispose() {
		// get these values here before calling super dispose
		final boolean dirty = isDirty();

		getCommandStack().removeCommandStackEventListener(this);
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		getActionRegistry().dispose();
		removeListenerFromDataTypeObj();
		super.dispose();
		if (dirty && dataTypeEntry != null) {
			// purge editable type from type entry after super.dispose() so that no notifiers will be called
			dataTypeEntry.setTypeEditable(null);
		}
	}

	@Override
	protected void firePropertyChange(final int property) {
		super.firePropertyChange(property);
		updateActions(propertyActions);
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		removeListenerFromDataTypeObj();
		loadAllOpenEditors();
		createSaveDialog();
	}

	private void createSaveDialog() {
		final String[] labels = { Messages.StructAlteringButton_SaveAndUpdate, Messages.StructAlteringButton_SaveAs,
				SWT.getMessage("SWT_Cancel") }; //$NON-NLS-1$

		structSaveDialog = new FBUpdateDialog(null, Messages.StructViewingComposite_Headline, null, "",
				MessageDialog.NONE, labels, DEFAULT_BUTTON_INDEX, dataTypeEntry);

		// Depending on the button clicked:
		switch (structSaveDialog.open()) {
		case DEFAULT_BUTTON_INDEX:
			dataTypeEntry.save();
			addListenerToDataTypeObj();
			commandStack.markSaveLocation();
			updateFB();
			firePropertyChange(IEditorPart.PROP_DIRTY);
			break;
		case SAVE_AS_BUTTON_INDEX:
			doSaveAs();
			break;
		case CANCEL_BUTTON_INDEX:
			MessageDialog.openInformation(null, Messages.StructViewingComposite_Headline,
					Messages.WarningDialog_StructNotSaved);
			break;
		default:
			break;
		}
	}

	@Override
	public void doSaveAs() {
		if (dataTypeEntry.getTypeEditable() instanceof final StructuredType structuredType) {
			new WizardDialog(null, new SaveAsStructTypeWizard(structuredType, this)).open();
		}
	}

	private void updateFB() {
		updateStructManipulators();
		updateTypes();
		updateInstances();
	}

	private void updateTypes() {
		final InstanceSearch search = StructDataTypeSearch
				.createStructInterfaceSearch((StructuredType) dataTypeEntry.getTypeEditable());
		final Set<INamedElement> fbTypes = search.performTypeLibBlockSearch(dataTypeEntry.getTypeLibrary());
		Display.getDefault().asyncExec(() -> {
			fbTypes.stream().filter(SubAppType.class::isInstance).map(SubAppType.class::cast).forEach(sApp -> {
				sApp.getInterfaceList().getAllInterfaceElements().stream()
				.filter(i -> i.getTypeName().equals(dataTypeEntry.getTypeName()))
				.filter(VarDeclaration.class::isInstance).map(VarDeclaration.class::cast).forEach(el -> {
					el.setType(dataTypeEntry.getType());
					if (el.getValue() != null && !el.getValue().getValue().equals("")) {
						el.getValue().setValue("");
					}
				});
				sApp.getTypeEntry().save();
			});
		});
	}

	private void updateInstances() {
		structSaveDialog.getCollectedFBs().stream().filter(SubApp.class::isInstance).map(SubApp.class::cast)
		.forEach(subApp -> {
			if (subApp.isTyped()) {
				final StructuredType structuredType = (StructuredType) dataTypeEntry.getTypeEditable();
				(new ChangeSubAppPinCommand(subApp, structuredType)).execute();
			} else {
				subApp.getInterface().getAllInterfaceElements().stream()
				.filter(i -> i.getTypeName().equals(dataTypeEntry.getTypeName()))
				.filter(VarDeclaration.class::isInstance).map(VarDeclaration.class::cast)
				.forEach(el -> {
					el.setType(dataTypeEntry.getType());
					if (!el.getValue().getValue().equals("")) {
						el.getValue().setValue("");
					}
				});
			}
		});

	}

	private void updateStructManipulators() {
		structSaveDialog.getCollectedFBs().stream().filter(StructManipulator.class::isInstance)
		.map(StructManipulator.class::cast).forEach(mux -> {
			final StructuredType structuredType = (StructuredType) dataTypeEntry.getTypeEditable();
			final EObject rootContainer = EcoreUtil.getRootContainer(EcoreUtil.getRootContainer(mux));

			if (rootContainer instanceof final AutomationSystem autoSys) {
				autoSys.getCommandStack().execute(new ChangeStructCommand(mux, structuredType));
			} else if (rootContainer instanceof final SubAppType subApp) {
				final IFile file = subApp.getTypeEntry().getFile();
				final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.findEditor(new FileEditorInput(file));
				if (editor != null) {
					HandlerHelper.getCommandStack(editor).execute(new ChangeStructCommand(mux, structuredType));
				} else {
					(new ChangeStructCommand(mux, structuredType)).execute();
				}
			}
		});
	}

	private static void loadAllOpenEditors() {
		final IEditorReference[] openEditors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (final IEditorReference iEditorReference : openEditors) {
			iEditorReference.getEditor(true);
		}
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		importType(input);
		setInput(input);
		setSite(site);
		addListenerToDataTypeObj();
		site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		getCommandStack().addCommandStackEventListener(this);
		initializeActionRegistry();
		setActionHandlers(site);
	}

	private void addListenerToDataTypeObj() {
		if (dataTypeEntry != null && dataTypeEntry.getType() != null) {
			dataTypeEntry.getType().eAdapters().add(adapter);
		}
	}

	private void removeListenerFromDataTypeObj() {
		if (dataTypeEntry != null && dataTypeEntry.getType() != null
				&& dataTypeEntry.getType().eAdapters().contains(adapter)) {
			dataTypeEntry.getType().eAdapters().remove(adapter);
		}
	}

	private void importType(final IEditorInput input) throws PartInitException {
		if (input instanceof final FileEditorInput fileEditorInput) {
			final IFile file = fileEditorInput.getFile();
			try {
				importFailed = importTypeBasedOnFile(file);
			} catch (final Exception e) {
				throw new PartInitException(e.getMessage(), e);
			}
		} else if (input instanceof FileStoreEditorInput) {
			// is called when files are opened via File -> Open File
			importFailed = true;
			outsideWorkspace = true;
		}
	}

	private boolean importTypeBasedOnFile(final IFile file) throws CoreException {
		file.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
		// exist anymore!
		if (file.exists()) {
			dataTypeEntry = (DataTypeEntry) TypeLibraryManager.INSTANCE.getTypeEntryForFile(file);
			setPartName(dataTypeEntry.getFile().getName());
			return !(dataTypeEntry.getTypeEditable() instanceof StructuredType);
		}
		return true; // import failed
	}

	private void setActionHandlers(final IEditorSite site) {
		final ActionRegistry registry = getActionRegistry();
		final IActionBars bars = site.getActionBars();
		String id = ActionFactory.UNDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.REDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.DELETE.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		bars.updateActionBars();
	}

	@Override
	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(final Composite parent) {
		if (dataTypeEntry.getTypeEditable() != null && (!importFailed)) {
			structComposite = new StructViewingComposite(parent, 1, commandStack, dataTypeEntry, this);
			structComposite.createPartControl(parent);
		} else if (importFailed) {
			createErrorComposite(parent, Messages.ErrorCompositeMessage);
			if (outsideWorkspace) {
				MessageDialog.openError(getSite().getShell().getShell(),
						Messages.MessageDialogTitle_OutsideWorkspaceError,
						Messages.MessageDialogContent_OutsideWorkspaceError);
			}
		}
	}

	private void createErrorComposite(final Composite parent, final String errorText) {
		errorComposite = new Composite(parent, SWT.NONE);
		errorComposite.setLayout(new GridLayout(1, false));
		final Label label = new Label(errorComposite, SWT.CENTER);
		label.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.HEADER_FONT));
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		label.setText(errorText);
	}

	@Override
	public void setFocus() {
		if (null == structComposite) {
			errorComposite.setFocus();
		} else {
			structComposite.setFocus();
		}
	}

	public CommandStack getCommandStack() {
		return commandStack;
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (this.equals(getSite().getPage().getActiveEditor())) {
			updateActions(selectionActions);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	private void createActions() {
		final ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new UndoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());

		action = new RedoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key == org.eclipse.ui.views.properties.IPropertySheetPage.class) {
			return key.cast(new TabbedPropertySheetPage(this));
		}
		if (key == CommandStack.class) {
			return key.cast(getCommandStack());
		}
		if (key == ActionRegistry.class) {
			return key.cast(getActionRegistry());
		}

		return super.getAdapter(key);
	}

	private List<String> getStackActions() {
		return stackActions;
	}

	private void initializeActionRegistry() {
		createActions();
		updateActions(propertyActions);
		updateActions(stackActions);
	}

	private void updateActions(final List<String> actionIds) {
		final ActionRegistry registry = getActionRegistry();
		actionIds.forEach(id -> {
			final IAction action = registry.getAction(id);
			if (action instanceof final UpdateAction updateAction) {
				updateAction.update();
			}
		});
	}

	private ActionRegistry getActionRegistry() {
		if (null == actionRegistry) {
			actionRegistry = new ActionRegistry();
		}
		return actionRegistry;
	}

	@Override
	public void reloadFile() {
		try {
			removeListenerFromDataTypeObj();
			dataTypeEntry.setTypeEditable(null);
			importType(getEditorInput());
			structComposite.reload();
			addListenerToDataTypeObj();
		} catch (final PartInitException e) {
			FordiacLogHelper
			.logError("Error during refreshing struct table after file change detection: " + e.toString(), e); //$NON-NLS-1$
		}

	}

	@Override
	public IFile getFile() {
		Assert.isNotNull(((FileEditorInput) getEditorInput()).getFile());
		return ((FileEditorInput) getEditorInput()).getFile();
	}

	@Override
	public void updateEditorInput(final FileEditorInput newInput) {
		setInput(newInput);
		setTitleToolTip(newInput.getFile().getFullPath().toOSString());
	}
}
