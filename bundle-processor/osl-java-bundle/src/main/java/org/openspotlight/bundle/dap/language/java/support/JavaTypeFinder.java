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
/**
 * 
 */
package org.openspotlight.bundle.dap.language.java.support;

import static org.openspotlight.common.util.Exceptions.logAndReturnNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspotlight.bundle.dap.language.java.metamodel.link.Extends;
import org.openspotlight.bundle.dap.language.java.metamodel.link.Implements;
import org.openspotlight.bundle.dap.language.java.metamodel.link.JavaLink;
import org.openspotlight.bundle.dap.language.java.metamodel.node.JavaType;
import org.openspotlight.bundle.dap.language.java.metamodel.node.JavaTypePrimitive;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.query.SLQuery;
import org.openspotlight.graph.query.SLQueryResult;

/**
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
public class JavaTypeFinder extends TypeFinder<JavaType, JavaLink> {

    private static final List<Class<? extends JavaLink>> implementationInheritanceLinks = new ArrayList<Class<? extends JavaLink>>();
    private static final List<Class<? extends JavaLink>> interfaceInheritanceLinks      = new ArrayList<Class<? extends JavaLink>>();
    private static final List<Class<? extends JavaLink>> primitiveHierarchyLinks        = new ArrayList<Class<? extends JavaLink>>();
    private static final List<Class<? extends JavaType>> primitiveTypes                 = new ArrayList<Class<? extends JavaType>>();

    static {
        implementationInheritanceLinks.add(Extends.class);
        interfaceInheritanceLinks.add(Implements.class);
        primitiveTypes.add(JavaTypePrimitive.class);
        primitiveHierarchyLinks.add(Extends.class);//FIXME

    }

    public JavaTypeFinder(
                           final SLContext abstractContext, final List<SLContext> orderedActiveContexts,
                           final boolean enableBoxing, final SLGraphSession session ) {
        super(implementationInheritanceLinks, interfaceInheritanceLinks, primitiveHierarchyLinks, abstractContext,
              orderedActiveContexts, primitiveTypes, enableBoxing, session);
    }

    /**
     * @{inheritDoc
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public <T extends JavaType> T getType( final String typeToSolve ) throws NodeNotFoundException {
        try {
            final SLQuery query = this.getSession().createQuery();
            query.selectByNodeType().type(JavaType.class.getName()).subTypes().selectEnd().where().type(JavaType.class.getName()).subTypes().each().property(
                                                                                                                                                             "completeName").equalsTo().value(
                                                                                                                                                                                              "java.util.Collection").typeEnd().whereEnd();
            final SLQueryResult result = query.execute();
            final Map<String, List<JavaType>> resultMap = new HashMap<String, List<JavaType>>();
            for (final SLContext ctx : super.getOrderedActiveContexts()) {
                resultMap.put(ctx.getID(), new ArrayList<JavaType>());
            }
            for (final SLNode n : result.getNodes()) {
                final List<JavaType> resultList = resultMap.get(n.getContext().getID());
                if ((resultList != null) && (n instanceof JavaType)) {
                    resultList.add((JavaType)n);
                }
            }
            for (final SLContext ctx : super.getOrderedActiveContexts()) {
                final List<JavaType> resultList = resultMap.get(ctx.getID());
                if (resultList.size() > 0) {
                    return (T)resultList.get(0);
                }
            }
            throw new NodeNotFoundException();
        } catch (final Exception e) {
            throw logAndReturnNew(e, NodeNotFoundException.class);
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    public <T extends JavaType, A extends JavaType> T getType( final String typeToSolve,
                                                               final A activeType,
                                                               final List<? extends JavaType> parametrizedTypes ) {
        throw new UnsupportedOperationException("not implemented yet");
    }

}
