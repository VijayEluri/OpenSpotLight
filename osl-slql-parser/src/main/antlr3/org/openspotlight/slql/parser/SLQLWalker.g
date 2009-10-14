/*
 * OpenSpotLight - Open Source IT Governance Platform
 *  
 * Copyright (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA 
 * or third-party contributors as indicated by the @author tags or express 
 * copyright attribution statements applied by the authors.  All third-party 
 * contributions are distributed under license by CARAVELATECH CONSULTORIA E 
 * TECNOLOGIA EM INFORMATICA LTDA. 
 * 
 * This copyrighted material is made available to anyone wishing to use, modify, 
 * copy, or redistribute it subject to the terms and conditions of the GNU 
 * Lesser General Public License, as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License  for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this distribution; if not, write to: 
 * Free Software Foundation, Inc. 
 * 51 Franklin Street, Fifth Floor 
 * Boston, MA  02110-1301  USA 
 * 
 *********************************************************************** 
 * OpenSpotLight - Plataforma de Governan�a de TI de C�digo Aberto 
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA 
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta 
 * @author ou por expressa atribui��o de direito autoral declarada e atribu�da pelo autor.
 * Todas as contribui��es de terceiros est�o distribu�das sob licen�a da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA. 
 * 
 * Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob os 
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free Software 
 * Foundation. 
 * 
 * Este programa � distribu�do na expectativa de que seja �til, por�m, SEM NENHUMA 
 * GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU ADEQUA��O A UMA
 * FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral Menor do GNU para mais detalhes.  
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto com este
 * programa; se n�o, escreva para: 
 * Free Software Foundation, Inc. 
 * 51 Franklin Street, Fifth Floor 
 * Boston, MA  02110-1301  USA
 */
tree grammar SLQLWalker;

options{
	output=template;
	tokenVocab=SLQL;
	ASTLabelType=CommonTree;
	TokenLabelType=CommonToken;
}

