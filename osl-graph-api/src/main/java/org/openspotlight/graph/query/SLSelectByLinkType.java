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


/**
 * The Interface SLSelectByLinkType.
 * 
 * @author Vitor Hugo Chagas
 */
public interface SLSelectByLinkType extends SLSelect {

	/**
	 * Type.
	 * 
	 * @param typeName the type name
	 * 
	 * @return the type
	 */
	public Type type(String typeName);
	
	/**
	 * By link.
	 * 
	 * @param typeName the type name
	 * 
	 * @return the by link
	 */
	public ByLink byLink(String typeName);
	
	/**
	 * End.
	 * 
	 * @return the end
	 */
	public End end();
	
	/**
	 * The Interface Type.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static interface Type {
		
		/**
		 * Comma.
		 * 
		 * @return the sL select by link type
		 */
		public SLSelectByLinkType comma();
		
		/**
		 * Select end.
		 * 
		 * @return the end
		 */
		public End selectEnd();
		
		/**
		 * Sub types.
		 * 
		 * @return the type
		 */
		public Type subTypes();
	}
	
	/**
	 * The Interface End.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static interface End extends SLSelectFacade {
		
		/**
		 * Where.
		 * 
		 * @return the sL where by link type
		 */
		public SLWhereByLinkType where();
		
		/**
		 * Order by.
		 * 
		 * @return the sL order by statement
		 */
		public SLOrderByStatement orderBy();
		
		/**
		 * Keep result.
		 * 
		 * @return the end
		 */
		public End keepResult();
		
		/**
		 * Execute x times.
		 * 
		 * @return the end
		 */
		public End executeXTimes();
		
		/**
		 * Execute x times.
		 * 
		 * @param x the x
		 * 
		 * @return the end
		 */
		public End executeXTimes(int x);
	}
	
	/**
	 * The Interface ByLink.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static interface ByLink {
		
		/**
		 * Comma.
		 * 
		 * @return the sL select by link type
		 */
		public SLSelectByLinkType comma();
		
		/**
		 * Select end.
		 * 
		 * @return the end
		 */
		public End selectEnd();
		
		/**
		 * A.
		 * 
		 * @return the by link
		 */
		public ByLink a();
		
		/**
		 * B.
		 * 
		 * @return the by link
		 */
		public ByLink b();
		
		/**
		 * Any.
		 * 
		 * @return the by link
		 */
		public ByLink any();
	}
}
