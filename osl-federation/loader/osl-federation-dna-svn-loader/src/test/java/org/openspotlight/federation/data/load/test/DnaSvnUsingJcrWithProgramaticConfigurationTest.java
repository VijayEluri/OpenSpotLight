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

package org.openspotlight.federation.data.load.test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.jboss.dna.connector.svn.SVNRepositorySource;
import org.jboss.dna.jcr.JcrConfiguration;
import org.jboss.dna.jcr.JcrEngine;
import org.jboss.dna.jcr.SecurityContextCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openspotlight.federation.data.load.DefaultSecurityContext;

/**
 * Test class to validate a programatic example for DNA Svn System Connector
 * using JCR.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@Ignore
@SuppressWarnings("all")
public class DnaSvnUsingJcrWithProgramaticConfigurationTest {
	private JcrEngine engine;
	private Session session;

	@Before
	public void setup() throws Exception {
		final String svnRepositorySource = "svnRepositorySource";
		final String repositoryName = "svnRepository";
		final JcrConfiguration configuration = new JcrConfiguration();
		configuration
				.repositorySource(svnRepositorySource)
				.usingClass(SVNRepositorySource.class)
				.setProperty("password", "")
				.setProperty("username", "anonymous")
				.setProperty(
						"repositoryRootURL", "http://hamcrest.googlecode.com/svn/trunk/hamcrest-java/").setProperty( //$NON-NLS-1$ //$NON-NLS-2$
						"creatingWorkspacesAllowed", true);

		configuration.repository(repositoryName).setSource(svnRepositorySource);
		configuration.save();
		this.engine = configuration.build();
		this.engine.start();

		this.session = this.engine.getRepository(repositoryName)
				.login(
						new SecurityContextCredentials(
								DefaultSecurityContext.READ_ONLY));

	}

	@After
	public void shutdown() throws Exception {
		if (this.session != null) {
			this.session.logout();
		}
		if (this.engine != null) {
			this.engine.shutdown();
		}
	}

	@Test
	public void test() throws Exception {
		NodeIterator nodeIterator = this.session.getRootNode().getNodes();

		while (nodeIterator.hasNext()) {
			System.out.println(nodeIterator.nextNode());
		}
		assertThat(this.session.getRootNode().getNode("hamcrest-core"),
				is(notNullValue()));
	}
}
