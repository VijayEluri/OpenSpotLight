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
package org.openspotlight.graph.query;

import static org.openspotlight.graph.query.SLConditionalOperatorType.AND;
import static org.openspotlight.graph.query.SLConditionalOperatorType.OR;
import static org.openspotlight.graph.query.SLRelationalOperatorType.EQUAL;

import java.text.Collator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openspotlight.common.exception.SLException;
import org.openspotlight.graph.SLCollatorSupport;
import org.openspotlight.graph.SLCommonSupport;
import org.openspotlight.graph.SLConsts;
import org.openspotlight.graph.SLGraphSessionException;
import org.openspotlight.graph.persistence.SLPersistentQuery;
import org.openspotlight.graph.persistence.SLPersistentQueryResult;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;
import org.openspotlight.graph.persistence.SLPersistentTreeSessionException;
import org.openspotlight.graph.query.SLXPathStatementBuilder.Statement;
import org.openspotlight.graph.query.SLXPathStatementBuilder.Statement.Condition;
import org.openspotlight.graph.query.info.SLSelectStatementInfo;
import org.openspotlight.graph.query.info.SLSelectTypeInfo;
import org.openspotlight.graph.query.info.SLWhereStatementInfo;
import org.openspotlight.graph.query.info.SLWhereTypeInfo;
import org.openspotlight.graph.query.info.SLWhereTypeInfo.SLTypeStatementInfo;
import org.openspotlight.graph.query.info.SLWhereTypeInfo.SLTypeStatementInfo.SLTypeConditionInfo;

