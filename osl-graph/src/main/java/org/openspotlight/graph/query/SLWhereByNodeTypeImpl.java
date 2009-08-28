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

import org.openspotlight.graph.query.info.SLWhereByNodeTypeInfo;
import org.openspotlight.graph.query.info.SLWhereByNodeTypeInfo.SLWhereTypeInfo;
import org.openspotlight.graph.query.info.SLWhereByNodeTypeInfo.SLWhereTypeInfo.SLTypeStatementInfo;
import org.openspotlight.graph.query.info.SLWhereByNodeTypeInfo.SLWhereTypeInfo.SLTypeStatementInfo.SLConditionInfo;

/**
 * The Class SLWhereByNodeTypeImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLWhereByNodeTypeImpl implements SLWhereByNodeType, SLWhereByNodeTypeInfoGetter {
	
	/** The end. */
	private End end;
	
	/** The where by node type info. */
	private SLWhereByNodeTypeInfo whereByNodeTypeInfo;
	
	/**
	 * Instantiates a new sL where by node type impl.
	 * 
	 * @param orderBy the order by
	 * @param whereByNodeTypeInfo the where by node type info
	 */
	public SLWhereByNodeTypeImpl(SLOrderByStatement orderBy, SLWhereByNodeTypeInfo whereByNodeTypeInfo) {
		this(new EndImpl(whereByNodeTypeInfo, orderBy), whereByNodeTypeInfo);
	}

	/**
	 * Instantiates a new sL where by node type impl.
	 * 
	 * @param end the end
	 * @param whereByNodeTypeInfo the where by node type info
	 */
	public SLWhereByNodeTypeImpl(End end, SLWhereByNodeTypeInfo whereByNodeTypeInfo) {
		this.end = end;
		this.whereByNodeTypeInfo = whereByNodeTypeInfo;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereByNodeTypeInfoGetter#getWhereStatementInfo()
	 */
	public SLWhereByNodeTypeInfo getWhereStatementInfo() {
		return whereByNodeTypeInfo;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereByNodeType#type(java.lang.String)
	 */
	public Type type(String typeName) {
		SLWhereTypeInfo typeInfo = new SLWhereTypeInfo(typeName);
		whereByNodeTypeInfo.getWhereTypeInfoList().add(typeInfo);
		return new TypeImpl(this, typeInfo);
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLWhereByNodeType#whereEnd()
	 */
	public End whereEnd() {
		return end;
	}

	/**
	 * private void verifyConditionalOperator() {
	 * if (statementInfo.getConditionInfoList().isEmpty()) {
	 * throw new SLInvalidQuerySyntaxRuntimeException("the first condition of a statement must not start with AND or OR operators");
	 * }
	 * }
	 */
	
	public static class EndImpl implements End {
		
		/** The where by node type info. */
		private SLWhereByNodeTypeInfo whereByNodeTypeInfo;
		
		/** The order by statement. */
		private SLOrderByStatement orderByStatement;
		
		/**
		 * Instantiates a new end impl.
		 * 
		 * @param whereByNodeTypeInfo the where by node type info
		 * @param orderByStatement the order by statement
		 */
		public EndImpl(SLWhereByNodeTypeInfo whereByNodeTypeInfo, SLOrderByStatement orderByStatement) {
			this.whereByNodeTypeInfo = whereByNodeTypeInfo;
			this.orderByStatement = orderByStatement;
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByNodeType.End#orderBy()
		 */
		public SLOrderByStatement orderBy() {
			return orderByStatement;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByNodeType.End#keepResult()
		 */
		public End keepResult() {
			whereByNodeTypeInfo.getSelectByNodeTypeInfo().setKeepResult(true);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByNodeType.End#executeXTimes()
		 */
		public End executeXTimes() {
			whereByNodeTypeInfo.getSelectByNodeTypeInfo().setXTimes(0);
			return this;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByNodeType.End#executeXTimes(int)
		 */
		public End executeXTimes(int x) {
			whereByNodeTypeInfo.getSelectByNodeTypeInfo().setXTimes(x);
			return this;
		}
	}

	/**
	 * The Class TypeImpl.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static class TypeImpl implements Type {
		
		/** The where statement. */
		private SLWhereByNodeType whereStatement;
		
		/** The type info. */
		private SLWhereTypeInfo typeInfo;
		
		/**
		 * Instantiates a new type impl.
		 * 
		 * @param whereStatement the where statement
		 * @param typeInfo the type info
		 */
		public TypeImpl(SLWhereByNodeType whereStatement, SLWhereTypeInfo typeInfo) {
			this.whereStatement = whereStatement;
			this.typeInfo = typeInfo;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type#subTypes()
		 */
		public SubTypes subTypes() {
			typeInfo.setSubTypes(true);
			return new SubTypesImpl(whereStatement, typeInfo);
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type#each()
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
			private SLWhereByNodeType whereStatement;
			
			/** The type info. */
			private SLWhereTypeInfo typeInfo;
			
			/**
			 * Instantiates a new sub types impl.
			 * 
			 * @param whereStatement the where statement
			 * @param typeInfo the type info
			 */
			public SubTypesImpl(SLWhereByNodeType whereStatement, SLWhereTypeInfo typeInfo) {
				this.whereStatement = whereStatement;
				this.typeInfo = typeInfo;
			}

			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.SubTypes#each()
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
			private SLWhereByNodeType whereStatement;
			
			/** The condition info. */
			private SLConditionInfo conditionInfo;
			
			/** The outer each. */
			private Each outerEach;

			/**
			 * Instantiates a new each impl.
			 * 
			 * @param whereStatement the where statement
			 * @param conditionInfo the condition info
			 */
			public EachImpl(SLWhereByNodeType whereStatement, SLConditionInfo conditionInfo) {
				this(whereStatement, conditionInfo, null);
			}
			
			/**
			 * Instantiates a new each impl.
			 * 
			 * @param whereStatement the where statement
			 * @param conditionInfo the condition info
			 * @param outerEach the outer each
			 */
			public EachImpl(SLWhereByNodeType whereStatement, SLConditionInfo conditionInfo, Each outerEach) {
				this.whereStatement = whereStatement;
				this.conditionInfo = conditionInfo;
				this.outerEach = outerEach;
			}

			/* (non-Javadoc)
			 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each#property(java.lang.String)
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
				private SLWhereByNodeType whereStatement;
				
				/** The condition info. */
				private SLConditionInfo conditionInfo;

				/**
				 * Instantiates a new property impl.
				 * 
				 * @param whereStatement the where statement
				 * @param each the each
				 * @param outerEach the outer each
				 * @param conditionInfo the condition info
				 */
				public PropertyImpl(SLWhereByNodeType whereStatement, Each each, Each outerEach, SLConditionInfo conditionInfo) {
					this.each = each;
					this.whereStatement = whereStatement;
					this.conditionInfo = conditionInfo;
					this.outerEach = outerEach;
				}

				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#not()
				 */
				public Property not() {
					conditionInfo.setRelationalNotOperator(true);
					return this;
				}

				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#lesserThan()
				 */
				public Operator lesserThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#greaterThan()
				 */
				public Operator greaterThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#equalsTo()
				 */
				public Operator equalsTo() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.EQUAL);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#lesserOrEqualThan()
				 */
				public Operator lesserOrEqualThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.LESSER_OR_EQUAL_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#greaterOrEqualThan()
				 */
				public Operator greaterOrEqualThan() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.GREATER_OR_EQUAL_THAN);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#contains()
				 */
				public Operator contains() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.CONTAINS);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#startsWith()
				 */
				public Operator startsWith() {
					conditionInfo.setRelationalOperator(SLRelationalOperatorType.STARTS_WITH);
					return new OperatorImpl(whereStatement, each, outerEach, conditionInfo);
				}
				
				/* (non-Javadoc)
				 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property#endsWith()
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
					private SLWhereByNodeType whereStatement;
					
					/** The condition info. */
					private SLConditionInfo conditionInfo;
					
					/**
					 * Instantiates a new operator impl.
					 * 
					 * @param whereStatement the where statement
					 * @param each the each
					 * @param outerEach the outer each
					 * @param conditionInfo the condition info
					 */
					public OperatorImpl(SLWhereByNodeType whereStatement, Each each, Each outerEach, SLConditionInfo conditionInfo) {
						this.each = each;
						this.outerEach = outerEach;
						this.whereStatement = whereStatement;
						this.conditionInfo = conditionInfo;
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator#value(java.lang.String)
					 */
					public Value value(String value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator#value(java.lang.Integer)
					 */
					public Value value(Integer value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator#value(java.lang.Long)
					 */
					public Value value(Long value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator#value(java.lang.Float)
					 */
					public Value value(Float value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator#value(java.lang.Double)
					 */
					public Value value(Double value) {
						conditionInfo.setValue(value);
						return new ValueImpl(whereStatement, each, outerEach, conditionInfo);
					}
					
					/* (non-Javadoc)
					 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator#value(java.lang.Boolean)
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
						private SLWhereByNodeType whereStatement;
						
						/** The condition info. */
						private SLConditionInfo conditionInfo;

						/**
						 * Instantiates a new value impl.
						 * 
						 * @param whereStatement the where statement
						 * @param each the each
						 * @param outerEach the outer each
						 * @param conditionInfo the condition info
						 */
						public ValueImpl(SLWhereByNodeType whereStatement, Each each, Each outerEach, SLConditionInfo conditionInfo) {
							this.each = each;
							this.outerEach = outerEach;
							this.whereStatement = whereStatement;
							this.conditionInfo = conditionInfo;
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value#typeEnd()
						 */
						public SLWhereByNodeType typeEnd() {
							return whereStatement;
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value#or()
						 */
						public RelationalOperator or() {
							SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
							SLConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
							Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
							return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value#and()
						 */
						public RelationalOperator and() {
							SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
							SLConditionInfo newConditionInfo = outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
							Each newEach = new EachImpl(whereStatement, newConditionInfo, this.each);
							return new RelationalOperatorImpl(whereStatement, newEach, newConditionInfo);
						}
						
						/* (non-Javadoc)
						 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value#closeBracket()
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
							private Each each;
							
							/** The where statement. */
							private SLWhereByNodeType whereStatement;
							
							/** The condition info. */
							private SLConditionInfo conditionInfo;

							/**
							 * Instantiates a new relational operator impl.
							 * 
							 * @param whereStatement the where statement
							 * @param each the each
							 * @param conditionInfo the condition info
							 */
							public RelationalOperatorImpl(SLWhereByNodeType whereStatement, Each each, SLConditionInfo conditionInfo) {
								this.each = each;
								this.whereStatement = whereStatement;
								this.conditionInfo = conditionInfo;
							}

							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.RelationalOperator#comma()
							 */
							public SLWhereByNodeType comma() {
								return this.whereStatement;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.RelationalOperator#each()
							 */
							public Each each() {
								return this.each;
							}

							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.RelationalOperator#openBracket()
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
								public OpenBracketImpl(Each each) {
									this.each = each;
								}

								/* (non-Javadoc)
								 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.RelationalOperator.OpenBracket#each()
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
							private SLWhereByNodeType whereStatement;
							
							/** The outer each. */
							private Each outerEach;
							
							/** The condition info. */
							private SLConditionInfo conditionInfo;
							
							/**
							 * Instantiates a new close bracket impl.
							 * 
							 * @param whereStatement the where statement
							 * @param outerEach the outer each
							 * @param conditionInfo the condition info
							 */
							public CloseBracketImpl(SLWhereByNodeType whereStatement, Each outerEach, SLConditionInfo conditionInfo) {
								this.whereStatement = whereStatement;
								this.outerEach = outerEach;
								this.conditionInfo = conditionInfo;
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.CloseBracket#or()
							 */
							public RelationalOperator or() {
								SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
								outerStatementInfo.addCondition(SLConditionalOperatorType.OR);
								Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
								return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.CloseBracket#and()
							 */
							public RelationalOperator and() {
								SLTypeStatementInfo outerStatementInfo = conditionInfo.getOuterStatementInfo();
								outerStatementInfo.addCondition(SLConditionalOperatorType.AND);
								Each each = new EachImpl(whereStatement, conditionInfo, outerEach);
								return new RelationalOperatorImpl(whereStatement, each, conditionInfo);
							}
							
							/* (non-Javadoc)
							 * @see org.openspotlight.graph.query.SLWhereByNodeType.Type.Each.Property.Operator.Value.CloseBracket#typeEnd()
							 */
							public SLWhereByNodeType typeEnd() {
								return whereStatement;
							}
						}
					}
				}
			}
		}
	}

}
