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
package org.openspotlight.federation.data.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openspotlight.federation.data.ConfigurationNode;
import org.openspotlight.federation.data.InstanceMetadata.ItemChangeEvent;
import org.openspotlight.federation.data.InstanceMetadata.ItemEventListener;
import org.openspotlight.federation.data.InstanceMetadata.SharedData;
import org.openspotlight.federation.data.impl.Configuration;
import org.openspotlight.federation.data.impl.Group;
import org.openspotlight.federation.data.impl.Repository;

/**
 * Test class for {@link SharedData} {@link ItemEventListener node listeners}.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("all")
public class SharedDataListenerTest {

	private static class CustomNodeListener implements
			ItemEventListener<ConfigurationNode> {

		private final List<ConfigurationNode> nodesChanged = new ArrayList<ConfigurationNode>();

		public void changeEventHappened(
				final ItemChangeEvent<ConfigurationNode> event) {
			final ConfigurationNode target = event.getNewItem() != null ? event
					.getNewItem() : event.getOldItem();
			this.nodesChanged.add(target);

		}

		public List<ConfigurationNode> getNodesChanged() {
			return this.nodesChanged;
		}

	}

	@Test
	public void shouldListenChangesForAGivenParent() throws Exception {
		final CustomNodeListener listener = new CustomNodeListener();
		final Configuration configuration = new Configuration();
		final Repository repo1 = new Repository(configuration, "repo1");
		final Repository repo2 = new Repository(configuration, "repo2");
		configuration.getInstanceMetadata().getSharedData()
				.addNodeListenerForAGivenParent(listener, repo2);
		new Group(repo1, "group1");
		final Group group2 = new Group(repo2, "group2");
		assertThat(listener.getNodesChanged().size(), is(1));
		assertThat((Group) listener.getNodesChanged().get(0), is(group2));

	}

	@Test
	public void shouldListenChangesForAGivenType() throws Exception {
		final CustomNodeListener listener = new CustomNodeListener();
		final Configuration configuration = new Configuration();
		configuration.getInstanceMetadata().getSharedData()
				.addNodeListenerForAGivenType(listener, Group.class);
		final Repository repo = new Repository(configuration, "repo");
		final Group group = new Group(repo, "group");
		assertThat(listener.getNodesChanged().size(), is(1));
		assertThat((Group) listener.getNodesChanged().get(0), is(group));
	}

}
