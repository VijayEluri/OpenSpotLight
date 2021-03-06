/**
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
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 * Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.graph.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openspotlight.graph.exception.MetaNodeTypeNotFoundException;
import org.openspotlight.graph.metadata.MetaNodeType;
import org.openspotlight.graph.metadata.Metadata;
import org.openspotlight.graph.query.XPathStatementBuilder.Statement;
import org.openspotlight.graph.query.info.SelectByNodeTypeInfo;
import org.openspotlight.graph.query.info.SelectByNodeTypeInfo.SLSelectTypeInfo;
import org.openspotlight.graph.query.info.WhereByNodeTypeInfo.SLWhereTypeInfo;
import org.openspotlight.graph.query.info.WhereByNodeTypeInfo.SLWhereTypeInfo.SLTypeStatementInfo;
import org.openspotlight.storage.domain.StorageNode;

/**
 * The Class SLSelectByNodeTypeExecuteCommand.
 * 
 * @author Vitor Hugo Chagas
 */
public class SelectByNodeTypeExecuteCommand extends SelectAbstractCommand {

    /** The command do. */
    private final SelectCommandDO          commandDO;

    /** The metadata. */
    private final Metadata                 metadata;

    /** The node wrapper list map. */
    private Map<String, List<StorageNode>> nodeWrapperListMap;

    /** The select info. */
    private final SelectByNodeTypeInfo     selectInfo;

    /**
     * Instantiates a new sL select by node type execute command.
     * 
     * @param selectInfo the select info
     * @param commandDO the command do
     */
    SelectByNodeTypeExecuteCommand(final SelectByNodeTypeInfo selectInfo,
                                   final SelectCommandDO commandDO) {
        this.selectInfo = selectInfo;
        this.commandDO = commandDO;
        metadata = commandDO.getMetadata();
    }

