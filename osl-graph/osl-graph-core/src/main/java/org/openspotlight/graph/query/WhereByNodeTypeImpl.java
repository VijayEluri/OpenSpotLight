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

import org.openspotlight.graph.query.info.WhereByNodeTypeInfo;
import org.openspotlight.graph.query.info.WhereByNodeTypeInfo.SLWhereTypeInfo;
import org.openspotlight.graph.query.info.WhereByNodeTypeInfo.SLWhereTypeInfo.SLTypeStatementInfo;
import org.openspotlight.graph.query.info.WhereByNodeTypeInfo.SLWhereTypeInfo.SLTypeStatementInfo.SLConditionInfo;

/**
 * The Class SLWhereByNodeTypeImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class WhereByNodeTypeImpl implements WhereByNodeType, WhereByNodeTypeInfoGetter {

    /** The end. */
    private End                   end;

    /** The where by node type info. */
    private WhereByNodeTypeInfo whereByNodeTypeInfo;

    /**
     * Instantiates a new sL where by node type impl.
     * 
     * @param selectFacade the select facade
     * @param orderBy the order by
     * @param whereByNodeTypeInfo the where by node type info
     */
    public WhereByNodeTypeImpl(
                                  SelectFacade selectFacade, OrderByStatement orderBy,
                                  WhereByNodeTypeInfo whereByNodeTypeInfo ) {
        this(new EndImpl(selectFacade, whereByNodeTypeInfo, orderBy), whereByNodeTypeInfo);
    }

    /**
     * Instantiates a new sL where by node type impl.
     * 
     * @param end the end
     * @param whereByNodeTypeInfo the where by node type info
     */
    public WhereByNodeTypeImpl(
                                  End end, WhereByNodeTypeInfo whereByNodeTypeInfo ) {
        this.end = end;
        this.whereByNodeTypeInfo = whereByNodeTypeInfo;
    }

    /**
     * {@inheritDoc}
     */
    public WhereByNodeTypeInfo getWhereStatementInfo() {
        return whereByNodeTypeInfo;
    }

    /**
     * {@inheritDoc}
     */
    public Type type( String typeName ) {
        SLWhereTypeInfo typeInfo = new SLWhereTypeInfo(typeName);
        whereByNodeTypeInfo.getWhereTypeInfoList().add(typeInfo);
        return new TypeImpl(this, typeInfo);
    }

    /**
     * {@inheritDoc}
     */
    public End whereEnd() {
        return end;
    }

    /**
     * private void verifyConditionalOperator() { if (statementInfo.getConditionInfoList().isEmpty()) { throw new
     * SLInvalidQuerySyntaxRuntimeException( "the first condition of a statement must not start with AND or OR operators" ); } }
     */

    public static class EndImpl implements End {

        /** The where by node type info. */
        private WhereByNodeTypeInfo whereByNodeTypeInfo;

        /** The order by statement. */
        private OrderByStatement    orderByStatement;

        /** The select facade. */
        private SelectFacade        selectFacade;

        /**
         * Instantiates a new end impl.
         * 
         * @param selectFacade the select facade
         * @param whereByNodeTypeInfo the where by node type info
         * @param orderByStatement the order by statement
         */
        public EndImpl(
                        SelectFacade selectFacade, WhereByNodeTypeInfo whereByNodeTypeInfo,
                        OrderByStatement orderByStatement ) {
            this.selectFacade = selectFacade;
            this.whereByNodeTypeInfo = whereByNodeTypeInfo;
            this.orderByStatement = orderByStatement;
        }

        /**
         * {@inheritDoc}
         */
        public OrderByStatement orderBy() {
            return orderByStatement;
        }

        /**
         * {@inheritDoc}
         */
        public End keepResult() {
            whereByNodeTypeInfo.getSelectByNodeTypeInfo().setKeepResult(true);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public End limit( Integer limit ) {
            whereByNodeTypeInfo.getSelectByNodeTypeInfo().setLimit(limit);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public End limit( Integer limit,
                          Integer offset ) {
            whereByNodeTypeInfo.getSelectByNodeTypeInfo().setLimit(limit);
            whereByNodeTypeInfo.getSelectByNodeTypeInfo().setOffset(offset);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public End executeXTimes() {
            whereByNodeTypeInfo.getSelectByNodeTypeInfo().setXTimes(0);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public End executeXTimes( int x ) {
            whereByNodeTypeInfo.getSelectByNodeTypeInfo().setXTimes(x);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public SelectByLinkType selectByLinkType() {
            return selectFacade.selectByLinkType();
        }

        /**
         * {@inheritDoc}
         */
        public SelectByNodeType selectByNodeType() {
            return selectFacade.selectByNodeType();
        }

        /**
         * {@inheritDoc}
         */
        public SelectByLinkCount selectByLinkCount() {
            return selectFacade.selectByLinkCount();
        }

        /**
         * {@inheritDoc}
         */
        public SelectStatement select() {
            return selectFacade.select();
        }
    }

    /**
     * The Class TypeImpl.
     * 
     * @author Vitor Hugo Chagas
     */
    public static class TypeImpl implements Type {

        /** The where statement. */
        private WhereByNodeType whereStatement;

        /** The type info. */
        private SLWhereTypeInfo   typeInfo;

        /**
         * Instantiates a new type impl.
         * 
         * @param whereStatement the where statement
         * @param typeInfo the type info
         */
        public TypeImpl(
                         WhereByNodeType whereStatement, SLWhereTypeInfo typeInfo ) {
            this.whereStatement = whereStatement;
            this.typeInfo = typeInfo;
        }

        /**
         * {@inheritDoc}
         */
        public SubTypes subTypes() {
            typeInfo.setSubTypes(true);
            return new SubTypesImpl(whereStatement, typeInfo);
        }

        /**
         * {@inheritDoc}
         */
        public Each each() {
            SLTypeStatementInfo whereStatementInfo = new SLTypeStatementInfo(typeInfo);
            typeInfo.setTypeStatementInfo(whereStatementInfo);
            SLConditionInfo conditionInfo = whereStatementInfo.addCondition();
            return new EachImpl(whereStatement, conditionInfo);
        }

        /**
         * The Class SubTypesImpl.
         * 
         * @author Vitor Hugo Chagas
         */
        public static class SubTypesImpl implements SubTypes {

            /** The where statement. */
            private WhereByNodeType whereStatement;

            /** The type info. */
            private SLWhereTypeInfo   typeInfo;

            /**
             * Instantiates a new sub types impl.
             * 
             * @param whereStatement the where statement
             * @param typeInfo the type info
             */
            public SubTypesImpl(
                                 WhereByNodeType whereStatement, SLWhereTypeInfo typeInfo ) {
                this.whereStatement = whereStatement;
                this.typeInfo = typeInfo;
            }

            /**
             * {@inheritDoc}
             */
            public Each each() {
                SLTypeStatementInfo whereStatementInfo = new SLTypeStatementInfo(typeInfo);
                typeInfo.setTypeStatementInfo(whereStatementInfo);
                SLConditionInfo conditionInfo = whereStatementInfo.addCondition();
                return new EachImpl(whereStatement, conditionInfo);
            }
        }

        /**
         * The Class EachImpl.
         * 
         * @author Vitor Hugo Chagas
         */
        public static class EachImpl implements Each {

            /** The where statement. */
            private WhereByNodeType whereStatement;

            /** The condition info. */
            private SLConditionInfo   conditionInfo;

            /** The outer each. */
            private Each              outerEach;

            /**
             * Instantiates a new each impl.
             * 
             * @param whereStatement the where statement
             * @param conditionInfo the condition info
             */
            public EachImpl(
                             WhereByNodeType whereStatement, SLConditionInfo conditionInfo ) {
                this(whereStatement, conditionInfo, null);
            }

            /**
             * Instantiates a new each impl.
             * 
             * @param whereStatement the where statement
             * @param conditionInfo the condition info
             * @param outerEach the outer each
             */
            public EachImpl(
                             WhereByNodeType whereStatement, SLConditionInfo conditionInfo, Each outerEach ) {
                this.whereStatement = whereStatement;
                this.conditionInfo = conditionInfo;
                this.outerEach = outerEach;
            }

            /**
             * {@inheritDoc}
             */
            public Property property( String name ) {
                conditionInfo.setPropertyName(name);
                return new PropertyImpl(whereStatement, this, outerEach, conditionInfo);
            }

            /**
             * {@inheritDoc}
             */
            public Link link( String name ) {
                conditionInfo.setLinkTypeName(name);
                return new LinkImpl(whereStatement, this, outerEach, conditionInfo);
            }

            /**
             * The Class LinkImpl.
             * 
             * @author Vitor Hugo Chagas
             */
            public static class LinkImpl implements Link {

                /** The where statement. */
                private WhereByNodeType whereStatement;

                /** The each. */
                private Each              each;

                /** The outer each. */
                private Each              outerEach;

                /** The condition info. */
                private SLConditionInfo   conditionInfo;

                /**
                 * Instantiates a new link impl.
                 * 
                 * @param whereStatement the where statement
                 * @param each the each
                 * @param outerEach the outer each
                 * @param conditionInfo the condition info
                 */
                public LinkImpl(
                                 WhereByNodeType whereStatement, Each each, Each outerEach, SLConditionInfo conditionInfo ) {
                    this.whereStatement = whereStatement;
                    this.each = each;
                    this.outerEach = outerEach;
                    this.conditionInfo = conditionInfo;
                }

                /**
                 * {@inheritDoc}
                 */
                public Side a() {
                    conditionInfo.setSide(SideType.A_SIDE);
                    return new SideImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Side b() {
                    conditionInfo.setSide(SideType.B_SIDE);
                    return new SideImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * The Class SideImpl.
                 * 
                 * @author Vitor Hugo Chagas
                 */
                public static class SideImpl implements Side {

                    /** The where statement. */
                    private WhereByNodeType whereStatement;

                    /** The each. */
                    private Each              each;

                    /** The outer each. */
                    private Each              outerEach;

                    /** The condition info. */
                    private SLConditionInfo   conditionInfo;

                    /**
                     * Instantiates a new side impl.
                     * 
                     * @param whereStatement the where statement
                     * @param each the each
                     * @param outerEach the outer each
                     * @param conditionInfo the condition info
                     */
                    public SideImpl(
                                     WhereByNodeType whereStatement, Each each, Each outerEach, SLConditionInfo conditionInfo ) {
                        this.whereStatement = whereStatement;
                        this.each = each;
                        this.outerEach = outerEach;
                        this.conditionInfo = conditionInfo;
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Count count() {
                        return new CountImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * The Class CountImpl.
                     * 
                     * @author Vitor Hugo Chagas
                     */
                    public static class CountImpl implements Count {

                        /** The each. */
                        private Each              each;

                        /** The outer each. */
                        private Each              outerEach;

                        /** The where statement. */
                        private WhereByNodeType whereStatement;

                        /** The condition info. */
                        private SLConditionInfo   conditionInfo;

                        /**
                         * Instantiates a new count impl.
                         * 
                         * @param whereStatement the where statement
                         * @param each the each
                         * @param outerEach the outer each
                         * @param conditionInfo the condition info
                         */
                        public CountImpl(
                                          WhereByNodeType whereStatement, Each each, Each outerEach,
                                          SLConditionInfo conditionInfo ) {
                            this.each = each;
                            this.whereStatement = whereStatement;
                            this.conditionInfo = conditionInfo;
                            this.outerEach = outerEach;
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public Count not() {
                            conditionInfo.setRelationalNotOperator(true);
                            return this;
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public Operator lesserThan() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.LESSER_THAN);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public Operator greaterThan() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.GREATER_THAN);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public Operator equalsTo() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.EQUAL);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public Operator lesserOrEqualThan() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.LESSER_OR_EQUAL_THAN);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public Operator greaterOrEqualThan() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.GREATER_OR_EQUAL_THAN);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /*
                         * (non-Javadoc)
                         * 
                         * @see
                         * org.openspotlight.graph.query.SLWhereByNodeType.Type
                         * .Each.Property#contains()
                         */
                        /**
                         * Contains.
                         * 
                         * @return the operator
                         */
                        public Operator contains() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.CONTAINS);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /*
                         * (non-Javadoc)
                         * 
                         * @see
                         * org.openspotlight.graph.query.SLWhereByNodeType.Type
                         * .Each.Property#startsWith()
                         */
                        /**
                         * Starts with.
                         * 
                         * @return the operator
                         */
                        public Operator startsWith() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.STARTS_WITH);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /*
                         * (non-Javadoc)
                         * 
                         * @see
                         * org.openspotlight.graph.query.SLWhereByNodeType.Type
                         * .Each.Property#endsWith()
                         */
                        /**
                         * Ends with.
                         * 
                         * @return the operator
                         */
                        public Operator endsWith() {
                            conditionInfo.setRelationalOperator(RelationalOperatorType.ENDS_WITH);
                            return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                        }

                        /**
                         * The Class OperatorImpl.
                         * 
                         * @author Vitor Hugo Chagas
                         */
                        public static class OperatorImpl implements Operator {

                            /** The each. */
                            private Each              each;

                            /** The outer each. */
                            private Each              outerEach;

                            /** The where statement. */
                            private WhereByNodeType whereStatement;

                            /** The condition info. */
                            private SLConditionInfo   conditionInfo;

                            /**
                             * Instantiates a new operator impl.
                             * 
                             * @param whereStatement the where statement
                             * @param each the each
                             * @param outerEach the outer each
                             * @param conditionInfo the condition info
                             */
                            public OperatorImpl(
                                                 WhereByNodeType whereStatement, Each each, Each outerEach,
                                                 SLConditionInfo conditionInfo ) {
                                this.each = each;
                                this.outerEach = outerEach;
                                this.whereStatement = whereStatement;
                                this.conditionInfo = conditionInfo;
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public Value value( Integer value ) {
                                conditionInfo.setValue(value);
                                return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                            }

                            /**
                             * The Class ValueImpl.
                             * 
                             * @author Vitor Hugo Chagas
                             */
                            public static class ValueImpl implements Value {

                                /** The each. */
                                private Each              each;

                                /** The outer each. */
                                private Each              outerEach;

                                /** The where statement. */
                                private WhereByNodeType whereStatement;

                                /** The condition info. */
                                private SLConditionInfo   conditionInfo;

                                /**
                                 * Instantiates a new value impl.
                                 * 
                                 * @param whereStatement the where statement
                                 * @param each the each
                                 * @param outerEach the outer each
                                 * @param conditionInfo the condition info
                                 */
                                public ValueImpl(
                                                  WhereByNodeType whereStatement, Each each, Each outerEach,
                                                  SLConditionInfo conditionInfo ) {
                                    this.each = each;
                                    this.outerEach = outerEach;
                                    this.whereStatement = whereStatement;
                                    this.conditionInfo = conditionInfo;
                                }

                                /**
                                 * {@inheritDoc}
                                 */
                                public WhereByNodeType typeEnd() {
                                    return whereStatement;
                                }

                                /**
                                 * {@inheritDoc}
                                 */
                                public RelationalOperator or() {
                                    SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                                    SLConditionInfo newConditionInfo = outerStatementInfo.addCondition(ConditionalOperatorType.OR);
                                    Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
                                    return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
                                }

                                /**
                                 * {@inheritDoc}
                                 */
                                public RelationalOperator and() {
                                    SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                                    SLConditionInfo newConditionInfo = outerStatementInfo.addCondition(ConditionalOperatorType.AND);
                                    Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
                                    return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
                                }

                                /**
                                 * {@inheritDoc}
                                 */
                                public CloseBracket closeBracket() {
                                    return new CloseBracketImpl(whereStatement, outerEach, conditionInfo);
                                }

                                /**
                                 * The Class RelationalOperatorImpl.
                                 * 
                                 * @author Vitor Hugo Chagas
                                 */
                                public static class RelationalOperatorImpl implements RelationalOperator {

                                    /** The each. */
                                    private Each              each;

                                    /** The where statement. */
                                    private WhereByNodeType whereStatement;

                                    /** The condition info. */
                                    private SLConditionInfo   conditionInfo;

                                    /**
                                     * Instantiates a new relational operator impl.
                                     * 
                                     * @param whereStatement the where statement
                                     * @param each the each
                                     * @param conditionInfo the condition info
                                     */
                                    public RelationalOperatorImpl(
                                                                   WhereByNodeType whereStatement, Each each,
                                                                   SLConditionInfo conditionInfo ) {
                                        this.each = each;
                                        this.whereStatement = whereStatement;
                                        this.conditionInfo = conditionInfo;
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public RelationalOperator not() {
                                        conditionInfo.setConditionalNotOperator(true);
                                        return this;
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public WhereByNodeType comma() {
                                        return this.whereStatement;
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public Each each() {
                                        return this.each;
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public OpenBracket openBracket() {
                                        SLTypeStatementInfo newStatementInfo = new SLTypeStatementInfo(
                                                                                                       conditionInfo.getTypeInfo());
                                        conditionInfo.setInnerStatementInfo(newStatementInfo);
                                        SLConditionInfo newConditionInfo = newStatementInfo.addCondition();
                                        Each each = new EachImpl(whereStatement, newConditionInfo, this.each);
                                        return new OpenBracketImpl(each);
                                    }

                                    /**
                                     * The Class OpenBracketImpl.
                                     * 
                                     * @author Vitor Hugo Chagas
                                     */
                                    public static class OpenBracketImpl implements OpenBracket {

                                        /** The each. */
                                        private Each each;

                                        /**
                                         * Instantiates a new open bracket impl.
                                         * 
                                         * @param each the each
                                         */
                                        public OpenBracketImpl(
                                                                Each each ) {
                                            this.each = each;
                                        }

                                        /**
                                         * {@inheritDoc}
                                         */
                                        public Each each() {
                                            return each;
                                        }
                                    }
                                }

                                /**
                                 * The Class CloseBracketImpl.
                                 * 
                                 * @author Vitor Hugo Chagas
                                 */
                                public static class CloseBracketImpl implements CloseBracket {

                                    /** The where statement. */
                                    private WhereByNodeType whereStatement;

                                    /** The outer each. */
                                    private Each              outerEach;

                                    /** The condition info. */
                                    private SLConditionInfo   conditionInfo;

                                    /**
                                     * Instantiates a new close bracket impl.
                                     * 
                                     * @param whereStatement the where statement
                                     * @param outerEach the outer each
                                     * @param conditionInfo the condition info
                                     */
                                    public CloseBracketImpl(
                                                             WhereByNodeType whereStatement, Each outerEach,
                                                             SLConditionInfo conditionInfo ) {
                                        this.whereStatement = whereStatement;
                                        this.outerEach = outerEach;
                                        this.conditionInfo = conditionInfo;
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public RelationalOperator or() {
                                        SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                                        outerStatementInfo.addCondition(ConditionalOperatorType.OR);
                                        Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
                                        return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public RelationalOperator and() {
                                        SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                                        outerStatementInfo.addCondition(ConditionalOperatorType.AND);
                                        Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
                                        return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
                                    }

                                    /**
                                     * {@inheritDoc}
                                     */
                                    public WhereByNodeType typeEnd() {
                                        return whereStatement;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**
             * The Class PropertyImpl.
             * 
             * @author Vitor Hugo Chagas
             */
            public static class PropertyImpl implements Property {

                /** The each. */
                private Each              each;

                /** The outer each. */
                private Each              outerEach;

                /** The where statement. */
                private WhereByNodeType whereStatement;

                /** The condition info. */
                private SLConditionInfo   conditionInfo;

                /**
                 * Instantiates a new property impl.
                 * 
                 * @param whereStatement the where statement
                 * @param each the each
                 * @param outerEach the outer each
                 * @param conditionInfo the condition info
                 */
                public PropertyImpl(
                                     WhereByNodeType whereStatement, Each each, Each outerEach, SLConditionInfo conditionInfo ) {
                    this.each = each;
                    this.whereStatement = whereStatement;
                    this.conditionInfo = conditionInfo;
                    this.outerEach = outerEach;
                }

                /**
                 * {@inheritDoc}
                 */
                public Property not() {
                    conditionInfo.setRelationalNotOperator(true);
                    return this;
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator lesserThan() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.LESSER_THAN);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator greaterThan() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.GREATER_THAN);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator equalsTo() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.EQUAL);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator lesserOrEqualThan() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.LESSER_OR_EQUAL_THAN);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator greaterOrEqualThan() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.GREATER_OR_EQUAL_THAN);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator contains() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.CONTAINS);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator startsWith() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.STARTS_WITH);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * {@inheritDoc}
                 */
                public Operator endsWith() {
                    conditionInfo.setRelationalOperator(RelationalOperatorType.ENDS_WITH);
                    return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
                }

                /**
                 * The Class OperatorImpl.
                 * 
                 * @author Vitor Hugo Chagas
                 */
                public static class OperatorImpl implements Operator {

                    /** The each. */
                    private Each              each;

                    /** The outer each. */
                    private Each              outerEach;

                    /** The where statement. */
                    private WhereByNodeType whereStatement;

                    /** The condition info. */
                    private SLConditionInfo   conditionInfo;

                    /**
                     * Instantiates a new operator impl.
                     * 
                     * @param whereStatement the where statement
                     * @param each the each
                     * @param outerEach the outer each
                     * @param conditionInfo the condition info
                     */
                    public OperatorImpl(
                                         WhereByNodeType whereStatement, Each each, Each outerEach,
                                         SLConditionInfo conditionInfo ) {
                        this.each = each;
                        this.outerEach = outerEach;
                        this.whereStatement = whereStatement;
                        this.conditionInfo = conditionInfo;
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Value value( String value ) {
                        conditionInfo.setValue(value);
                        return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Value value( Integer value ) {
                        conditionInfo.setValue(value);
                        return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Value value( Long value ) {
                        conditionInfo.setValue(value);
                        return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Value value( Float value ) {
                        conditionInfo.setValue(value);
                        return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Value value( Double value ) {
                        conditionInfo.setValue(value);
                        return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    public Value value( Boolean value ) {
                        conditionInfo.setValue(value);
                        return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
                    }

                    /**
                     * The Class ValueImpl.
                     * 
                     * @author Vitor Hugo Chagas
                     */
                    public static class ValueImpl implements Value {

                        /** The each. */
                        private Each              each;

                        /** The outer each. */
                        private Each              outerEach;

                        /** The where statement. */
                        private WhereByNodeType whereStatement;

                        /** The condition info. */
                        private SLConditionInfo   conditionInfo;

                        /**
                         * Instantiates a new value impl.
                         * 
                         * @param whereStatement the where statement
                         * @param each the each
                         * @param outerEach the outer each
                         * @param conditionInfo the condition info
                         */
                        public ValueImpl(
                                          WhereByNodeType whereStatement, Each each, Each outerEach,
                                          SLConditionInfo conditionInfo ) {
                            this.each = each;
                            this.outerEach = outerEach;
                            this.whereStatement = whereStatement;
                            this.conditionInfo = conditionInfo;
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public WhereByNodeType typeEnd() {
                            return whereStatement;
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public RelationalOperator or() {
                            SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                            SLConditionInfo newConditionInfo = outerStatementInfo.addCondition(ConditionalOperatorType.OR);
                            Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
                            return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public RelationalOperator and() {
                            SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                            SLConditionInfo newConditionInfo = outerStatementInfo.addCondition(ConditionalOperatorType.AND);
                            Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
                            return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
                        }

                        /**
                         * {@inheritDoc}
                         */
                        public CloseBracket closeBracket() {
                            return new CloseBracketImpl(whereStatement, outerEach, conditionInfo);
                        }

                        /**
                         * The Class RelationalOperatorImpl.
                         * 
                         * @author Vitor Hugo Chagas
                         */
                        public static class RelationalOperatorImpl implements RelationalOperator {

                            /** The each. */
                            private Each              each;

                            /** The where statement. */
                            private WhereByNodeType whereStatement;

                            /** The condition info. */
                            private SLConditionInfo   conditionInfo;

                            /**
                             * Instantiates a new relational operator impl.
                             * 
                             * @param whereStatement the where statement
                             * @param each the each
                             * @param conditionInfo the condition info
                             */
                            public RelationalOperatorImpl(
                                                           WhereByNodeType whereStatement, Each each,
                                                           SLConditionInfo conditionInfo ) {
                                this.each = each;
                                this.whereStatement = whereStatement;
                                this.conditionInfo = conditionInfo;
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public RelationalOperator not() {
                                conditionInfo.setConditionalNotOperator(true);
                                return this;
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public WhereByNodeType comma() {
                                return this.whereStatement;
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public Each each() {
                                return this.each;
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public OpenBracket openBracket() {
                                SLTypeStatementInfo newStatementInfo = new SLTypeStatementInfo(conditionInfo.getTypeInfo());
                                conditionInfo.setInnerStatementInfo(newStatementInfo);
                                SLConditionInfo newConditionInfo = newStatementInfo.addCondition();
                                Each each = new EachImpl(whereStatement, newConditionInfo, this.each);
                                return new OpenBracketImpl(each);
                            }

                            /**
                             * The Class OpenBracketImpl.
                             * 
                             * @author Vitor Hugo Chagas
                             */
                            public static class OpenBracketImpl implements OpenBracket {

                                /** The each. */
                                private Each each;

                                /**
                                 * Instantiates a new open bracket impl.
                                 * 
                                 * @param each the each
                                 */
                                public OpenBracketImpl(
                                                        Each each ) {
                                    this.each = each;
                                }

                                /**
                                 * {@inheritDoc}
                                 */
                                public Each each() {
                                    return each;
                                }
                            }
                        }

                        /**
                         * The Class CloseBracketImpl.
                         * 
                         * @author Vitor Hugo Chagas
                         */
                        public static class CloseBracketImpl implements CloseBracket {

                            /** The where statement. */
                            private WhereByNodeType whereStatement;

                            /** The outer each. */
                            private Each              outerEach;

                            /** The condition info. */
                            private SLConditionInfo   conditionInfo;

                            /**
                             * Instantiates a new close bracket impl.
                             * 
                             * @param whereStatement the where statement
                             * @param outerEach the outer each
                             * @param conditionInfo the condition info
                             */
                            public CloseBracketImpl(
                                                     WhereByNodeType whereStatement, Each outerEach,
                                                     SLConditionInfo conditionInfo ) {
                                this.whereStatement = whereStatement;
                                this.outerEach = outerEach;
                                this.conditionInfo = conditionInfo;
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public RelationalOperator or() {
                                SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                                outerStatementInfo.addCondition(ConditionalOperatorType.OR);
                                Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
                                return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public RelationalOperator and() {
                                SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
                                outerStatementInfo.addCondition(ConditionalOperatorType.AND);
                                Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
                                return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
                            }

                            /**
                             * {@inheritDoc}
                             */
                            public WhereByNodeType typeEnd() {
                                return whereStatement;
                            }
                        }
                    }
                }
            }
        }
    }
}
