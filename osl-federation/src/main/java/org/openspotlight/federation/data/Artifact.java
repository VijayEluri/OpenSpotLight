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

import static java.util.Collections.emptySet;
import static org.openspotlight.common.util.Arrays.andValues;
import static org.openspotlight.common.util.Arrays.map;
import static org.openspotlight.common.util.Arrays.ofKeys;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public final class Artifact extends AbstractConfigurationNode {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = -889016915372708085L;
    
    private static final String DATA = "data";
    
    private static final String DATA_SHA1 = "dataSha1";
    
    @SuppressWarnings("unchecked")
    private static final Map<String, Class<?>> PROPERTY_TYPES = map(ofKeys(
            DATA_SHA1, DATA), andValues(String.class, InputStream.class));
    
    public Artifact(final String name, final Bundle bundle) {
        super(name, bundle, PROPERTY_TYPES);
    }
    
    public Bundle getBundle() {
        return this.getParent();
    }
    
    @Override
    public Set<Class<?>> getChildrenTypes() {
        return emptySet();
    }
    
    public InputStream getData() {
        return this.getTransientProperty(DATA);
    }
    
    public String getDataSha1() {
        return this.getProperty(DATA_SHA1);
    }
    
    @Override
    public Class<?> getParentType() {
        return Bundle.class;
    }
    
    public void setData(final InputStream data) {
        this.setTransientProperty(DATA, data);
    }
    
    public void setDataSha1(final String dataSha1) {
        this.setProperty(DATA_SHA1, dataSha1);
    }
    
}