@header {
/*
 * OpenSpotLight - Open Source IT Governance Platform
 *  
 * Copyright (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA 
 * or third-party contributors as indicated by the @author tags or express 
 * copyright attribution statements applied by the authors.  All third-party 
 * contributions are distributed under license by CARAVELATECH CONSULTORIA E 
 * TECNOLOGIA EM INFORMATICA LTDA. 
 * 
 * This copyrighted material is made available to anyone wishing to use, modify, 
 * copy, or redistribute it subject to the terms and conditions of the GNU 
 * Lesser General Public License, as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License  for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this distribution; if not, write to: 
 * Free Software Foundation, Inc. 
 * 51 Franklin Street, Fifth Floor 
 * Boston, MA  02110-1301  USA 
 * 
 *********************************************************************** 
 * OpenSpotLight - Plataforma de Governan�a de TI de C�digo Aberto 
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA 
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta 
 * @author ou por expressa atribui��o de direito autoral declarada e atribu�da pelo autor.
 * Todas as contribui��es de terceiros est�o distribu�das sob licen�a da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA. 
 * 
 * Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob os 
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free Software 
 * Foundation. 
 * 
 * Este programa � distribu�do na expectativa de que seja �til, por�m, SEM NENHUMA 
 * GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU ADEQUA��O A UMA
 * FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral Menor do GNU para mais detalhes.  
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto com este
 * programa; se n�o, escreva para: 
 * Free Software Foundation, Inc. 
 * 51 Franklin Street, Fifth Floor 
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.slql.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

}

@members {
	private SLQLQueryInfo queryInfo = new SLQLQueryInfo();
}

compilationUnit returns [SLQLQueryInfo queryInfoReturn]
	:	enclosedCompilationUnit
	{	queryInfo.setContent($enclosedCompilationUnit.st.toString());
		$queryInfoReturn = queryInfo;	}
	;

enclosedCompilationUnit
	:	^(VT_COMPILATION_UNIT
			generalCollatorLevel?
			defineOutput?
			defineMessage*
			defineDominValues*
			(defineTarget {queryInfo.setDefineTargetContent($defineTarget.st.toString());})?
			(sl+=select)+)
		-> compilationUnit(generalCollatorLevel={$generalCollatorLevel.st},selects={$sl})
	;

generalCollatorLevel
	:	^(VT_COLLATOR_LEVEL collatorLevel)
		-> generalCollatorLevel(collatorLevel={$collatorLevel.st})
	;

collatorLevel
	:	IDENTICAL_VK	-> collatorLevelIdentical()
	|	PRIMARY_VK		-> collatorLevelPrimary()
	|	SECONDARY_VK	-> collatorLevelSecondary()
	|	TERTIARY_VK		-> collatorLevelTertiary()
	;

defineOutput
	:	GRAPHIC_MODEL_NAME
	{	queryInfo.setOutputModelName($GRAPHIC_MODEL_NAME.text);	}
	;

defineTarget
@init	{
	boolean hasSelect = false;
	boolean hasKeepResult = false;
}
@after	{
	queryInfo.setHasTarget(true);
	queryInfo.setTargetKeepsResult(hasKeepResult);
}	:	^(DEFINE_TARGET_VK (nodeType (KEEP_RESULT_VK {hasKeepResult = true;})?|select {hasSelect = true; hasKeepResult = $select.hasKeepResult;}))
	-> {hasSelect}? defineTargetWithSelect(select={$select.st})
	-> defineTarget(nodeType={$nodeType.st})
	;

defineMessage
	:	^(DEFINE_MESSAGE_VK variable STRING)
		{	queryInfo.getMessageVariables().put($variable.varName, $STRING.text);	}
	;

variable returns [String varName]
	:	VAR_INT		{$varName = $VAR_INT.text; queryInfo.getIntVariables().add($VAR_INT.text);}
	|	VAR_DEC		{$varName = $VAR_DEC.text; queryInfo.getDecVariables().add($VAR_DEC.text);}
	|	VAR_STRING	{$varName = $VAR_STRING.text; queryInfo.getStringVariables().add($VAR_STRING.text);}
	|	VAR_BOOL	{$varName = $VAR_BOOL.text; queryInfo.getBoolVariables().add($VAR_BOOL.text);}
	;

defineDominValues
@init	{
	Set<Object> domainValues = new HashSet<Object>();
}	:	^(DEFINE_DOMAIN_VK variable (ve=valueExpr {domainValues.add($ve.value);})+)
		{	queryInfo.getDomainVariables().put($variable.varName, domainValues);	}
	;

select returns [boolean hasKeepResult]
@init	{
	$hasKeepResult = false;
}	:	^(SELECT_VK selectedElements 
			byLink?
			where?
			executing?
			limitOffset?
			(keepResult {$hasKeepResult = true;})?
			orderBy?
			selectCollatorLevel?)
		-> select(selectedElements={$selectedElements.st},
					byLink={$byLink.st},
					where={$where.st},
					orderBy={$orderBy.st},
					keepResult={$keepResult.st},
					executing={$executing.st},
					limitOffset={$limitOffset.st},
					collatorLevel={$selectCollatorLevel.st})
	;

keepResult
	:	KEEP_RESULT_VK	-> keepResult()
	;

selectedElements
	:	STAR			-> selectStar()
	|	DOUBLE_STAR		-> selectDoubleStar()
	|	nodeType moreNodeTypes -> nodeTypes(firstType={$nodeType.st}, moreTypes={$moreNodeTypes.st}) 
	;

moreNodeTypes
	:	swcnt+=startWithCommaNodeType*	-> moreNodeTypes(nodeTypes={swcnt})
	;

startWithCommaNodeType
	:	nodeType -> startWithCommaNodeType(nodeType={$nodeType.st})
	;

byLink
	:	^(BY_LINK_VK (bld+=byLinkDefinition)+) -> byLink(byLinkDefinitions={$bld})
	;

byLinkDefinition
	:	LINK_TYPE_NAME linkDirections	-> byLinkDefinition(linkType={$LINK_TYPE_NAME.text}, linkDirections={$linkDirections.st})
	;

where
	:	^(WHERE_VK (wgbnt+=whereGroupByNodeType)+)
		-> where(content={$wgbnt})
	;

whereGroupByNodeType
	:	^(VT_GROUP_BY_NODE_TYPE nodeType expr)
		-> whereGroupByNodeType(nodeType={$nodeType.st}, expr={$expr.st})
	;

limitOffset
	:	^(LIMIT_VK valueExpr offset?)	-> limit(expr={$valueExpr.st},offset={$offset.st})
	;

offset
	:	^(OFFSET_VK valueExpr)			-> offset(expr={$valueExpr.st})
	;

orderBy
	:	^(ORDER_BY_VK (obgbnt+=orderByGroupByNodeType)+)
		-> orderBy(content={$obgbnt})
	;

orderByGroupByNodeType
@init	{
	int propertyCount = 0;
}	:	^(VT_GROUP_BY_NODE_TYPE nodeType ({propertyCount++;}pr+=propertyReference[propertyCount])*)
		-> orderByGroupByNodeType(nodeType={$nodeType.st}, properties={$pr} )
	;

selectCollatorLevel
	:	^(VT_COLLATOR_LEVEL collatorLevel)
		-> selectCollatorLevel(collatorLevel={$collatorLevel.st})
	;

propertyReference[int propertyCount]
@init	{
boolean beginsWithComma = true;
}	:	^(PROPERTY PROPERTY_NAME orderType)
	{	if(propertyCount > 0){
			beginsWithComma = true;
		}	}
		-> {beginsWithComma}? commaOrderProperty(propertyName={$PROPERTY_NAME.text}, orderType={$orderType.st})
		-> orderProperty(propertyName={$PROPERTY_NAME.text}, orderType={$orderType.st})
	;

orderType
	:	ASC_VK	-> ascType()
	|	DESC_VK	-> descType()
	;

expr
	:	^(AND_OPERATOR lhs=expr rhs=expr)
		-> booleanAndOperator(lhs={$lhs.st}, rhs={$rhs.st})
	|	^(OR_OPERATOR lhs=expr rhs=expr)
		-> booleanOrOperator(lhs={$lhs.st}, rhs={$rhs.st})
	|	^(NEGATED_OPERATOR nexpr=expr)
		-> negatedOperator(expr={$nexpr.st})
	|	^(PROPERTY ^(operator PROPERTY_NAME valueExpr))
		 -> whereProperty(operator={$operator.st}, propertyName={$PROPERTY_NAME.text}, value={$valueExpr.st})
	|	^(LINK_VK ^(numericOperator LINK_TYPE_NAME linkDirections numericValue))
		 -> whereLinkCount(operator={$numericOperator.st}, linkName={$LINK_TYPE_NAME.text}, linkDirections={$linkDirections.st}, value={$numericValue.st})
	;

linkDirections
	:	^(VT_LINK_DIRECTIONS linkDirectionOptions) -> linkDirections(linkDirectionOptions={$linkDirectionOptions.st}) 
	;

linkDirectionOptions
	:	A_VK B_VK BOTH_VK	-> linkDirectionABBoth()
	|	A_VK B_VK			-> linkDirectionAB()
	|	A_VK BOTH_VK		-> linkDirectionABoth()
	|	B_VK BOTH_VK		-> linkDirectionBBoth()
	|	A_VK				-> linkDirectionA()
	|	B_VK				-> linkDirectionB()
	|	BOTH_VK				-> linkDirectionBoth()
	;

operator
	:	so=stringOperator	-> operator(op={$so.st})
	|	no=numericOperator	-> operator(op={$no.st})
	;

stringOperator
	:	STARTS_WITH			-> startsWithOperator()
	|	ENDS_WITH			-> endsWithOperator()
	|	CONTAINS			-> containsOperator()
	;

numericOperator
	:	EQUALS				-> equalsOperator()
	|	GREATER				-> greaterOperator()
	|	LESSER				-> lesserOperator()
	|	GREATER_OR_EQUALS	-> greaterOrEqualsOperator()
	|	LESSER_OR_EQUALS	-> lesserOrEqualsOperator()
	|	NOT_EQUALS			-> notEqualsOperator()
	;

valueExpr returns [Object value]
	:	NULL_VK			{$value=null;}		-> nullValue()
	|	bv=booleanValue	{$value=$bv.value;}	-> anyValue(value={$bv.st})
	|	nv=numericValue	{$value=$nv.value;}	-> anyValue(value={$nv.st})
	|	sv=stringValue	{$value=$sv.value;}	-> anyValue(value={$sv.st})
	|	N_VK 
	;

booleanValue returns [Object value]
	:	TRUE_VK		{$value = true;}									->	trueValue()
	|	FALSE_VK	{$value = false;}									->	falseValue()
	|	VAR_BOOL	{queryInfo.getBoolVariables().add($VAR_BOOL.text);}	->	booleanVariableValue(variableName={$VAR_BOOL.text})
	;

stringValue returns [Object value]
	:	STRING		{$value = $STRING.text;}								-> stringValue(value={$STRING.text})
	|	VAR_STRING	{queryInfo.getStringVariables().add($VAR_STRING.text);}	-> stringVariableValue(variableName={$VAR_STRING.text})
	;

numericValue returns [Object value]
	:	INT		{$value = $INT.text;}								-> intValue(value={$INT.text})
	|	DEC		{$value = $DEC.text;}								-> decValue(value={$DEC.text})
	|	VAR_DEC	{queryInfo.getDecVariables().add($VAR_DEC.text);}	-> decVariableValue(variableName={$VAR_DEC.text})
	|	VAR_INT	{queryInfo.getIntVariables().add($VAR_INT.text);}	-> intVariableValue(variableName={$VAR_INT.text})
	;

executing
	:	^(VT_EXECUTING_TIMES valueExpr)	-> executing(times={$valueExpr.st})
	;

nodeType
	:	NODE_TYPE_NAME -> nodeType(typeName={$NODE_TYPE_NAME.text})
	|	NODE_TYPE_NAME_WITH_SUBTYPES -> nodeTypeWithSubTypes(typeName={$NODE_TYPE_NAME_WITH_SUBTYPES.text})
	;
