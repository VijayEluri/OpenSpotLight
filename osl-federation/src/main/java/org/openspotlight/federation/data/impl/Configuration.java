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

package org.openspotlight.federation.data.impl;

import static org.openspotlight.federation.data.InstanceMetadata.Factory.createRoot;

import java.util.Collection;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;

import org.openspotlight.federation.data.ConfigurationNode;
import org.openspotlight.federation.data.InstanceMetadata;
import org.openspotlight.federation.data.StaticMetadata;

/**
 * This is the root node of the configuration classes that contains the
 * following structure.
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 * &lt;configuration&gt;
 * 	&lt;repository name=&quot;development&quot; active=&quot;true&quot;
 * 		numberOfParallelThreads=&quot;15&quot;&gt;
 * 		&lt;project name=&quot;Open SpotLight&quot; active=&quot;true&quot;&gt;
 * 			&lt;bundle name=&quot;Java sources&quot; active=&quot;true&quot; type=&quot;fileSystem&quot;
 * 				initialLookup=&quot;/usr/src/osl&quot;&gt;
 * 				&lt;artifact
 * 					name=&quot;/usr/src/osl/src/main/java/org/openspotligth/Example.java&quot;
 * 					dataSha1=&quot;sha1&quot; /&gt;
 * 				&lt;artifact
 * 					name=&quot;/usr/src/osl/src/main/java/org/openspotligth/AnotherOneExample.java&quot;
 * 					dataSha1=&quot;sha1&quot; /&gt;
 * 				&lt;artifactMapping name=&quot;all java files&quot; active=&quot;true&quot;
 * 					included=&quot;/src/main/java/ **.java&quot; /&gt;
 * 				&lt;artifactMapping name=&quot;no older types&quot; active=&quot;true&quot;
 * 					excluded=&quot;** / *.asm&quot; /&gt;
 * 			&lt;/bundle&gt;
 * 		&lt;/project&gt;
 * 	&lt;/repository&gt;
 * &lt;/configuration&gt;
 * </pre>
 * 
 * This structure are used to pass the artifacts to the parser. All the classes
 * are thread save by default.
 * 
 * @see ConfigurationNode
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("unchecked")
@ThreadSafe
@StaticMetadata(validChildrenTypes = Repository.class)
public final class Configuration implements ConfigurationNode {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5615522050633506216L;
    
    private final InstanceMetadata instanceMetadata;
    
    /**
     * Default constructor
     */
    public Configuration() {
        this.instanceMetadata = createRoot(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public final int compareTo(final ConfigurationNode o) {
        return this.instanceMetadata.compare(this, o);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        return this.instanceMetadata.equals(obj);
    }
    
    /**
     * {@inheritDoc}
     */
    public final InstanceMetadata getInstanceMetadata() {
        return this.instanceMetadata;
    }
    
    /**
     * 
     * @return all repositories
     */
    public final Collection<Repository> getRepositories() {
        return this.instanceMetadata.getChildrensOfType(Repository.class);
    }
    
    /**
     * Returns a repository by its name.
     * 
     * @param name
     * @return a repository
     */
    public final Repository getRepositoryByName(final String name) {
        return this.instanceMetadata.getChildByKeyValue(Repository.class, name);
    }
    
    /**
     * 
     * @return all repository names
     */
    public final Set<String> getRepositoryNames() {
        return (Set<String>) this.instanceMetadata
                .getKeysFromChildrenOfType(Repository.class);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return this.instanceMetadata.hashCode();
    }
    
    /**
     * Removes a given repository
     * 
     * @param repository
     */
    public final void removeRepository(final Repository repository) {
        this.instanceMetadata.removeChild(repository);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return this.instanceMetadata.toString();
    }
    
}
