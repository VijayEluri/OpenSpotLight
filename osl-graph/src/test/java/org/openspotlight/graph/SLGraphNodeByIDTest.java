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
package org.openspotlight.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.apache.log4j.Logger;
import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.graph.query.SLGraphQueryTest;
import org.openspotlight.graph.test.domain.JavaInterface;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The Class SLGraphNodeByIDTest.
 * 
 * @author Vitor Hugo Chagas
 */
@Test
public class SLGraphNodeByIDTest {
	
	/** The Constant LOGGER. */
	static final Logger LOGGER = Logger.getLogger(SLGraphQueryTest.class);
	
	/** The graph. */
	private SLGraph graph;
	
	/** The session. */
	private SLGraphSession session;

	/**
	 * Sets the up.
	 */
	@BeforeClass
	public void setUp() {
		try {
			SLGraphFactory factory = AbstractFactory.getDefaultInstance(SLGraphFactory.class);
            graph = factory.createTempGraph(true);
            session = graph.openSession();
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

    /**
     * Finish.
     */
    @AfterClass
    public void finish() {
        session.close();
        graph.shutdown();
    }
    
    /**
     * Test get node by id casting.
     */
    @Test
    public void testGetNodeByIDCasting() {
    	try {
    		
            SLContext context = session.createContext("linkCountTest");
            SLNode root = context.getRootNode();
            
            JavaInterface javaInterface = root.addNode(JavaInterface.class, "javaInterface");
            JavaInterface javaInterface2 = (JavaInterface) session.getNodeByID(javaInterface.getID());
            
            assertThat(javaInterface, is(javaInterface2));
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
    }
}
