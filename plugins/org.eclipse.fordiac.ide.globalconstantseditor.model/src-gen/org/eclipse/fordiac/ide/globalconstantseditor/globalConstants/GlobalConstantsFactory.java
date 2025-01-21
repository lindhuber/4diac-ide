/**
 * generated by Xtext 2.27.0
 */
package org.eclipse.fordiac.ide.globalconstantseditor.globalConstants;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.GlobalConstantsPackage
 * @generated
 */
public interface GlobalConstantsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GlobalConstantsFactory eINSTANCE = org.eclipse.fordiac.ide.globalconstantseditor.globalConstants.impl.GlobalConstantsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>ST Global Consts Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ST Global Consts Source</em>'.
	 * @generated
	 */
	STGlobalConstsSource createSTGlobalConstsSource();

	/**
	 * Returns a new object of class '<em>ST Global Constants</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ST Global Constants</em>'.
	 * @generated
	 */
	STGlobalConstants createSTGlobalConstants();

	/**
	 * Returns a new object of class '<em>ST Var Global Declaration Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ST Var Global Declaration Block</em>'.
	 * @generated
	 */
	STVarGlobalDeclarationBlock createSTVarGlobalDeclarationBlock();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	GlobalConstantsPackage getGlobalConstantsPackage();

} //GlobalConstantsFactory