    /**
     * Filter by where statement.
     * 
     * @param statement the statement
     * @param typeName the type name
     * @param typeStatementInfo the type statement info
     * @throws SLPersistentTreeSessionException the SL persistent tree session exception
     */
    private void filterByWhereStatement(final Statement statement, final String typeName,
                                        final SLTypeStatementInfo typeStatementInfo) {
        //
        // List<SLConditionInfo> conditionInfoList = typeStatementInfo
        // .getConditionInfoList();
        //
        // for (SLConditionInfo conditionInfo : conditionInfoList) {
        //
        // Statement conditionStatement;
        // if (conditionInfo.getConditionalOperator() == null) {
        // conditionStatement = statement.openBracket();
        // } else {
        // conditionStatement = statement.operator(
        // conditionInfo.getConditionalOperator(),
        // conditionInfo.isConditionalNotOperator()).openBracket();
        // }
        //
        // if (conditionInfo.getInnerStatementInfo() == null) {
        // Statement idStatement = null;
        // if (commandDO.getPreviousNodeWrappers() != null) {
        // List<StorageNode> StorageNodes = nodeWrapperListMap
        // .get(typeName);
        // if (StorageNodes != null && !StorageNodes.isEmpty()) {
        // idStatement = conditionStatement.openBracket();
        // for (int j = 0; j < StorageNodes.size(); j++) {
        // StorageNode StorageNode = StorageNodes.get(j);
        // Condition idCondition;
        // if (j == 0)
        // idCondition = idStatement.condition();
        // else
        // idCondition = idStatement.operator(OR)
        // .condition();
        // idCondition.leftOperand("jcr:uuid").operator(EQUAL)
        // .rightOperand(StorageNode.getKeyAsString());
        // }
        // idStatement.closeBracket();
        // }
        // }
        // Condition condition;
        // if (idStatement == null) {
        // condition = conditionStatement.condition();
        // } else {
        // condition = conditionStatement.operator(AND).condition();
        // }
        //
        // String propertyName;
        // if (conditionInfo.getPropertyName() != null) {
        // propertyName = SLCommonSupport
        // .toUserPropertyName(conditionInfo.getPropertyName());
        // } else {
        // propertyName = (conditionInfo.getSide().equals(
        // SLSideType.A_SIDE) ? SLConsts.PROPERTY_NAME_SOURCE_COUNT
        // : SLConsts.PROPERTY_NAME_TARGET_COUNT)
        // + "." + conditionInfo.getLinkTypeName().hashCode();
        // propertyName = SLCommonSupport
        // .toInternalPropertyName(propertyName);
        // }
        //
        // condition
        // .leftOperand(propertyName)
        // .operator(conditionInfo.getRelationalOperator(),
        // conditionInfo.isRelationalNotOperator())
        // .rightOperand(conditionInfo.getValue());
        // } else {
        // filterByWhereStatement(conditionStatement, typeName,
        // conditionInfo.getInnerStatementInfo());
        // }
        //
        // conditionStatement.closeBracket();
        // }
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the select type info set.
     * 
     * @return the select type info set
     */
    private Set<SLSelectTypeInfo> getSelectTypeInfoSet() {
        final Set<SLSelectTypeInfo> set = new HashSet<SLSelectTypeInfo>();
        if (selectInfo.getAllTypes() != null) {
            if (selectInfo.getAllTypes().isOnWhere()) {
                final List<SLWhereTypeInfo> whereTypeInfoList = selectInfo
                        .getWhereStatementInfo().getWhereTypeInfoList();
                for (final SLWhereTypeInfo whereTypeInfo: whereTypeInfoList) {
                    final SLSelectTypeInfo typeInfo = new SLSelectTypeInfo(
                            whereTypeInfo.getName());
                    typeInfo.setSubTypes(whereTypeInfo.isSubTypes());
                    set.add(typeInfo);
                }
            } else {
                final Iterable<MetaNodeType> metaNodeTypes = metadata
                        .getMetaNodesTypes();
                for (final MetaNodeType metaNodeType: metaNodeTypes) {
                    final SLSelectTypeInfo typeInfo = new SLSelectTypeInfo(
                            metaNodeType.getTypeName());
                    typeInfo.setSubTypes(true);
                    set.add(typeInfo);
                }
            }
        } else {
            set.addAll(selectInfo.getTypeInfoList());
        }
        return set;
    }

    /**
     * Gets the where type info map.
     * 
     * @return the where type info map
     */
    private Map<String, SLWhereTypeInfo> getWhereTypeInfoMap()
            throws MetaNodeTypeNotFoundException {
        final Map<String, SLWhereTypeInfo> map = new HashMap<String, SLWhereTypeInfo>();
        final List<SLWhereTypeInfo> list = selectInfo.getWhereStatementInfo()
                .getWhereTypeInfoList();
        for (final SLWhereTypeInfo whereTypeInfo: list) {
            final List<String> typeNames = QuerySupport.getHierarchyTypeNames(
                    metadata, whereTypeInfo.getName(),
                    whereTypeInfo.isSubTypes());
            for (final String typeName: typeNames) {
                if (!map.containsKey(typeName)) {
                    final SLWhereTypeInfo typeInfo = new SLWhereTypeInfo(typeName);
                    typeInfo.setTypeStatementInfo(whereTypeInfo
                            .getTypeStatementInfo());
                    map.put(typeName, typeInfo);
                }
            }
        }
        return map;
    }

    /*
     * (non-Javadoc)
     * @see org.openspotlight.graph.query.SLSelectAbstractCommand#execute()
     */
    @Override
    public void execute() {
        throw new UnsupportedOperationException();
        // try {
        //
        // if (commandDO.getPreviousNodeWrappers() != null) {
        // nodeWrapperListMap =
        // QuerySupport.mapNodesByType(commandDO.getPreviousNodeWrappers());
        // }
        //
        // Set<SLSelectTypeInfo> typeInfoSet = getSelectTypeInfoSet();
        // String typePropName =
        // SLCommonSupport.toInternalPropertyName(SLConsts.PROPERTY_NAME_TYPE);
        // WhereByNodeTypeInfo whereStatementInfo =
        // selectInfo.getWhereStatementInfo();
        //
        // Set<String> typesNotFiltered = new HashSet<String>();
        // for (SLSelectTypeInfo typeInfo : typeInfoSet) {
        // List<String> hierarchyTypeNames =
        // QuerySupport.getHierarchyTypeNames(metadata, typeInfo.getName(),
        // typeInfo.isSubTypes());
        // typesNotFiltered.addAll(hierarchyTypeNames);
        // }
        //
        // XPathStatementBuilder statementBuilder = new
        // XPathStatementBuilder(commandDO.getTreeSession().getXPathRootPath()
        // + "/contexts//*");
        // Statement rootStatement = statementBuilder.getRootStatement();
        //
        // if (whereStatementInfo != null) {
        // List<SLWhereTypeInfo> whereTypeInfoList =
        // whereStatementInfo.getWhereTypeInfoList();
        // if (whereTypeInfoList != null && !whereTypeInfoList.isEmpty()) {
        // Map<String, SLWhereTypeInfo> whereTypeInfoMap =
        // getWhereTypeInfoMap();
        // List<SLWhereTypeInfo> list = new
        // ArrayList<SLWhereTypeInfo>(whereTypeInfoMap.values());
        // for (int i = 0; i < list.size(); i++) {
        // SLWhereTypeInfo typeInfo = list.get(i);
        // Statement typeStatement;
        // if (i > 0) typeStatement = rootStatement.operator(OR).openBracket();
        // else typeStatement = rootStatement.openBracket();
        // Statement typeFilterStatement =
        // typeStatement.condition().leftOperand(typePropName).operator(EQUAL).rightOperand(
        // typeInfo.getName()).operator(
        // AND).openBracket();
        // filterByWhereStatement(typeFilterStatement, typeInfo.getName(),
        // typeInfo.getTypeStatementInfo());
        // typeFilterStatement.closeBracket();
        // typeStatement.closeBracket();
        // typesNotFiltered.remove(typeInfo.getName());
        // }
        // }
        // }
        //
        // if (commandDO.getPreviousNodeWrappers() == null) {
        // for (String typeName : typesNotFiltered) {
        // Condition condition;
        // if (rootStatement.getConditionCount() == 0) {
        // condition = rootStatement.condition();
        // } else {
        // condition = rootStatement.operator(OR).condition();
        // }
        // condition.leftOperand(typePropName).operator(EQUAL).rightOperand(typeName);
        // }
        // }
        //
        // statementBuilder.setOrderBy(typePropName);
        // Set<StorageNode> StorageNodes = new HashSet<StorageNode>();
        // commandDO.setNodeWrappers(StorageNodes);
        //
        // if (statementBuilder.getRootStatement().getConditionCount() > 0) {
        // SLPersistentTreeSession treeSession = commandDO.getTreeSession();
        // String xpath = statementBuilder.getXPath();
        // SLPersistentQuery query = treeSession.createQuery(xpath,
        // SLPersistentQuery.TYPE_XPATH);
        // SLPersistentQueryResult result = query.execute();
        // StorageNodes.addAll(QuerySupport.wrapNodes(result.getNodes()));
        // }
        //
        // if (commandDO.getPreviousNodeWrappers() != null) {
        // for (String typeName : typesNotFiltered) {
        // List<StorageNode> typeNodeWrappers =
        // nodeWrapperListMap.get(typeName);
        // if (typeNodeWrappers != null) {
        // StorageNodes.addAll(typeNodeWrappers);
        // }
        // }
        // }
        // } catch (SLException e) {
        // throw new QueryException("Error on attempt to execute " +
        // this.getClass().getName() + " command.");
        // }
    }
}
