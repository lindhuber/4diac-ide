/*
 * generated by Xtext 2.25.0
 */
package org.eclipse.fordiac.ide.structuredtextalgorithm.serializer;

import com.google.inject.Inject;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fordiac.ide.structuredtextalgorithm.sTAlgorithm.STAlgorithm;
import org.eclipse.fordiac.ide.structuredtextalgorithm.sTAlgorithm.STAlgorithmBody;
import org.eclipse.fordiac.ide.structuredtextalgorithm.sTAlgorithm.STAlgorithmPackage;
import org.eclipse.fordiac.ide.structuredtextalgorithm.sTAlgorithm.STAlgorithms;
import org.eclipse.fordiac.ide.structuredtextalgorithm.services.STAlgorithmGrammarAccess;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.ArrayInitElement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.ArrayInitializerExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.Code;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STArrayAccessExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STAssignmentStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STBinaryExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCaseCases;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCaseStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STContinue;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCorePackage;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STDateAndTimeLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STDateLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STElseIfPart;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STElsePart;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STExit;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STFeatureExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STForStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STIfStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STMemberAccessExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STMultibitPartialExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STNop;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STNumericLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STRepeatStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STReturn;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STStringLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STTimeLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STTimeOfDayLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STUnaryExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STVarDeclaration;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STWhileStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.VarDeclarationBlock;
import org.eclipse.fordiac.ide.structuredtextcore.serializer.STCoreSemanticSequencer;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.serializer.ISerializationContext;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public abstract class AbstractSTAlgorithmSemanticSequencer extends STCoreSemanticSequencer {

	@Inject
	private STAlgorithmGrammarAccess grammarAccess;
	
	@Override
	public void sequence(ISerializationContext context, EObject semanticObject) {
		EPackage epackage = semanticObject.eClass().getEPackage();
		ParserRule rule = context.getParserRule();
		Action action = context.getAssignedAction();
		Set<Parameter> parameters = context.getEnabledBooleanParameters();
		if (epackage == STAlgorithmPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case STAlgorithmPackage.ST_ALGORITHM:
				sequence_STAlgorithm(context, (STAlgorithm) semanticObject); 
				return; 
			case STAlgorithmPackage.ST_ALGORITHM_BODY:
				sequence_STAlgorithmBody(context, (STAlgorithmBody) semanticObject); 
				return; 
			case STAlgorithmPackage.ST_ALGORITHMS:
				sequence_STAlgorithms(context, (STAlgorithms) semanticObject); 
				return; 
			}
		else if (epackage == STCorePackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case STCorePackage.ARRAY_INIT_ELEMENT:
				sequence_ArrayInitElement(context, (ArrayInitElement) semanticObject); 
				return; 
			case STCorePackage.ARRAY_INITIALIZER_EXPRESSION:
				sequence_ArrayInitializerExpression(context, (ArrayInitializerExpression) semanticObject); 
				return; 
			case STCorePackage.CODE:
				sequence_Code(context, (Code) semanticObject); 
				return; 
			case STCorePackage.ST_ARRAY_ACCESS_EXPRESSION:
				sequence_STAccessExpression(context, (STArrayAccessExpression) semanticObject); 
				return; 
			case STCorePackage.ST_ASSIGNMENT_STATEMENT:
				sequence_STAssignmentStatement(context, (STAssignmentStatement) semanticObject); 
				return; 
			case STCorePackage.ST_BINARY_EXPRESSION:
				sequence_STAddSubExpression_STAndExpression_STComparisonExpression_STEqualityExpression_STMulDivModExpression_STOrExpression_STPowerExpression_STSubrangeExpression_STXorExpression(context, (STBinaryExpression) semanticObject); 
				return; 
			case STCorePackage.ST_CASE_CASES:
				sequence_STCaseCases(context, (STCaseCases) semanticObject); 
				return; 
			case STCorePackage.ST_CASE_STATEMENT:
				sequence_STCaseStatement(context, (STCaseStatement) semanticObject); 
				return; 
			case STCorePackage.ST_CONTINUE:
				sequence_STStatement(context, (STContinue) semanticObject); 
				return; 
			case STCorePackage.ST_DATE_AND_TIME_LITERAL:
				sequence_STDateAndTimeLiteral(context, (STDateAndTimeLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_DATE_LITERAL:
				sequence_STDateLiteral(context, (STDateLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_ELSE_IF_PART:
				sequence_STElseIfPart(context, (STElseIfPart) semanticObject); 
				return; 
			case STCorePackage.ST_ELSE_PART:
				sequence_STElsePart(context, (STElsePart) semanticObject); 
				return; 
			case STCorePackage.ST_EXIT:
				sequence_STStatement(context, (STExit) semanticObject); 
				return; 
			case STCorePackage.ST_FEATURE_EXPRESSION:
				sequence_STFeatureExpression(context, (STFeatureExpression) semanticObject); 
				return; 
			case STCorePackage.ST_FOR_STATEMENT:
				sequence_STForStatement(context, (STForStatement) semanticObject); 
				return; 
			case STCorePackage.ST_IF_STATEMENT:
				sequence_STIfStatement(context, (STIfStatement) semanticObject); 
				return; 
			case STCorePackage.ST_MEMBER_ACCESS_EXPRESSION:
				sequence_STAccessExpression(context, (STMemberAccessExpression) semanticObject); 
				return; 
			case STCorePackage.ST_MULTIBIT_PARTIAL_EXPRESSION:
				sequence_STMultibitPartialExpression(context, (STMultibitPartialExpression) semanticObject); 
				return; 
			case STCorePackage.ST_NOP:
				sequence_STStatement(context, (STNop) semanticObject); 
				return; 
			case STCorePackage.ST_NUMERIC_LITERAL:
				sequence_STNumericLiteral(context, (STNumericLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_REPEAT_STATEMENT:
				sequence_STRepeatStatement(context, (STRepeatStatement) semanticObject); 
				return; 
			case STCorePackage.ST_RETURN:
				sequence_STStatement(context, (STReturn) semanticObject); 
				return; 
			case STCorePackage.ST_STRING_LITERAL:
				sequence_STStringLiteral(context, (STStringLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_TIME_LITERAL:
				sequence_STTimeLiteral(context, (STTimeLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_TIME_OF_DAY_LITERAL:
				sequence_STTimeOfDayLiteral(context, (STTimeOfDayLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_UNARY_EXPRESSION:
				sequence_STUnaryExpression(context, (STUnaryExpression) semanticObject); 
				return; 
			case STCorePackage.ST_VAR_DECLARATION:
				sequence_VarDeclaration(context, (STVarDeclaration) semanticObject); 
				return; 
			case STCorePackage.ST_WHILE_STATEMENT:
				sequence_STWhileStatement(context, (STWhileStatement) semanticObject); 
				return; 
			case STCorePackage.VAR_DECLARATION_BLOCK:
				if (rule == grammarAccess.getVarDeclarationBlockRule()) {
					sequence_VarDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getVarInputDeclarationBlockRule()) {
					sequence_VarInputDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getVarOutputDeclarationBlockRule()) {
					sequence_VarOutputDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getVarTempDeclarationBlockRule()) {
					sequence_VarTempDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null)
			errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Contexts:
	 *     STAlgorithmBody returns STAlgorithmBody
	 *
	 * Constraint:
	 *     (varTempDeclarations+=VarTempDeclarationBlock* statements+=STStatement*)
	 */
	protected void sequence_STAlgorithmBody(ISerializationContext context, STAlgorithmBody semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     STAlgorithm returns STAlgorithm
	 *
	 * Constraint:
	 *     (name=ID body=STAlgorithmBody)
	 */
	protected void sequence_STAlgorithm(ISerializationContext context, STAlgorithm semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, STAlgorithmPackage.Literals.ST_ALGORITHM__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, STAlgorithmPackage.Literals.ST_ALGORITHM__NAME));
			if (transientValues.isValueTransient(semanticObject, STAlgorithmPackage.Literals.ST_ALGORITHM__BODY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, STAlgorithmPackage.Literals.ST_ALGORITHM__BODY));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getSTAlgorithmAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getSTAlgorithmAccess().getBodySTAlgorithmBodyParserRuleCall_2_0(), semanticObject.getBody());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     STAlgorithms returns STAlgorithms
	 *
	 * Constraint:
	 *     algorithms+=STAlgorithm*
	 */
	protected void sequence_STAlgorithms(ISerializationContext context, STAlgorithms semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
}
