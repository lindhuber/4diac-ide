/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Ernst Blecha
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.commands.testinfra;

import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.model.Palette.FBTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.PaletteFactory;
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.fordiac.ide.model.typelibrary.DataTypeLibrary;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibrary;
import org.eclipse.gef.commands.Command;

public abstract class FBNetworkTestBase extends CommandTestBase<FBNetworkTestBase.State> {

	private static final DataTypeLibrary dataTypeLib = new DataTypeLibrary();

	// create a state description that fits our purpose
	public static class State implements CommandTestBase.StateBase {
		private final FBNetwork net;
		private final FBTypePaletteEntry functionblock;

		private Command cmd;

		private static final String FUNCTIONBLOCK_NAME = "functionblock"; //$NON-NLS-1$

		private FBTypePaletteEntry createFB() {
			final FBTypePaletteEntry pe = PaletteFactory.eINSTANCE.createFBTypePaletteEntry();
			final TypeLibrary typelib = TypeLibrary.getTypeLibrary(null);
			final BasicFBType functionBlock = LibraryElementFactory.eINSTANCE.createBasicFBType();

			functionBlock.setInterfaceList(LibraryElementFactory.eINSTANCE.createInterfaceList());
			functionBlock.setName(FUNCTIONBLOCK_NAME);

			functionBlock.setECC(LibraryElementFactory.eINSTANCE.createECC());

			pe.setLabel(FUNCTIONBLOCK_NAME);
			pe.setPalette(typelib.getBlockTypeLib());
			pe.setType(functionBlock);

			return pe;

		}

		public FBNetwork getFbNetwork() {
			return net;
		}

		public FBTypePaletteEntry getFunctionblock() {
			return functionblock;
		}

		@Override
		public Command getCommand() {
			return cmd;
		}

		@Override
		public void setCommand(Command cmd) {
			this.cmd = cmd;
		}

		public State() {
			net = LibraryElementFactory.eINSTANCE.createFBNetwork();
			functionblock = createFB();
		}

		private State(State s) {
			net = EcoreUtil.copy(s.net);
			functionblock = EcoreUtil.copy(s.functionblock);
		}

		@Override
		public Object getClone() {
			return new State(this);
		}
	}

	protected static State undoCommand(Object stateObj) {
		final State state = (State) stateObj;
		assumeTrue(state.getCommand().canUndo());
		state.getCommand().undo();
		return (state);
	}

	protected static State redoCommand(Object stateObj) {
		final State state = (State) stateObj;
		assumeTrue(state.getCommand().canRedo());
		state.getCommand().redo();
		return (state);
	}

	protected static Collection<Object[]> describeCommand(String description, StateInitializer<?> initializer,
			StateVerifier<?> initialVerifier, List<Object> commands) {
		return describeCommand(description, initializer, initialVerifier, commands, FBNetworkTestBase::undoCommand,
				FBNetworkTestBase::redoCommand);
	}

	protected static void verifyDefaultInitialValues(State state, State oldState, TestFunction t) {
		t.test(!state.getFbNetwork().isCFBTypeNetwork());
		t.test(!state.getFbNetwork().isResourceNetwork());
		t.test(!state.getFbNetwork().isSubApplicationNetwork());
		t.test(state.getFbNetwork().getNetworkElements().isEmpty());
	}

	protected static List<Object[]> createCommands(List<Object> executionDescriptions) {
		final List<Object[]> commands = new ArrayList<>();

		commands.addAll(describeCommand("Start from default values", //
				State::new, //
				(State state, State oldState, TestFunction t) -> verifyDefaultInitialValues(state, oldState, t), //
				executionDescriptions //
		));

		return commands;
	}

	public static DataTypeLibrary getDatatypelib() {
		return dataTypeLib;
	}

}
