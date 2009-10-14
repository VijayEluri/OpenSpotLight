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
tree grammar SLQLTestWalker;

options{
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

}

compilationUnit
	:	^(VT_COMPILATION_UNIT
			generalCollatorLevel?
			defineOutput?
			defineMessage*
			defineDominValues*
			defineTarget?
			select+)
	;

generalCollatorLevel
	:	^(VT_COLLATOR_LEVEL collatorLevel)
	;

collatorLevel
	:	IDENTICAL_VK
	|	PRIMARY_VK
	|	SECONDARY_VK
	|	TERTIARY_VK
	;

defineOutput
	:	GRAPHIC_MODEL_NAME
	;

defineTarget
	:	^(DEFINE_TARGET_VK (nodeType KEEP_RESULT_VK?|select))
	;

defineMessage
	:	^(DEFINE_MESSAGE_VK variable STRING)
	;

variable
	:	VAR_INT
	|	VAR_DEC
	|	VAR_STRING
	|	VAR_BOOL
	;

defineDominValues
	:	^(DEFINE_DOMAIN_VK variable valueExpr+)
	;

select
	:	^(SELECT_VK selectedElements 
			byLink?
			where?
			executing?
			limitOffset?
			keepResult?
			orderBy?
			selectCollatorLevel?)
	;

keepResult
	:	KEEP_RESULT_VK
	;

selectedElements
	:	STAR
	|	DOUBLE_STAR
	|	nodeType+ 
	;

byLink
	:	^(BY_LINK_VK byLinkDefinition+)
	;

byLinkDefinition
	:	LINK_TYPE_NAME linkDirections
	;

where
	:	^(WHERE_VK whereGroupByNodeType+)
	;

whereGroupByNodeType
	:	^(VT_GROUP_BY_NODE_TYPE nodeType expr)
	;

limitOffset
	:	^(LIMIT_VK valueExpr offset?)
	;

offset
	:	^(OFFSET_VK valueExpr)
	;

orderBy
	:	^(ORDER_BY_VK orderByGroupByNodeType+)
	;

orderByGroupByNodeType
	:	^(VT_GROUP_BY_NODE_TYPE nodeType propertyReference*)
	;

selectCollatorLevel
	:	^(VT_COLLATOR_LEVEL collatorLevel)
	;

propertyReference
	:	^(PROPERTY PROPERTY_NAME orderType)
	;

orderType
	:	ASC_VK
	|	DESC_VK
	;

expr
	:	^(AND_OPERATOR expr expr)
	|	^(OR_OPERATOR expr expr)
	|	^(NEGATED_OPERATOR expr)
	|	^(PROPERTY ^(operator PROPERTY_NAME valueExpr))
	|	^(LINK_VK ^(numericOperator LINK_TYPE_NAME linkDirections numericValue))
	;

linkDirections
	:	^(VT_LINK_DIRECTIONS linkDirectionOptions) 
	;

linkDirectionOptions
	:	A_VK B_VK BOTH_VK
	|	A_VK B_VK
	|	A_VK BOTH_VK
	|	B_VK BOTH_VK
	|	A_VK
	|	B_VK
	|	BOTH_VK
	;

operator
	:	stringOperator
	|	numericOperator
	;

stringOperator
	:	STARTS_WITH
	|	ENDS_WITH
	|	CONTAINS
	;

numericOperator
	:	EQUALS
	|	GREATER
	|	LESSER
	|	GREATER_OR_EQUALS
	|	LESSER_OR_EQUALS
	|	NOT_EQUALS
	;

valueExpr
	:	NULL_VK
	|	booleanValue
	|	numericValue
	|	stringValue
	|	N_VK 
	;

booleanValue
	:	TRUE_VK
	|	FALSE_VK
	|	VAR_BOOL
	;

stringValue
	:	STRING
	|	VAR_STRING
	;

numericValue
	:	INT
	|	DEC
	|	VAR_DEC
	|	VAR_INT
	;

executing
	:	^(VT_EXECUTING_TIMES valueExpr)
	;

nodeType
	:	NODE_TYPE_NAME
	|	NODE_TYPE_NAME_WITH_SUBTYPES
	;

//Just to test purpose
linkType
	:	LINK_TYPE_NAME
	;

useCollatorLevel
	:	^(VT_COLLATOR_LEVEL collatorLevel)
	;