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

import org.openspotlight.common.exception.SLException;

public class SLInvalidQuerySyntaxException extends SLException {

    /**
     * 
     */
    private static final long serialVersionUID = 400L;

    private String            errorCode        = null;
    private int               lineNumber;
    private int               column;
    private int               offset;

    /**
     * @see java.lang.Exception#Exception(String message)
     */
    public SLInvalidQuerySyntaxException(
                                           final String message ) {
        super(message);
    }

    /**
     * @see java.lang.Exception#Exception(String message, Throwable cause)
     */
    public SLInvalidQuerySyntaxException(
                                           final String message,
                                           final Throwable cause ) {
        super(message);
    }

    /**
     * @see java.lang.Exception#Exception(Throwable cause)
     */
    public SLInvalidQuerySyntaxException(
                                           final Throwable cause ) {
        super(cause);
    }

    /**
     * SLQueryLanguageParserException constructor.
     * 
     * @param errorCode error code
     * @param message message
     * @param lineNumber line number
     * @param column column
     * @param offset offset
     * @param cause exception cause
     */
    public SLInvalidQuerySyntaxException(
                                           String errorCode,
                                           String message,
                                           int lineNumber,
                                           int column,
                                           int offset,
                                           Throwable cause ) {
        super(message, cause);
        this.errorCode = errorCode;
        this.lineNumber = lineNumber;
        this.column = column;
        this.offset = offset;
    }

    public String getMessage() {
        if (null == errorCode) {
            return super.getMessage();
        }
        return "[" + errorCode + "] " + super.getMessage();
    }

    /**
     * getter for error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * getter for line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * getter for column position
     */
    public int getColumn() {
        return column;
    }

    /**
     * getter for char offset
     */
    public int getOffset() {
        return offset;
    }

}
