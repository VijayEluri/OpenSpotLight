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

package org.openspotlight.federation.data;

import static org.openspotlight.common.util.Assertions.checkNotEmpty;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;

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
 * @see ConfigurationNodeMetadata
 * @see AbstractConfigurationNode
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@ThreadSafe
public final class Configuration extends AbstractConfigurationNode {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 4126478190563727620L;
    
    public static final String DEFAULT_CONFIGURATION_NAME = "root";
    
    /**
     * static property types.
     */
    private static final Map<String, Class<?>> PROPERTY_TYPES = new HashMap<String, Class<?>>();
    
    private static final Set<Class<?>> CHILDREN_CLASSES = new HashSet<Class<?>>();
    
    static {
        CHILDREN_CLASSES.add(Repository.class);
    }
    
    /**
     * Default constructor.
     */
    public Configuration() {
        super(DEFAULT_CONFIGURATION_NAME, null, PROPERTY_TYPES);
    }
    
    /**
     * Adds a new repository.
     * 
     * @param repository
     *            to be added
     */
    public void addRepository(final Repository repository) {
        this.addChild(repository);
    }
    
    public Artifact findByName(final String initialLookup,
            final String artifactName) {
        checkNotEmpty("initialLookup", initialLookup);
        checkNotEmpty("artifactName", artifactName);
        for (final Repository repository : this.getRepositories()) {
            for (final Project project : repository.getProjects()) {
                for (final Bundle bundle : project.getBundles()) {
                    if (bundle.getInitialLookup().equals(initialLookup)) {
                        for (final Artifact artifact : bundle.getArtifacts()) {
                            if (artifactName.equals(artifact.getName())) {
                                return artifact;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Set<Class<?>> getChildrenTypes() {
        return CHILDREN_CLASSES;
    }
    
    @Override
    public Class<?> getParentType() {
        return null;
    }
    
    public Collection<Repository> getRepositories() {
        return super.getChildrensOfType(Repository.class);
    }
    
    public Repository getRepositoryByName(final String name) {
        return super.getChildByName(Repository.class, name);
    }
    
    public Set<String> getRepositoryNames() {
        return super.getNamesFromChildrenOfType(Repository.class);
    }
    
    public void removeRepository(final Repository repository) {
        this.removeChild(repository);
    }
    
}
