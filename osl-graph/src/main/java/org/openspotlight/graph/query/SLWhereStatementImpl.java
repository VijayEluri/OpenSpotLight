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

import org.openspotlight.graph.SLGraphSessionException;
import org.openspotlight.graph.query.info.SLOrderByStatementInfo;
import org.openspotlight.graph.query.info.SLSelectStatementInfo;
import org.openspotlight.graph.query.info.SLWhereLinkTypeInfo;
import org.openspotlight.graph.query.info.SLWhereStatementInfo;
import org.openspotlight.graph.query.info.SLWhereTypeInfo;
import org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo;
import org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo.SLLinkTypeConditionInfo;
import org.openspotlight.graph.query.info.SLWhereTypeInfo.SLTypeStatementInfo;
import org.openspotlight.graph.query.info.SLWhereTypeInfo.SLTypeStatementInfo.SLTypeConditionInfo;

/**
 * The Class SLWhereStatementImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLWhereStatementImpl implements SLWhereStatement {

	/** The end. */
	private End end;
	
	/** The where statement info. */
	private SLWhereStatementInfo whereStatementInfo;

	/**
	 * Instantiates a new sL where statement impl.
	 * 
	 * @param selectFacade the select facade
	 * @param orderBy the order by
	 * @param whereStatementInfo the where statement info
	 */
	public SLWhereStatementImpl(SLSelectFacade selectFacade, SLOrderByStatement orderBy, SLWhereStatementInfo whereStatementInfo) {
		this(new EndImpl(selectFacade, whereStatementInfo, orderBy), whereStatementInfo);
	}

	/**
	 * Instantiates a new sL where statement impl.
	 * 
	 * @param end the end
	 * @param whereStatementInfo the where statement info
	 */
	public SLWhereStatementImpl(End end, SLWhereStatementInfo whereStatementInfo) {
		this.end = end;
		this.whereStatementInfo = whereStatementInfo;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereByNodeTypeInfoGetter#getWhereStatementInfo()
	 */
	/**
	 * Gets the where statement info.
	 * 
	 * @return the where statement info
	 */
	public SLWhereStatementInfo getWhereStatementInfo() {
		return whereStatementInfo;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereStatement#type(java.lang.String)
	 */
	public Type type(String typeName) {
		SLWhereTypeInfo typeInfo = new SLWhereTypeInfo(typeName);
		whereStatementInfo.getWhereTypeInfoList().add(typeInfo);
		return new TypeImpl(this, typeInfo);
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereStatement#linkType(java.lang.String)
	 */
	public LinkType linkType(String typeName) {
		SLWhereLinkTypeInfo linkTypeInfo = new SLWhereLinkTypeInfo(typeName);
		whereStatementInfo.getWhereLinkTypeInfoList().add(linkTypeInfo);
		return new LinkTypeImpl(this, linkTypeInfo);
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereStatement#whereEnd()
	 */
	public End whereEnd() {
		return end;
	}

	/**
	 * The Class EndImpl.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	
	public static class EndImpl implements End {
		
		/** The where statement info. */
		private SLWhereStatementInfo whereStatementInfo;
		
		/** The order by statement. */
		private SLOrderByStatement orderByStatement;
		
		/** The select facade. */
		private SLSelectFacade selectFacade;
		
		/**
		 * Instantiates a new end impl.
		 * 
		 * @param selectFacade the select facade
		 * @param whereStatementInfo the where statement info
		 * @param orderByStatement the order by statement
		 */
		public EndImpl(SLSelectFacade selectFacade, SLWhereStatementInfo whereStatementInfo, SLOrderByStatement orderByStatement) {
			this.selectFacade = selectFacade;
			this.whereStatementInfo = whereStatementInfo;
			this.orderByStatement = orderByStatement;
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.End#orderBy()
		 */
		public SLOrderByStatement orderBy() {
			if (orderByStatement == null) {
				SLSelectStatementInfo selectInfo = whereStatementInfo.getSelectStatementInfo();
				SLOrderByStatementInfo orderByStatementInfo = new SLOrderByStatementInfo(selectInfo);
				selectInfo.setOrderByStatementInfo(orderByStatementInfo);
				orderByStatement = new SLOrderByStatementImpl(selectFacade, orderByStatementInfo);
			}
			return orderByStatement;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.End#keepResult()
		 */
		public End keepResult() {
			whereStatementInfo.getSelectStatementInfo().setKeepResult(true);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.End#executeXTimes()
		 */
		public End executeXTimes() {
			whereStatementInfo.getSelectStatementInfo().setXTimes(0);
			return this;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.End#executeXTimes(int)
		 */
		public End executeXTimes(int x) {
			whereStatementInfo.getSelectStatementInfo().setXTimes(x);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.End#collator(int)
		 */
		public End collator(int strength) {
			whereStatementInfo.getSelectStatementInfo().setCollatorStrength(strength);
			return this;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectFacade#selectByLinkType()
		 */
		public SLSelectByLinkType selectByLinkType() throws SLGraphSessionException {
			return selectFacade.selectByLinkType();
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectFacade#selectByNodeType()
		 */
		public SLSelectByNodeType selectByNodeType() throws SLGraphSessionException {
			return selectFacade.selectByNodeType();
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectFacade#selectByLinkCount()
		 */
		public SLSelectByLinkCount selectByLinkCount() throws SLGraphSessionException {
			return selectFacade.selectByLinkCount();
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectFacade#select()
		 */
		public SLSelectStatement select() throws SLGraphSessionException {
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
		private SLWhereStatement whereStatement;
		
		/** The type info. */
		private SLWhereTypeInfo typeInfo;
		
		/**
		 * Instantiates a new type impl.
		 * 
		 * @param whereStatement the where statement
		 * @param typeInfo the type info
		 */
		public TypeImpl(SLWhereStatement whereStatement, SLWhereTypeInfo typeInfo) {
			this.whereStatement = whereStatement;
			this.typeInfo = typeInfo;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.Type#subTypes()
		 */
		public SubTypes subTypes() {
			typeInfo.setSubTypes(true);
			return new SubTypesImpl(whereStatement, typeInfo);
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereStatement.Type#each()
		 */
		public Each each() {
			SLTypeStatementInfo whereStatementInfo = new SLTypeStatementInfo(typeInfo, null);
			typeInfo.setTypeStatementInfo(whereStatementInfo);
			SLTypeConditionInfo conditionInfo = whereStatementInfo.addCondition();
			return new EachImpl(whereStatement, conditionInfo);
		}
		
		/**
		 * The Class SubTypesImpl.
		 * 
		 * @author Vitor Hugo Chagas
		 */
		public static class SubTypesImpl implements SubTypes {
			
			/** The where statement. */
			private SLWhereStatement whereStatement;
			
			/** The type info. */
			private SLWhereTypeInfo typeInfo;
			
			/**
			 * Instantiates a new sub types impl.
			 * 
			 * @param whereStatement the where statement
			 * @param typeInfo the type info
			 */
			public SubTypesImpl(SLWhereStatement whereStatement, SLWhereTypeInfo typeInfo) {
				this.whereStatement = whereStatement;
				this.typeInfo = typeInfo;
			}

			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.SLWhereStatement.Type.SubTypes#each()
			 */
			public Each each() {
				SLTypeStatementInfo whereStatementInfo = new SLTypeStatementInfo(typeInfo, null);
				typeInfo.setTypeStatementInfo(whereStatementInfo);
				SLTypeConditionInfo conditionInfo = whereStatementInfo.addCondition();
				return new EachImpl(whereStatement, conditionInfo);
			}
		}
		
		/**
		 * The Class EachImpl.
		 * 
		 * @author Vitor Hugo Chagas
		 */
		public static class EachImpl implements Each, OuterEachGetter<Each> {

			/** The where statement. */
			private SLWhereStatement whereStatement;
			
			/** The condition info. */
			private SLTypeConditionInfo conditionInfo;
			
			/** The outer each. */
			private Each outerEach;

			/**
			 * Instantiates a new each impl.
			 * 
			 * @param whereStatement the where statement
			 * @param conditionInfo the condition info
			 */
			public EachImpl(SLWhereStatement whereStatement, SLTypeConditionInfo conditionInfo) {
				this(whereStatement, conditionInfo, null);
			}
			
			/**
			 * Instantiates a new each impl.
			 * 
			 * @param whereStatement the where statement
			 * @param conditionInfo the condition info
			 * @param outerEach the outer each
			 */
			public EachImpl(SLWhereStatement whereStatement, SLTypeConditionInfo conditionInfo, Each outerEach) {
				this.whereStatement = whereStatement;
				this.conditionInfo = conditionInfo;
				this.outerEach = outerEach;
			}
			
			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.OuterEachGetter#getOuterEach()
			 */
			public Each getOuterEach() {
				return outerEach;
			}

			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each#property(java.lang.String)
			 */
			public Property property(String name) {
				conditionInfo.setPropertyName(name);
				return new PropertyImpl(whereStatement, this, outerEach, conditionInfo);
			}
			
			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each#property(java.lang.String)
			 */
			public Link link(String name) {
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
				private SLWhereStatement whereStatement;
				
				/** The each. */
				private Each each;
				
				/** The outer each. */
				private Each outerEach;
				
				/** The condition info. */
				private SLTypeConditionInfo conditionInfo;
				
				/**
				 * Instantiates a new link impl.
				 * 
				 * @param whereStatement the where statement
				 * @param each the each
				 * @param outerEach the outer each
				 * @param conditionInfo the condition info
				 */
				public LinkImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
					this.whereStatement = whereStatement;
					this.each = each;
					this.outerEach = outerEach;
					this.conditionInfo = conditionInfo;
				}

				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Link#a()
				 */
				public Side a() {
					conditionInfo.setSide(SLSideType.A_SIDE);
					return new SideImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Link#b()
				 */
				public Side b() {
					conditionInfo.setSide(SLSideType.B_SIDE);
					return new SideImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/**
				 * The Class SideImpl.
				 * 
				 * @author Vitor Hugo Chagas
				 */
				public static class SideImpl implements Side {

					/** The where statement. */
					private SLWhereStatement whereStatement;
					
					/** The each. */
					private Each each;
					
					/** The outer each. */
					private Each outerEach;
					
					/** The condition info. */
					private SLTypeConditionInfo conditionInfo;
					
					/**
					 * Instantiates a new side impl.
					 * 
					 * @param whereStatement the where statement
					 * @param each the each
					 * @param outerEach the outer each
					 * @param conditionInfo the condition info
					 */
					public SideImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
						this.whereStatement = whereStatement;
						this.each = each;
						this.outerEach = outerEach;
						this.conditionInfo = conditionInfo;
					}

					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Link.Side#count()
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
						private Each each;
						
						/** The outer each. */
						private Each outerEach;
						
						/** The where statement. */
						private SLWhereStatement whereStatement;
						
						/** The condition info. */
						private SLTypeConditionInfo conditionInfo;

						/**
						 * Instantiates a new count impl.
						 * 
						 * @param whereStatement the where statement
						 * @param each the each
						 * @param outerEach the outer each
						 * @param conditionInfo the condition info
						 */
						public CountImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
							this.each = each;
							this.whereStatement = whereStatement;
							this.conditionInfo = conditionInfo;
							this.outerEach = outerEach;
						}

						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#not()
						 */
						public Count not() {
							conditionInfo.setRelationalNotOperator(true);
							return this;
						}

						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#lesserThan()
						 */
						public Operator lesserThan() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_THAN);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#greaterThan()
						 */
						public Operator greaterThan() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_THAN);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#equalsTo()
						 */
						public Operator equalsTo() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.EQUAL);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#lesserOrEqualThan()
						 */
						public Operator lesserOrEqualThan() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_OR_EQUAL_THAN);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#greaterOrEqualThan()
						 */
						public Operator greaterOrEqualThan() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_OR_EQUAL_THAN);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#contains()
						 */
						/**
						 * Contains.
						 * 
						 * @return the operator
						 */
						public Operator contains() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.CONTAINS);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#startsWith()
						 */
						/**
						 * Starts with.
						 * 
						 * @return the operator
						 */
						public Operator startsWith() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.STARTS_WITH);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#endsWith()
						 */
						/**
						 * Ends with.
						 * 
						 * @return the operator
						 */
						public Operator endsWith() {
							conditionInfo.setRelationalOperator(SLRelationalOperatorType.ENDS_WITH);
							return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
						}
						
						/**
						 * The Class OperatorImpl.
						 * 
						 * @author Vitor Hugo Chagas
						 */
						public static class OperatorImpl implements Operator {

							/** The each. */
							private Each each;
							
							/** The outer each. */
							private Each outerEach;
							
							/** The where statement. */
							private SLWhereStatement whereStatement;
							
							/** The condition info. */
							private SLTypeConditionInfo conditionInfo;
							
							/**
							 * Instantiates a new operator impl.
							 * 
							 * @param whereStatement the where statement
							 * @param each the each
							 * @param outerEach the outer each
							 * @param conditionInfo the condition info
							 */
							public OperatorImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
								this.each = each;
								this.outerEach = outerEach;
								this.whereStatement = whereStatement;
								this.conditionInfo = conditionInfo;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.Integer)
							 */
							public Value value(Integer value) {
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
								private Each each;
								
								/** The outer each. */
								private Each outerEach;
								
								/** The where statement. */
								private SLWhereStatement whereStatement;
								
								/** The condition info. */
								private SLTypeConditionInfo conditionInfo;

								/**
								 * Instantiates a new value impl.
								 * 
								 * @param whereStatement the where statement
								 * @param each the each
								 * @param outerEach the outer each
								 * @param conditionInfo the condition info
								 */
								public ValueImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
									this.each = each;
									this.outerEach = outerEach;
									this.whereStatement = whereStatement;
									this.conditionInfo = conditionInfo;
								}
								
								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#typeEnd()
								 */
								public SLWhereStatement typeEnd() {
									conditionInfo.getTypeInfo().getTypeStatementInfo().setClosed(true);
									return whereStatement;
								}
								
								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#or()
								 */
								public RelationalOperator or() {
									SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
									SLTypeConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
									Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
									return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
								}
								
								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#and()
								 */
								public RelationalOperator and() {
									SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
									SLTypeConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
									Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
									return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
								}
								
								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#closeBracket()
								 */
								public CloseBracket closeBracket() {
									SLTypeStatementInfo statementInfo = conditionInfo.getOuterStatementInfo();
									statementInfo.setClosed(true);
									return new CloseBracketImpl(whereStatement, outerEach, conditionInfo);
								}
								
								/**
								 * The Class RelationalOperatorImpl.
								 * 
								 * @author Vitor Hugo Chagas
								 */
								public static class RelationalOperatorImpl implements RelationalOperator {

									/** The each. */
									private Each each;
									
									/** The where statement. */
									private SLWhereStatement whereStatement;
									
									/** The condition info. */
									private SLTypeConditionInfo conditionInfo;

									/**
									 * Instantiates a new relational operator impl.
									 * 
									 * @param whereStatement the where statement
									 * @param each the each
									 * @param conditionInfo the condition info
									 */
									public RelationalOperatorImpl(SLWhereStatement whereStatement, Each each, SLTypeConditionInfo conditionInfo) {
										this.each = each;
										this.whereStatement = whereStatement;
										this.conditionInfo = conditionInfo;
									}
									
									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#not()
									 */
									public RelationalOperator not() {
										conditionInfo.setConditionalNotOperator(true);
										return this;
									}

									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#comma()
									 */
									public SLWhereStatement comma() {
										return this.whereStatement;
									}
									
									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#each()
									 */
									public Each each() {
										return this.each;
									}

									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#openBracket()
									 */
									public OpenBracket openBracket() {
										SLTypeStatementInfo newStatementInfo = new SLTypeStatementInfo(conditionInfo.getTypeInfo(), conditionInfo.getOuterStatementInfo());
										conditionInfo.setInnerStatementInfo(newStatementInfo);
										SLTypeConditionInfo newConditionInfo = newStatementInfo.addCondition();
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
										public OpenBracketImpl(Each each) {
											this.each = each;
										}

										/* (non-Javadoc)
										 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator.OpenBracket#each()
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
									private SLWhereStatement whereStatement;
									
									/** The outer each. */
									private Each outerEach;
									
									/** The condition info. */
									private SLTypeConditionInfo conditionInfo;
									
									/**
									 * Instantiates a new close bracket impl.
									 * 
									 * @param whereStatement the where statement
									 * @param outerEach the outer each
									 * @param conditionInfo the condition info
									 */
									public CloseBracketImpl(SLWhereStatement whereStatement, Each outerEach, SLTypeConditionInfo conditionInfo) {
										this.whereStatement = whereStatement;
										this.outerEach = outerEach;
										this.conditionInfo = conditionInfo;
									}
									
									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Link.Side.Count.Operator.Value.CloseBracket#closeBracket()
									 */
									@SuppressWarnings("unchecked")
									public CloseBracket closeBracket() {
										int size = conditionInfo.getOuterStatementInfo().getConditionInfoList().size();
										SLTypeConditionInfo outerConditionInfo = conditionInfo.getOuterStatementInfo().getConditionInfoList().get(size - 1);
										SLTypeStatementInfo outerStatementInfo = outerConditionInfo.getOuterStatementInfo().getOuterStatementInfo();
										outerStatementInfo.setClosed(true);
										OuterEachGetter<Each> getter = OuterEachGetter.class.cast(outerEach);
										return new CloseBracketImpl(whereStatement, getter.getOuterEach(), outerConditionInfo);
									}
									
									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.CloseBracket#or()
									 */
									public RelationalOperator or() {
										SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
										outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
										Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
										return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
									}
									
									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.CloseBracket#and()
									 */
									public RelationalOperator and() {
										SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
										outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
										Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
										return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
									}
									
									/* (non-Javadoc)
									 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.CloseBracket#typeEnd()
									 */
									public SLWhereStatement typeEnd() {
										conditionInfo.getTypeInfo().getTypeStatementInfo().setClosed(true);
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
				private Each each;
				
				/** The outer each. */
				private Each outerEach;
				
				/** The where statement. */
				private SLWhereStatement whereStatement;
				
				/** The condition info. */
				private SLTypeConditionInfo conditionInfo;

				/**
				 * Instantiates a new property impl.
				 * 
				 * @param whereStatement the where statement
				 * @param each the each
				 * @param outerEach the outer each
				 * @param conditionInfo the condition info
				 */
				public PropertyImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
					this.each = each;
					this.whereStatement = whereStatement;
					this.conditionInfo = conditionInfo;
					this.outerEach = outerEach;
				}

				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#not()
				 */
				public Property not() {
					conditionInfo.setRelationalNotOperator(true);
					return this;
				}

				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#lesserThan()
				 */
				public Operator lesserThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#greaterThan()
				 */
				public Operator greaterThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#equalsTo()
				 */
				public Operator equalsTo() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.EQUAL);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#lesserOrEqualThan()
				 */
				public Operator lesserOrEqualThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_OR_EQUAL_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#greaterOrEqualThan()
				 */
				public Operator greaterOrEqualThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_OR_EQUAL_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#contains()
				 */
				public Operator contains() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.CONTAINS);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#startsWith()
				 */
				public Operator startsWith() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.STARTS_WITH);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property#endsWith()
				 */
				public Operator endsWith() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.ENDS_WITH);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/**
				 * The Class OperatorImpl.
				 * 
				 * @author Vitor Hugo Chagas
				 */
				public static class OperatorImpl implements Operator {

					/** The each. */
					private Each each;
					
					/** The outer each. */
					private Each outerEach;
					
					/** The where statement. */
					private SLWhereStatement whereStatement;
					
					/** The condition info. */
					private SLTypeConditionInfo conditionInfo;
					
					/**
					 * Instantiates a new operator impl.
					 * 
					 * @param whereStatement the where statement
					 * @param each the each
					 * @param outerEach the outer each
					 * @param conditionInfo the condition info
					 */
					public OperatorImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
						this.each = each;
						this.outerEach = outerEach;
						this.whereStatement = whereStatement;
						this.conditionInfo = conditionInfo;
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.String)
					 */
					public Value value(String value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.Integer)
					 */
					public Value value(Integer value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.Long)
					 */
					public Value value(Long value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.Float)
					 */
					public Value value(Float value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.Double)
					 */
					public Value value(Double value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator#value(java.lang.Boolean)
					 */
					public Value value(Boolean value) {
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
						private Each each;
						
						/** The outer each. */
						private Each outerEach;
						
						/** The where statement. */
						private SLWhereStatement whereStatement;
						
						/** The condition info. */
						private SLTypeConditionInfo conditionInfo;

						/**
						 * Instantiates a new value impl.
						 * 
						 * @param whereStatement the where statement
						 * @param each the each
						 * @param outerEach the outer each
						 * @param conditionInfo the condition info
						 */
						public ValueImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLTypeConditionInfo conditionInfo) {
							this.each = each;
							this.outerEach = outerEach;
							this.whereStatement = whereStatement;
							this.conditionInfo = conditionInfo;
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#typeEnd()
						 */
						public SLWhereStatement typeEnd() {
							conditionInfo.getTypeInfo().getTypeStatementInfo().setClosed(true);
							return whereStatement;
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#or()
						 */
						public RelationalOperator or() {
							SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
							SLTypeConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
							Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
							return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#and()
						 */
						public RelationalOperator and() {
							SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
							SLTypeConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
							Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
							return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value#closeBracket()
						 */
						public CloseBracket closeBracket() {
							SLTypeStatementInfo statementInfo = conditionInfo.getOuterStatementInfo();
							statementInfo.setClosed(true);
							return new CloseBracketImpl(whereStatement, outerEach, conditionInfo);
						}
						
						/**
						 * The Class RelationalOperatorImpl.
						 * 
						 * @author Vitor Hugo Chagas
						 */
						public static class RelationalOperatorImpl implements RelationalOperator {

							/** The each. */
							private Each each;
							
							/** The where statement. */
							private SLWhereStatement whereStatement;
							
							/** The condition info. */
							private SLTypeConditionInfo conditionInfo;

							/**
							 * Instantiates a new relational operator impl.
							 * 
							 * @param whereStatement the where statement
							 * @param each the each
							 * @param conditionInfo the condition info
							 */
							public RelationalOperatorImpl(SLWhereStatement whereStatement, Each each, SLTypeConditionInfo conditionInfo) {
								this.each = each;
								this.whereStatement = whereStatement;
								this.conditionInfo = conditionInfo;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#not()
							 */
							public RelationalOperator not() {
								conditionInfo.setConditionalNotOperator(true);
								return this;
							}

							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#comma()
							 */
							public SLWhereStatement comma() {
								return this.whereStatement;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#each()
							 */
							public Each each() {
								return this.each;
							}

							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator#openBracket()
							 */
							public OpenBracket openBracket() {
								SLTypeStatementInfo newStatementInfo = new SLTypeStatementInfo(conditionInfo.getTypeInfo(), conditionInfo.getOuterStatementInfo());
								conditionInfo.setInnerStatementInfo(newStatementInfo);
								SLTypeConditionInfo newConditionInfo = newStatementInfo.addCondition();
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
								public OpenBracketImpl(Each each) {
									this.each = each;
								}

								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.RelationalOperator.OpenBracket#each()
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
							private SLWhereStatement whereStatement;
							
							/** The outer each. */
							private Each outerEach;
							
							/** The condition info. */
							private SLTypeConditionInfo conditionInfo;
							
							/**
							 * Instantiates a new close bracket impl.
							 * 
							 * @param whereStatement the where statement
							 * @param outerEach the outer each
							 * @param conditionInfo the condition info
							 */
							public CloseBracketImpl(SLWhereStatement whereStatement, Each outerEach, SLTypeConditionInfo conditionInfo) {
								this.whereStatement = whereStatement;
								this.outerEach = outerEach;
								this.conditionInfo = conditionInfo;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Link.Side.Count.Operator.Value.CloseBracket#closeBracket()
							 */
							@SuppressWarnings("unchecked")
							public CloseBracket closeBracket() {
								int size = conditionInfo.getOuterStatementInfo().getConditionInfoList().size();
								SLTypeConditionInfo outerConditionInfo = conditionInfo.getOuterStatementInfo().getConditionInfoList().get(size - 1);
								SLTypeStatementInfo outerStatementInfo = outerConditionInfo.getOuterStatementInfo().getOuterStatementInfo();
								outerStatementInfo.setClosed(true);
								OuterEachGetter<Each> getter = OuterEachGetter.class.cast(outerEach);
								return new CloseBracketImpl(whereStatement, getter.getOuterEach(), outerConditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.CloseBracket#or()
							 */
							public RelationalOperator or() {
								SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
								outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
								Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
								return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.CloseBracket#and()
							 */
							public RelationalOperator and() {
								SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
								outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
								Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
								return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.Type.Each.Property.Operator.Value.CloseBracket#typeEnd()
							 */
							public SLWhereStatement typeEnd() {
								conditionInfo.getTypeInfo().getTypeStatementInfo().setClosed(true);
								return whereStatement;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * The Class LinkTypeImpl.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static class LinkTypeImpl implements LinkType {
		
		/** The where statement. */
		private SLWhereStatement whereStatement;
		
		/** The type info. */
		private SLWhereLinkTypeInfo typeInfo;
		
		/**
		 * Instantiates a new link type impl.
		 * 
		 * @param whereStatement the where statement
		 * @param typeInfo the type info
		 */
		public LinkTypeImpl(SLWhereStatement whereStatement, SLWhereLinkTypeInfo typeInfo) {
			this.whereStatement = whereStatement;
			this.typeInfo = typeInfo;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType#each()
		 */
		public Each each() {
			SLLinkTypeStatementInfo whereStatementInfo = new SLLinkTypeStatementInfo(typeInfo);
			typeInfo.setLinkTypeStatementInfo(whereStatementInfo);
			SLLinkTypeConditionInfo conditionInfo = whereStatementInfo.addCondition();
			return new EachImpl(whereStatement, conditionInfo);
		}
		
		/**
		 * The Class EachImpl.
		 * 
		 * @author Vitor Hugo Chagas
		 */
		public static class EachImpl implements Each {

			/** The where statement. */
			private SLWhereStatement whereStatement;
			
			/** The condition info. */
			private SLLinkTypeConditionInfo conditionInfo;
			
			/** The outer each. */
			private Each outerEach;

			/**
			 * Instantiates a new each impl.
			 * 
			 * @param whereStatement the where statement
			 * @param conditionInfo the condition info
			 */
			public EachImpl(SLWhereStatement whereStatement, SLLinkTypeConditionInfo conditionInfo) {
				this(whereStatement, conditionInfo, null);
			}
			
			/**
			 * Instantiates a new each impl.
			 * 
			 * @param whereStatement the where statement
			 * @param conditionInfo the condition info
			 * @param outerEach the outer each
			 */
			public EachImpl(SLWhereStatement whereStatement, org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo.SLLinkTypeConditionInfo conditionInfo, Each outerEach) {
				this.whereStatement = whereStatement;
				this.conditionInfo = conditionInfo;
				this.outerEach = outerEach;
			}

			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each#property(java.lang.String)
			 */
			public Property property(String name) {
				conditionInfo.setPropertyName(name);
				return new PropertyImpl(whereStatement, this, outerEach, conditionInfo);
			}
			
			/**
			 * The Class PropertyImpl.
			 * 
			 * @author Vitor Hugo Chagas
			 */
			public static class PropertyImpl implements Property {

				/** The each. */
				private Each each;
				
				/** The outer each. */
				private Each outerEach;
				
				/** The where statement. */
				private SLWhereStatement whereStatement;
				
				/** The condition info. */
				private org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo.SLLinkTypeConditionInfo conditionInfo;

				/**
				 * Instantiates a new property impl.
				 * 
				 * @param whereStatement the where statement
				 * @param each the each
				 * @param outerEach the outer each
				 * @param conditionInfo the condition info
				 */
				public PropertyImpl(SLWhereStatement whereStatement, Each each, Each outerEach, org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo.SLLinkTypeConditionInfo conditionInfo) {
					this.each = each;
					this.whereStatement = whereStatement;
					this.conditionInfo = conditionInfo;
					this.outerEach = outerEach;
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#not()
				 */
				public Property not() {
					conditionInfo.setRelationalNotOperator(true);
					return this;
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#lesserThan()
				 */
				public Operator lesserThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#greaterThan()
				 */
				public Operator greaterThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#equalsTo()
				 */
				public Operator equalsTo() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.EQUAL);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#lesserOrEqualThan()
				 */
				public Operator lesserOrEqualThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_OR_EQUAL_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#greaterOrEqualThan()
				 */
				public Operator greaterOrEqualThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_OR_EQUAL_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#contains()
				 */
				public Operator contains() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.CONTAINS);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#startsWith()
				 */
				public Operator startsWith() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.STARTS_WITH);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property#endsWith()
				 */
				public Operator endsWith() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.ENDS_WITH);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/**
				 * The Class OperatorImpl.
				 * 
				 * @author Vitor Hugo Chagas
				 */
				public static class OperatorImpl implements Operator {

					/** The each. */
					private Each each;
					
					/** The outer each. */
					private Each outerEach;
					
					/** The where statement. */
					private SLWhereStatement whereStatement;
					
					/** The condition info. */
					private org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo.SLLinkTypeConditionInfo conditionInfo;
					
					/**
					 * Instantiates a new operator impl.
					 * 
					 * @param whereStatement the where statement
					 * @param each the each
					 * @param outerEach the outer each
					 * @param conditionInfo the condition info
					 */
					public OperatorImpl(SLWhereStatement whereStatement, Each each, Each outerEach, org.openspotlight.graph.query.info.SLWhereLinkTypeInfo.SLLinkTypeStatementInfo.SLLinkTypeConditionInfo conditionInfo) {
						this.each = each;
						this.outerEach = outerEach;
						this.whereStatement = whereStatement;
						this.conditionInfo = conditionInfo;
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator#value(java.lang.String)
					 */
					public Value value(String value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator#value(java.lang.Integer)
					 */
					public Value value(Integer value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator#value(java.lang.Long)
					 */
					public Value value(Long value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator#value(java.lang.Float)
					 */
					public Value value(Float value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator#value(java.lang.Double)
					 */
					public Value value(Double value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator#value(java.lang.Boolean)
					 */
					public Value value(Boolean value) {
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
						private Each each;
						
						/** The outer each. */
						private Each outerEach;
						
						/** The where statement. */
						private SLWhereStatement whereStatement;
						
						/** The condition info. */
						private SLLinkTypeConditionInfo conditionInfo;

						/**
						 * Instantiates a new value impl.
						 * 
						 * @param whereStatement the where statement
						 * @param each the each
						 * @param outerEach the outer each
						 * @param conditionInfo the condition info
						 */
						public ValueImpl(SLWhereStatement whereStatement, Each each, Each outerEach, SLLinkTypeConditionInfo conditionInfo) {
							this.each = each;
							this.outerEach = outerEach;
							this.whereStatement = whereStatement;
							this.conditionInfo = conditionInfo;
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value#linkTypeEnd()
						 */
						public SLWhereStatement linkTypeEnd() {
							conditionInfo.getLinkTypeInfo().getLinkTypeStatementInfo().setClosed(true);
							return whereStatement;
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value#or()
						 */
						public RelationalOperator or() {
							SLLinkTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
							SLLinkTypeConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
							Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
							return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value#and()
						 */
						public RelationalOperator and() {
							SLLinkTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
							SLLinkTypeConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
							Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
							return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value#closeBracket()
						 */
						public CloseBracket closeBracket() {
							conditionInfo.getOuterStatementInfo().setClosed(true);
							return new CloseBracketImpl(whereStatement, outerEach, conditionInfo);
						}
						
						/**
						 * The Class RelationalOperatorImpl.
						 * 
						 * @author Vitor Hugo Chagas
						 */
						public static class RelationalOperatorImpl implements RelationalOperator {

							/** The each. */
							private Each each;
							
							/** The where statement. */
							private SLWhereStatement whereStatement;
							
							/** The condition info. */
							private SLLinkTypeConditionInfo conditionInfo;

							/**
							 * Instantiates a new relational operator impl.
							 * 
							 * @param whereStatement the where statement
							 * @param each the each
							 * @param conditionInfo the condition info
							 */
							public RelationalOperatorImpl(SLWhereStatement whereStatement, Each each, SLLinkTypeConditionInfo conditionInfo) {
								this.each = each;
								this.whereStatement = whereStatement;
								this.conditionInfo = conditionInfo;
							}

							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value.RelationalOperator#comma()
							 */
							public SLWhereStatement comma() {
								return this.whereStatement;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value.RelationalOperator#each()
							 */
							public Each each() {
								return this.each;
							}

							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value.RelationalOperator#openBracket()
							 */
							public OpenBracket openBracket() {
								SLLinkTypeStatementInfo newStatementInfo = new SLLinkTypeStatementInfo(conditionInfo.getLinkTypeInfo());
								conditionInfo.setInnerStatementInfo(newStatementInfo);
								SLLinkTypeConditionInfo newConditionInfo = newStatementInfo.addCondition();
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
								public OpenBracketImpl(Each each) {
									this.each = each;
								}

								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value.RelationalOperator.OpenBracket#each()
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
							private SLWhereStatement whereStatement;
							
							/** The outer each. */
							private Each outerEach;
							
							/** The condition info. */
							private SLLinkTypeConditionInfo conditionInfo;
							
							/**
							 * Instantiates a new close bracket impl.
							 * 
							 * @param whereStatement the where statement
							 * @param outerEach the outer each
							 * @param conditionInfo the condition info
							 */
							public CloseBracketImpl(SLWhereStatement whereStatement, Each outerEach, SLLinkTypeConditionInfo conditionInfo) {
								this.whereStatement = whereStatement;
								this.outerEach = outerEach;
								this.conditionInfo = conditionInfo;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.LinkType.Each.Property.Operator.Value.CloseBracket#closeBracket()
							 */
							public CloseBracket closeBracket() {
								conditionInfo.getOuterStatementInfo().setClosed(true);
								return new CloseBracketImpl(whereStatement, outerEach, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value.CloseBracket#or()
							 */
							public RelationalOperator or() {
								SLLinkTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
								outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
								Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
								return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByLinkType.LinkType.Each.Property.Operator.Value.CloseBracket#and()
							 */
							public RelationalOperator and() {
								SLLinkTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
								outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
								Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
								return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereStatement.LinkType.Each.Property.Operator.Value.CloseBracket#linkTypeEnd()
							 */
							public SLWhereStatement linkTypeEnd() {
								conditionInfo.getLinkTypeInfo().getLinkTypeStatementInfo().setClosed(true);
								return whereStatement;
							}
						}
					}
				}
			}
		}
	}

}

interface OuterEachGetter<E> {
	
	public E getOuterEach();
}