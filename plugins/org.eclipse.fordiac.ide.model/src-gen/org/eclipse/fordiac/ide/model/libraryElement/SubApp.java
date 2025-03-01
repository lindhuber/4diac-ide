/********************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger
 *    - initial API and implementation and/or initial documentation
 ********************************************************************************/
package org.eclipse.fordiac.ide.model.libraryElement;

/** <!-- begin-user-doc --> A representation of the model object '<em><b>Sub App</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.SubApp#getSubAppNetwork <em>Sub App Network</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getSubApp()
 * @model
 * @generated */
public interface SubApp extends FBNetworkElement {
	/** Returns the value of the '<em><b>Sub App Network</b></em>' containment reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub App Network</em>' containment reference isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Sub App Network</em>' containment reference.
	 * @see #setSubAppNetwork(FBNetwork)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getSubApp_SubAppNetwork()
	 * @model containment="true" resolveProxies="true"
	 * @generated */
	FBNetwork getSubAppNetwork();

	/** Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.SubApp#getSubAppNetwork <em>Sub App
	 * Network</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Sub App Network</em>' containment reference.
	 * @see #getSubAppNetwork()
	 * @generated */
	void setSubAppNetwork(FBNetwork value);

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated */
	@Override
	SubAppType getType();

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated */
	boolean isUnfolded();

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated */
	boolean isTyped();

	/** <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> if it is an untyped subapp and the
	 * fbnetwork is not yet loaded load it from the type <!-- end-model-doc -->
	 * 
	 * @model
	 * @generated */
	FBNetwork loadSubAppNetwork();

} // SubApp
