/*******************************************************************************
 * Copyright (c) 2008, 2009, 2014, 2017 Profactor GbmH, fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Jose Cabral
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.gef;

import org.eclipse.fordiac.ide.gef.preferences.DiagramPreferences;
import org.eclipse.fordiac.ide.ui.Abstract4DIACUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends Abstract4DIACUIPlugin {

	private static final String RULER_UNITS = "RulerUnits"; //$NON-NLS-1$

	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.eclipse.fordiac.ide.gef"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor.
	 */
	public Activator() {
		// empty constructor
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		//transitional code for workspaces with ruler units. If inches or centimeters were used, the grid spacing is set to a default
		if (getPreferenceStore().contains(RULER_UNITS) && 
				org.eclipse.gef.rulers.RulerProvider.UNIT_PIXELS != getPreferenceStore().getInt(RULER_UNITS)){
			getPreferenceStore().setValue(DiagramPreferences.GRID_SPACING, 20);
			getPreferenceStore().setValue(RULER_UNITS, org.eclipse.gef.rulers.RulerProvider.UNIT_PIXELS);
			//thre's no way to delete a key. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=279774
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
}