/**
 * The Class SLSelectByNodeTypeCommand.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLSelectByNodeTypeCommand extends SLSelectAbstractCommand {

	/** The select info. */
	private SLSelectStatementInfo selectInfo;
	
	/** The command do. */
	private SLSelectCommandDO commandDO;
	
	/** The node wrapper list map. */
	private Map<String, List<PNodeWrapper>> nodeWrapperListMap;
	
	/**
	 * Instantiates a new sL select by node type command.
	 * 
	 * @param selectInfo the select info
	 * @param commandDO the command do
	 */
	SLSelectByNodeTypeCommand(SLSelectStatementInfo selectInfo, SLSelectCommandDO commandDO) {
		this.selectInfo = selectInfo;
		this.commandDO = commandDO;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectAbstractCommand#execute()
	 */
	@Override
	public void execute() throws SLGraphSessionException {
		
		try {
			
			if (commandDO.getPreviousNodeWrappers() != null) {
				nodeWrapperListMap = SLQuerySupport.mapNodesByType(commandDO.getPreviousNodeWrappers());
			}
			
			List<SLSelectTypeInfo> typeInfoList = selectInfo.getTypeInfoList();
			String typePropName = SLCommonSupport.toInternalPropertyName(SLConsts.PROPERTY_NAME_TYPE);
			SLWhereStatementInfo whereStatementInfo = selectInfo.getWhereStatementInfo();

			Set<String> typesNotFiltered = new HashSet<String>();
	 		for (SLSelectTypeInfo typeInfo : typeInfoList) {
	 			typesNotFiltered.add(typeInfo.getName());
			}

	 		SLXPathStatementBuilder statementBuilder = new SLXPathStatementBuilder("//osl/contexts//*");
	 		Statement rootStatement = statementBuilder.getRootStatement();
	 		
	 		if (whereStatementInfo != null) {
	 	 		List<org.openspotlight.graph.query.info.SLWhereTypeInfo> whereTypeInfoList = whereStatementInfo.getWhereTypeInfoList();
	 			if (whereTypeInfoList != null && !whereTypeInfoList.isEmpty()) {
	 				List<SLWhereTypeInfo> list = whereStatementInfo.getWhereTypeInfoList(); 
	 				for (int i = 0; i < list.size(); i++) {
	 					SLWhereTypeInfo typeInfo = list.get(i);
	 					Statement typeStatement;
	 					if (i > 0) typeStatement = rootStatement.operator(OR).openBracket();
	 					else typeStatement = rootStatement.openBracket(); 
	 					Statement typeFilterStatement = typeStatement.condition()
	 						.leftOperand(typePropName).operator(EQUAL).rightOperand(typeInfo.getName())
	 						.operator(AND).openBracket();
	 					filterByWhereStatement(typeFilterStatement, typeInfo.getName(), typeInfo.getTypeStatementInfo());
	 					typeFilterStatement.closeBracket();
	 					typeStatement.closeBracket();
	 					typesNotFiltered.remove(typeInfo.getName());
					}
	 			}
	 		}
	 		
	 		if (commandDO.getPreviousNodeWrappers() == null) {
 				for (String typeName : typesNotFiltered) {
	 				Condition condition;
	 				if (rootStatement.getConditionCount() == 0) {
	 					condition = rootStatement.condition();
	 				}
	 				else {
	 					condition = rootStatement.operator(OR).condition();
	 				}
	 				condition.leftOperand(typePropName).operator(EQUAL).rightOperand(typeName);
 				}
	 		}
	 		
	 		statementBuilder.setOrderBy(typePropName);
	 		Set<PNodeWrapper> pNodeWrappers = new HashSet<PNodeWrapper>();
	 		commandDO.setNodeWrappers(pNodeWrappers);
	 		
	 		if (statementBuilder.getRootStatement().getConditionCount() > 0) {
				SLPersistentTreeSession treeSession = commandDO.getTreeSession();
				String xpath = statementBuilder.getXPath();
				SLPersistentQuery query = treeSession.createQuery(xpath, SLPersistentQuery.TYPE_XPATH);
				SLPersistentQueryResult result = query.execute();
				pNodeWrappers.addAll(SLQuerySupport.wrapNodes(result.getNodes()));
	 		}
			
			if (commandDO.getPreviousNodeWrappers() != null) {
				for (String typeName : typesNotFiltered) {
					List<PNodeWrapper> typeNodeWrappers = nodeWrapperListMap.get(typeName);
					if (typeNodeWrappers != null) {
						pNodeWrappers.addAll(typeNodeWrappers);
					}
				}
			}
		}
		catch (SLException e) {
			throw new SLGraphSessionException("Error on attempt to execute " + this.getClass().getName() + " command.");
		}
	}
	
	/**
	 * Filter by where statement.
	 * 
	 * @param statement the statement
	 * @param typeName the type name
	 * @param typeStatementInfo the type statement info
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 * @throws SLPersistentTreeSessionException the SL persistent tree session exception
	 */
	private void filterByWhereStatement(Statement statement, String typeName, SLTypeStatementInfo typeStatementInfo) throws SLGraphSessionException, SLPersistentTreeSessionException {
		
		List<SLTypeConditionInfo> conditionInfoList = typeStatementInfo.getConditionInfoList();
		
		for (SLTypeConditionInfo conditionInfo : conditionInfoList) {

			Statement conditionStatement;
			if (conditionInfo.getConditionalOperator() == null) {
				conditionStatement = statement.openBracket();
			}
			else {
				conditionStatement = statement.operator(conditionInfo.getConditionalOperator(), conditionInfo.isConditionalNotOperator()).openBracket();
			}
			
			if (conditionInfo.getInnerStatementInfo() == null) {
				Statement idStatement = null;
				if (commandDO.getPreviousNodeWrappers() != null) {
					List<PNodeWrapper> pNodeWrappers = nodeWrapperListMap.get(typeName);
					if (pNodeWrappers != null && !pNodeWrappers.isEmpty()) {
						idStatement = conditionStatement.openBracket();
						for (int j = 0; j < pNodeWrappers.size(); j++) {
							PNodeWrapper pNodeWrapper = pNodeWrappers.get(j);
							Condition idCondition;
							if (j == 0) idCondition = idStatement.condition();
							else idCondition = idStatement.operator(OR).condition();
							idCondition.leftOperand("jcr:uuid").operator(EQUAL).rightOperand(pNodeWrapper.getID());
						}
						idStatement.closeBracket();
					}
				}

				if (conditionInfo.getPropertyName() != null) {
					String propertyName = SLCommonSupport.toUserPropertyName(conditionInfo.getPropertyName());
					Statement propStatement = idStatement == null ? conditionStatement.openBracket() : conditionStatement.operator(AND).openBracket();
					propStatement.condition().leftOperand(propertyName).operator(conditionInfo.getRelationalOperator(), conditionInfo.isRelationalNotOperator()).rightOperand(conditionInfo.getValue());
					int collatorStrength = commandDO.getCollatorStrength();
					if (conditionInfo.getValue() instanceof String && collatorStrength != Collator.IDENTICAL) {
						propertyName = SLCollatorSupport.getCollatorPropName(conditionInfo.getPropertyName(), commandDO.getCollatorStrength());
						String value = SLCollatorSupport.getCollatorKey(commandDO.getCollatorStrength(), conditionInfo.getValue().toString());
						propStatement.operator(OR).condition().leftOperand(propertyName).operator(conditionInfo.getRelationalOperator(), conditionInfo.isRelationalNotOperator()).rightOperand(value);
					}
					propStatement.closeBracket();
				}
				else {
					Condition condition = idStatement == null ? conditionStatement.condition() : conditionStatement.operator(AND).condition();
					String propertyName = (conditionInfo.getSide().equals(SLSideType.A_SIDE) ? SLConsts.PROPERTY_NAME_SOURCE_COUNT : SLConsts.PROPERTY_NAME_TARGET_COUNT)	+ "." + conditionInfo.getLinkTypeName().hashCode();
					propertyName = SLCommonSupport.toInternalPropertyName(propertyName);
					if (conditionInfo.getRelationalOperator().equals(EQUAL) && conditionInfo.getValue().equals(0)) {
						condition.leftOperand(propertyName).inexistent();	
					}
					else {
						condition.leftOperand(propertyName).operator(conditionInfo.getRelationalOperator(), conditionInfo.isRelationalNotOperator()).rightOperand(conditionInfo.getValue());	
					}
				}
			}
			else {
				filterByWhereStatement(conditionStatement, typeName, conditionInfo.getInnerStatementInfo());
			}
			
			conditionStatement.closeBracket();
		}
	}
	
}