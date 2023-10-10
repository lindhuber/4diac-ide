/*******************************************************************************
 * Copyright (c) 2017, 2023 fortiss GmbH
 *                          Johannes Kepler University Linz
 *                          Martin Erich Jobst
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Monika Wenger - initial API and implementation and/or initial documentation
 *   Alois Zoitl - extracted helper for ComboCellEditors that unfold on activation
 *               - cleaned command stack handling for property sections
 *   Melanie Winter - buttons are created with AddDeleteWidget
 *   Martin Erich Jobst - convert to new attribute model and nat table
 *******************************************************************************/
package org.eclipse.fordiac.ide.gef.properties;

import org.eclipse.fordiac.ide.gef.filters.AttributeFilter;
import org.eclipse.fordiac.ide.gef.nat.AttributeColumnAccessor;
import org.eclipse.fordiac.ide.gef.nat.AttributeConfigLabelAccumulator;
import org.eclipse.fordiac.ide.gef.nat.AttributeEditableRule;
import org.eclipse.fordiac.ide.gef.nat.AttributeTableColumn;
import org.eclipse.fordiac.ide.gef.nat.InitialValueEditorConfiguration;
import org.eclipse.fordiac.ide.model.commands.change.ChangeAttributeOrderCommand;
import org.eclipse.fordiac.ide.model.commands.change.IndexUpDown;
import org.eclipse.fordiac.ide.model.commands.create.CreateAttributeCommand;
import org.eclipse.fordiac.ide.model.commands.delete.DeleteAttributeCommand;
import org.eclipse.fordiac.ide.model.libraryElement.Attribute;
import org.eclipse.fordiac.ide.model.libraryElement.ConfigurableObject;
import org.eclipse.fordiac.ide.model.ui.nat.DataTypeSelectionTreeContentProvider;
import org.eclipse.fordiac.ide.model.ui.widgets.DataTypeSelectionContentProvider;
import org.eclipse.fordiac.ide.model.ui.widgets.TypeSelectionButton;
import org.eclipse.fordiac.ide.ui.widget.AddDeleteReorderListWidget;
import org.eclipse.fordiac.ide.ui.widget.ChangeableListDataProvider;
import org.eclipse.fordiac.ide.ui.widget.I4diacNatTableUtil;
import org.eclipse.fordiac.ide.ui.widget.IChangeableRowDataProvider;
import org.eclipse.fordiac.ide.ui.widget.NatTableColumnProvider;
import org.eclipse.fordiac.ide.ui.widget.NatTableWidgetFactory;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class AttributeSection extends AbstractSection implements I4diacNatTableUtil {
	protected IChangeableRowDataProvider<Attribute> provider;
	protected NatTable table;

	@Override
	public void createControls(final Composite parent, final TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		createAttributesControls(parent);
	}

	public void createAttributesControls(final Composite parent) {
		final Composite composite = getWidgetFactory().createComposite(parent);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final AddDeleteReorderListWidget buttons = new AddDeleteReorderListWidget();
		buttons.createControls(composite, getWidgetFactory());

		provider = new ChangeableListDataProvider<>(new AttributeColumnAccessor(this));
		final DataLayer dataLayer = new DataLayer(provider);
		dataLayer.setConfigLabelAccumulator(new AttributeConfigLabelAccumulator(provider));
		table = NatTableWidgetFactory.createRowNatTable(composite, dataLayer,
				new NatTableColumnProvider<>(AttributeTableColumn.DEFAULT_COLUMNS),
				new AttributeEditableRule(IEditableRule.ALWAYS_EDITABLE, AttributeTableColumn.DEFAULT_COLUMNS,
						provider),
				new TypeSelectionButton(this::getTypeLibrary, DataTypeSelectionContentProvider.INSTANCE,
						DataTypeSelectionTreeContentProvider.INSTANCE),
				this, false);
		table.addConfiguration(new InitialValueEditorConfiguration(provider));
		table.configure();

		buttons.bindToTableViewer(table, this,
				ref -> CreateAttributeCommand.forTemplate(getType(), getLastSelectedAttribute(), getInsertionIndex()),
				ref -> new DeleteAttributeCommand(getType(), getLastSelectedAttribute()),
				ref -> new ChangeAttributeOrderCommand(getType(), (Attribute) ref, IndexUpDown.UP),
				ref -> new ChangeAttributeOrderCommand(getType(), (Attribute) ref, IndexUpDown.DOWN));
	}

	private int getInsertionIndex() {
		final Attribute attribute = getLastSelectedAttribute();
		if (null == attribute) {
			return getType().getAttributes().size();
		}
		return getType().getAttributes().indexOf(attribute) + 1;
	}

	private Attribute getLastSelectedAttribute() {
		return (Attribute) NatTableWidgetFactory.getLastSelectedVariable(table);
	}

	@Override
	public void addEntry(final Object entry, final boolean isInput, final int index, final CompoundCommand cmd) {
		if (entry instanceof final Attribute attribute) {
			cmd.add(CreateAttributeCommand.forTemplate(getType(), attribute, index));
		}
	}

	@Override
	public void refresh() {
		final CommandStack commandStackBuffer = commandStack;
		commandStack = null;
		if (null != getType()) {
			provider.setInput(getType().getAttributes());
		}
		commandStack = commandStackBuffer;
		table.refresh();
	}

	@Override
	public void executeCompoundCommand(final CompoundCommand cmd) {
		executeCommand(cmd);
		table.refresh();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	protected ConfigurableObject getInputType(final Object input) {
		return AttributeFilter.parseObject(input) instanceof final ConfigurableObject configurableObject
				? configurableObject
				: null;
	}

	@Override
	protected void setInputCode() {
		// nothing to do here
	}

	@Override
	protected void setInputInit() {
		final ConfigurableObject currentType = getType();
		if (currentType != null) {
			provider.setInput(currentType.getAttributes());
		}
	}

	@Override
	public void removeEntry(final Object entry, final CompoundCommand cmd) {
		if (entry instanceof final Attribute attribute) {
			cmd.add(new DeleteAttributeCommand(getType(), attribute));
		}
	}

	@Override
	protected ConfigurableObject getType() {
		return type instanceof final ConfigurableObject configurableObject ? configurableObject : null;
	}
}
