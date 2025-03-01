/*******************************************************************************
 * Copyright (c) 2017 fortiss GmbH
 * 				 2019, 2020 Johannes Kepler University Linz
 * 				 2020 Primetals Technologies Germany GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *   Bianca Wiesmayr - create command now has enhanced guess
 *   Daniel Lindhuber - added insert command method & cell editor classes
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.properties;

import org.eclipse.fordiac.ide.application.commands.ChangeSubAppIETypeCommand;
import org.eclipse.fordiac.ide.application.commands.ChangeSubAppInterfaceOrderCommand;
import org.eclipse.fordiac.ide.application.commands.CreateSubAppInterfaceElementCommand;
import org.eclipse.fordiac.ide.application.commands.DeleteSubAppInterfaceElementCommand;
import org.eclipse.fordiac.ide.application.editparts.SubAppForFBNetworkEditPart;
import org.eclipse.fordiac.ide.application.editparts.UISubAppNetworkEditPart;
import org.eclipse.fordiac.ide.gef.properties.AbstractEditInterfaceDataSection;
import org.eclipse.fordiac.ide.model.commands.change.ChangeDataTypeCommand;
import org.eclipse.fordiac.ide.model.commands.change.ChangeInterfaceOrderCommand;
import org.eclipse.fordiac.ide.model.commands.create.CreateInterfaceElementCommand;
import org.eclipse.fordiac.ide.model.commands.delete.DeleteInterfaceCommand;
import org.eclipse.fordiac.ide.model.data.DataType;
import org.eclipse.fordiac.ide.model.edit.providers.DataLabelProvider;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class EditInterfaceDataSection extends AbstractEditInterfaceDataSection {
	@Override
	protected CreateInterfaceElementCommand newCreateCommand(final IInterfaceElement interfaceElement, final boolean isInput) {
		final DataType last = getLastUsedDataType(getType().getInterface(), isInput, interfaceElement);
		final int pos = getInsertingIndex(interfaceElement, isInput);
		return new CreateSubAppInterfaceElementCommand(last, getCreationName(interfaceElement),
				getType().getInterface(), isInput, pos);
	}

	@Override
	protected CreateInterfaceElementCommand newInsertCommand(final IInterfaceElement interfaceElement,
			final boolean isInput,
			final int index) {
		return new CreateSubAppInterfaceElementCommand(interfaceElement, isInput, getType().getInterface(), index);
	}

	@Override
	protected LabelProvider getLabelProvider() {
		return new DataLabelProvider() {

			@Override
			public Color getBackground(final Object element, final int columnIndex) {
				if ((columnIndex == INITIALVALUE_COL_INDEX) && (!((VarDeclaration) element).isIsInput())) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
				}
				return null;
			}

			@Override
			public String getColumnText(final Object element, final int columnIndex) {
				if ((columnIndex == INITIALVALUE_COL_INDEX) && !((VarDeclaration) element).isIsInput()) {
					return "-"; //$NON-NLS-1$
				}
				return super.getColumnText(element, columnIndex);
			}

		};
	}

	@Override
	protected InterfaceCellModifier getCellModifier(final TableViewer viewer) {
		return new DataInterfaceCellModifier(viewer) {
			@Override
			public boolean canModify(final Object element, final String property) {
				if (INITIAL_VALUE.equals(property)) {
					return ((VarDeclaration) element).isIsInput();
				}
				return super.canModify(element, property);
			}

			@Override
			public Object getValue(final Object element, final String property) {
				if (property.equals(INITIAL_VALUE) && !((VarDeclaration) element).isIsInput()) {
					return "-"; //$NON-NLS-1$
				}
				return super.getValue(element, property);
			}
		};
	}

	@Override
	protected SubApp getInputType(final Object input) {
		if (input instanceof SubAppForFBNetworkEditPart) {
			return ((SubAppForFBNetworkEditPart) input).getModel();
		}
		if (input instanceof UISubAppNetworkEditPart) {
			return ((UISubAppNetworkEditPart) input).getSubApp();
		}
		if (input instanceof SubApp) {
			return (SubApp) input;
		}
		return null;
	}

	@Override
	protected DeleteInterfaceCommand newDeleteCommand(final IInterfaceElement selection) {
		return new DeleteSubAppInterfaceElementCommand(selection);
	}

	@Override
	protected ChangeInterfaceOrderCommand newOrderCommand(final IInterfaceElement selection, final boolean moveUp) {
		return new ChangeSubAppInterfaceOrderCommand(selection, moveUp);
	}

	@Override
	protected ChangeDataTypeCommand newChangeTypeCommand(final VarDeclaration data, final DataType newType) {
		return new ChangeSubAppIETypeCommand(data, newType);
	}

	@Override
	protected SubApp getType() {
		return (SubApp) type;
	}

}
