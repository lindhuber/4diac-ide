/*******************************************************************************
 * Copyright (c) 2023 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Michael Oberlehner - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.typemanagement.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.typelibrary.DataTypeEntry;
import org.eclipse.fordiac.ide.model.typelibrary.TypeEntry;
import org.eclipse.fordiac.ide.typemanagement.util.FBUpdater;
import org.eclipse.fordiac.ide.ui.FordiacLogHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Display;

public class InterfaceDataTypeChange extends Change {

	private final FBType fbType;
	private final TypeEntry typeEntry;
	private final String oldName;
	private final List<String> pinNames;

	public InterfaceDataTypeChange(final FBType fbType, final TypeEntry oldTypeEntry, final String oldName) {
		this.fbType = fbType;
		this.typeEntry = oldTypeEntry;
		this.oldName = oldName;
		this.pinNames = new ArrayList<>();
	}

	@Override
	public String getName() {
		return "Update InterfacePins: - " + fbType.getName() + "." //$NON-NLS-1$ //$NON-NLS-2$
				+ fbType.getTypeEntry().getFile().getFileExtension() + " - " //$NON-NLS-1$
				+ fbType.getTypeEntry().getFile().getProject().getName() + ": " + pinNames.toString(); //$NON-NLS-1$
	}

	@Override
	public void initializeValidationData(final IProgressMonitor pm) {

	}

	private Command getUpdatePinInTypeDelcarationCommand() {
		return FBUpdater.createUpdatePinInTypeDeclarationCommand(fbType, (DataTypeEntry) typeEntry, oldName);
	}

	@Override
	public RefactoringStatus isValid(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change perform(final IProgressMonitor pm) throws CoreException {
		// we have to execute in UI thread, otherwise SWT crashes if the editor of the
		// type is open
		final Command cmd = getUpdatePinInTypeDelcarationCommand();
		Display.getDefault().syncExec(() -> {
			cmd.execute();
			try {
				fbType.getTypeEntry().save(fbType, pm);
			} catch (final CoreException e) {
				FordiacLogHelper.logError(e.getMessage(), e);
			}
		});
		return null;
	}

	@Override
	public Object getModifiedElement() {
		return fbType;
	}

}
