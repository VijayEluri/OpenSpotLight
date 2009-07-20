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

package org.openspotlight.common.util;

import static org.openspotlight.common.util.Assertions.checkCondition;
import static org.openspotlight.common.util.Exceptions.logAndThrow;

/**
 * Helper class to build equals methods in a secure and concise way
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
public class Equals {
    
    /**
     * Method that call equals in a null pointer safe way.
     * 
     * @param o1
     * @param o2
     * @return
     */
    public static boolean eachEquality(final Object o1, final Object o2) {
        if (o1 == o2) {
            return true;
        }
        if ((o1 == null) && (o2 != null)) {
            return false;
        }
        if ((o2 == null) && (o1 != null)) {
            return false;
        }
        return (o1 != null) && o1.equals(o2);
    }
    
    /**
     * Equals method to be used like this:
     * 
     * <pre>
     * import static org.openspotlight.common.util.Arrays.of;
     * import static org.openspotlight.common.util.Arrays.andOf;
     * import static org.openspotlight.common.util.Equals.equals;
     * 
     * //...
     * 
     * public void equals(Object o){
     *     if(o==this)
     *         return true;
     *     if(!(o instanceof ThisClass))
     *         return false;
     *     that = (ThisClass) o;
     *     return eachEquality(of(this.attribute1,this.attribute2)
     *                     ,andOf(that.attribute1,that.attribute2));
     * }
     * 
     * </pre>
     * 
     * @param of
     * @param andOf
     * @return
     */
    public static boolean eachEquality(final Object[] of, final Object[] andOf) {
        if (of == null) {
            if (andOf == null) {
                return true;
            }
            return false;
        }
        checkCondition("sameSize", (of.length == andOf.length)); //$NON-NLS-1$
        final int size = of.length;
        for (int i = 0; i < size; i++) {
            if (!eachEquality(of[i], andOf[i])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Should not be instantiated
     */
    private Equals() {
        logAndThrow(new IllegalStateException(Messages
                .getString("invalidConstructor"))); //$NON-NLS-1$
    }
}
