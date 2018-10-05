/*******************************************************************************
 * Copyright (c) 2014, 2016 fortiss GmbH, 2018 TU Wien/ACIN
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alois Zoitl, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *   
 *   Peter Gsellmann
 *     - incorporation of simple fb
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.ecc.commands;

import org.eclipse.fordiac.ide.model.libraryElement.Algorithm;
import org.eclipse.fordiac.ide.model.libraryElement.BaseFBType;
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType;
import org.eclipse.fordiac.ide.model.libraryElement.ECAction;
import org.eclipse.fordiac.ide.model.libraryElement.ECState;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.fordiac.ide.model.libraryElement.OtherAlgorithm;
import org.eclipse.fordiac.ide.model.libraryElement.STAlgorithm;
import org.eclipse.fordiac.ide.model.libraryElement.TextAlgorithm;
import org.eclipse.gef.commands.Command;

public class ChangeAlgorithmTypeCommand extends Command {

	protected final BaseFBType fbType;
	protected Algorithm oldAlgorithm;
	protected Algorithm newAlgorithm;
	private String algorithmType;

	public ChangeAlgorithmTypeCommand(BaseFBType fbType, Algorithm oldAlgorithm, String algorithmType) {
		this.fbType = fbType;
		this.oldAlgorithm = oldAlgorithm;
		this.algorithmType = algorithmType;
	}

	@Override
	public boolean canExecute() {
		if (algorithmType.equalsIgnoreCase("ST")) { //$NON-NLS-1$
			if (oldAlgorithm instanceof STAlgorithm) {
				return false;
			} else if (!(oldAlgorithm instanceof TextAlgorithm || oldAlgorithm instanceof OtherAlgorithm)) {
				return false;
			}
		} else if ((!(oldAlgorithm instanceof STAlgorithm))
				&& (oldAlgorithm instanceof TextAlgorithm || oldAlgorithm instanceof OtherAlgorithm)) {
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		// FIXME this only works if there are no more other algorithms
		// supported!!!

		if (algorithmType.equalsIgnoreCase("ST")) { //$NON-NLS-1$
			newAlgorithm = createSTAlgorithm();
		} else {
			newAlgorithm = createOtherAlgorithm();
		}

		redo();
	}

	private Algorithm createSTAlgorithm() {
		STAlgorithm algorithm = LibraryElementFactory.eINSTANCE.createSTAlgorithm();
		algorithm.setText(((TextAlgorithm) oldAlgorithm).getText());
		algorithm.setName(oldAlgorithm.getName());
		algorithm.setComment(oldAlgorithm.getComment());
		return algorithm;
	}

	private Algorithm createOtherAlgorithm() {
		OtherAlgorithm algorithm = LibraryElementFactory.eINSTANCE.createOtherAlgorithm();
		algorithm.setText(((TextAlgorithm) oldAlgorithm).getText());
		algorithm.setName(oldAlgorithm.getName());
		algorithm.setComment(oldAlgorithm.getComment());
		algorithm.setLanguage("AnyText"); //$NON-NLS-1$
		return algorithm;
	}

	@Override
	public void undo() {
		((BasicFBType) fbType).getAlgorithm().add(((BasicFBType) fbType).getAlgorithm().indexOf(newAlgorithm),
				oldAlgorithm);
		updateECActions(false);
		((BasicFBType) fbType).getAlgorithm().remove(newAlgorithm);
	}

	@Override
	public void redo() {
		((BasicFBType) fbType).getAlgorithm().add(((BasicFBType) fbType).getAlgorithm().indexOf(oldAlgorithm),
				newAlgorithm);
		updateECActions(true);
		((BasicFBType) fbType).getAlgorithm().remove(oldAlgorithm);
	}

	private void updateECActions(boolean redo) {
		for (ECState state : ((BasicFBType) fbType).getECC().getECState()) {
			for (ECAction action : state.getECAction()) {
				if (true == redo) {
					if (action.getAlgorithm() == oldAlgorithm) {
						action.setAlgorithm(newAlgorithm);
					}
				} else {
					if (action.getAlgorithm() == newAlgorithm) {
						action.setAlgorithm(oldAlgorithm);
					}
				}
			}
		}
	}

	public Algorithm getNewAlgorithm() {
		return newAlgorithm;
	}

}
