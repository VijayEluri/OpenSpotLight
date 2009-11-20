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

import org.openspotlight.common.SharedConstants;

/**
 * The Class SLConsts.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLConsts implements SharedConstants {

    public static final String DEFAULT_GROUP_CONTEXT                       = "groupContext";

    /** The Constant DEFAULT_REPOSITORY_NAME. */
    public static final String DEFAULT_REPOSITORY_NAME                     = "default";

    /** The Constant NODE_NAME_LINK. */
    public static final String NODE_NAME_LINK                              = "link";

    /** The Constant DIRECTION_AB. */
    public static final int    DIRECTION_AB                                = 1;

    /** The Constant DIRECTION_BA. */
    public static final int    DIRECTION_BA                                = 2;

    /** The Constant DIRECTION_BOTH. */
    public static final int    DIRECTION_BOTH                              = 3;

    /** The Constant NODE_NAME_CONTEXTS. */
    public static final String NODE_NAME_CONTEXTS                          = "contexts";

    /** The Constant NODE_NAME_QUERY_CACHE. */
    public static final String NODE_NAME_QUERY_CACHE                       = "queryCache";

    /** The Constant NODE_NAME_LINKS. */
    public static final String NODE_NAME_LINKS                             = "links";

    /** The Constant NODE_NAME_METADATA. */
    public static final String NODE_NAME_METADATA                          = "metadata";

    /** The Constant NODE_NAME_TYPES. */
    public static final String NODE_NAME_TYPES                             = "types";

    /** The Constant PROPERTY_NAME_PRIMARY_KEY. */
    public static final String PROPERTY_NAME_PRIMARY_KEY                   = "primaryKey";

    /** The Constant PROPERTY_NAME_SECONDARY_KEY. */
    public static final String PROPERTY_NAME_SECONDARY_KEY                 = "secondaryKey";

    /** The Constant PROPERTY_NAME_TERTIARY_KEY. */
    public static final String PROPERTY_NAME_TERTIARY_KEY                  = "tertiaryKey";

    /** The Constant PROPERTY_NAME_PRIMARY_DESCRIPTION. */
    public static final String PROPERTY_NAME_PRIMARY_DESCRIPTION           = "primaryDescription";

    /** The Constant PROPERTY_NAME_SECONDARY_DESCRIPTION. */
    public static final String PROPERTY_NAME_SECONDARY_DESCRIPTION         = "secondaryDescription";

    /** The Constant PROPERTY_NAME_TERTIARY_DESCRIPTION. */
    public static final String PROPERTY_NAME_TERTIARY_DESCRIPTION          = "tertiaryDescription";

    /** The Constant PROPERTY_NAME_RENDER_HINT. */
    public static final String PROPERTY_NAME_RENDER_HINT                   = "renderHint";

    /** The Constant PROPERTY_NAME_DESCRIPTION. */
    public static final String PROPERTY_NAME_DESCRIPTION                   = "description";

    /** The Constant PROPERTY_NAME_META_NODE_ID. */
    public static final String PROPERTY_NAME_META_NODE_ID                  = "metaNodeID";

    /** The Constant PROPERTY_NAME_UNIDIRECTIONAL. */
    public static final String PROPERTY_NAME_UNIDIRECTIONAL                = "unidirectional";

    /** The Constant PROPERTY_NAME_BIDIRECTIONAL. */
    public static final String PROPERTY_NAME_BIDIRECTIONAL                 = "bidirectional";

    /** The Constant PROPERTY_NAME_ALLOWS_CHANGE_TO_BIDIRECIONAL. */
    public static final String PROPERTY_NAME_ALLOWS_CHANGE_TO_BIDIRECIONAL = "allowsChangeToBidirecional";

    /** The Constant PROPERTY_NAME_TYPE. */
    public static final String PROPERTY_NAME_TYPE                          = "node.type";

    /** The Constant PROPERTY_NAME_DECODED_NAME. */
    public static final String PROPERTY_NAME_DECODED_NAME                  = "decodedName";

    /** The Constant PROPERTY_NAME_A_NODE_ID. */
    public static final String PROPERTY_NAME_A_NODE_ID                     = "aNodeID";

    /** The Constant PROPERTY_NAME_B_NODE_ID. */
    public static final String PROPERTY_NAME_B_NODE_ID                     = "bNodeID";

    /** The Constant PROPERTY_NAME_DIRECTION. */
    public static final String PROPERTY_NAME_DIRECTION                     = "direction";

    /** The Constant PROPERTY_NAME_LINK_COUNT. */
    public static final String PROPERTY_NAME_LINK_COUNT                    = "linkCount";

    /** The Constant PROPERTY_NAME_NODE_TYPE. */
    public static final String PROPERTY_NAME_NODE_TYPE                     = "nodeType";

    /** The Constant PROPERTY_NAME_A_CLASS_NAME. */
    public static final String PROPERTY_NAME_A_CLASS_NAME                  = "aClassName";

    /** The Constant PROPERTY_NAME_B_CLASS_NAME. */
    public static final String PROPERTY_NAME_B_CLASS_NAME                  = "bClassName";

    /** The Constant PROPERTY_NAME_SOURCE_ID. */
    public static final String PROPERTY_NAME_SOURCE_ID                     = "sourceID";

    /** The Constant PROPERTY_NAME_TARGET_ID. */
    public static final String PROPERTY_NAME_TARGET_ID                     = "targetID";

    /** The Constant PROPERTY_NAME_SOURCE_TYPE_HASH. */
    public static final String PROPERTY_NAME_SOURCE_TYPE_HASH              = "sourceTypeHash";

    /** The Constant PROPERTY_NAME_TARGET_TYPE_HASH. */
    public static final String PROPERTY_NAME_TARGET_TYPE_HASH              = "targetTypeHash";

    /** The Constant PROPERTY_NAME_LINK_TYPE_HASH. */
    public static final String PROPERTY_NAME_LINK_TYPE_HASH                = "linkTypeHash";

    /** The Constant PROPERTY_NAME_SOURCE_COUNT. */
    public static final String PROPERTY_NAME_SOURCE_COUNT                  = "sourceCount";

    /** The Constant PROPERTY_NAME_TARGET_COUNT. */
    public static final String PROPERTY_NAME_TARGET_COUNT                  = "targetCount";

    /** The Constant PROPERTY_PREFIX_INTERNAL. */
    public static final String PROPERTY_PREFIX_INTERNAL                    = "internal";

    /** The Constant PROPERTY_PREFIX_USER. */
    public static final String PROPERTY_PREFIX_USER                        = "user";

}
