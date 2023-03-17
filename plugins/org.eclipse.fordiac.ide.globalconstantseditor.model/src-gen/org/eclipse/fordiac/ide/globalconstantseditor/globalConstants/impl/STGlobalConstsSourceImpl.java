/**
 * generated by Xtext 2.27.0
 */
package org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.GlobalConstantsPackage;
import org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.STGlobalConstsSource;
import org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.STVarGlobalDeclarationBlock;

import org.eclipse.fordiac.ide.structuredtextcore.stcore.impl.STSourceImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ST Global Consts Source</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.impl.STGlobalConstsSourceImpl#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class STGlobalConstsSourceImpl extends STSourceImpl implements STGlobalConstsSource {
	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList<STVarGlobalDeclarationBlock> elements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected STGlobalConstsSourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GlobalConstantsPackage.Literals.ST_GLOBAL_CONSTS_SOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<STVarGlobalDeclarationBlock> getElements() {
		if (elements == null) {
			elements = new EObjectContainmentEList<STVarGlobalDeclarationBlock>(STVarGlobalDeclarationBlock.class, this, GlobalConstantsPackage.ST_GLOBAL_CONSTS_SOURCE__ELEMENTS);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GlobalConstantsPackage.ST_GLOBAL_CONSTS_SOURCE__ELEMENTS:
				return ((InternalEList<?>)getElements()).basicRemove(otherEnd, msgs);
			default:
				return super.eInverseRemove(otherEnd, featureID, msgs);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GlobalConstantsPackage.ST_GLOBAL_CONSTS_SOURCE__ELEMENTS:
				return getElements();
			default:
				return super.eGet(featureID, resolve, coreType);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GlobalConstantsPackage.ST_GLOBAL_CONSTS_SOURCE__ELEMENTS:
				getElements().clear();
				getElements().addAll((Collection<? extends STVarGlobalDeclarationBlock>)newValue);
				return;
			default:
				super.eSet(featureID, newValue);
				return;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case GlobalConstantsPackage.ST_GLOBAL_CONSTS_SOURCE__ELEMENTS:
				getElements().clear();
				return;
			default:
				super.eUnset(featureID);
				return;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GlobalConstantsPackage.ST_GLOBAL_CONSTS_SOURCE__ELEMENTS:
				return elements != null && !elements.isEmpty();
			default:
				return super.eIsSet(featureID);
		}
	}

} //STGlobalConstsSourceImpl
