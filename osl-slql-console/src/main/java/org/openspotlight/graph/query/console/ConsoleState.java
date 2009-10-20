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
package org.openspotlight.graph.query.console;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.query.console.command.Command;

/**
 * The Class ConsoleState. This is a simple data class that holds console application state.
 * 
 * @author porcelli
 */
public class ConsoleState {

    /** The session. */
    private SLGraphSession session              = null;

    /** The StringBuilder that holds the buffer. */
    private StringBuilder  sb                   = new StringBuilder();

    /** The additional properties that should be displayed during query output result. */
    private Set<String>    additionalProperties = new HashSet<String>();

    /** The active command. */
    private Command        activeCommand        = null;

    /** The quit application. */
    private boolean        quitApplication      = false;

    /** The input. */
    private String         input                = "";

    /** The last query. */
    private String         lastQuery            = "";

    /**
     * Instantiates a new console state.
     * 
     * @param session the session
     */
    public ConsoleState(
                         SLGraphSession session ) {
        this.session = session;
    }

    /**
     * Gets the buffer.
     * 
     * @return the buffer
     */
    public String getBuffer() {
        return sb.toString();
    }

    /**
     * Append buffer.
     * 
     * @param buffer the buffer
     */
    public void appendBuffer( String buffer ) {
        this.sb.append(buffer);
    }

    /**
     * Append line buffer.
     * 
     * @param buffer the buffer
     */
    public void appendLineBuffer( String buffer ) {
        appendBuffer(buffer);
        appendBuffer("\n");
    }

    /**
     * Clear buffer.
     */
    public void clearBuffer() {
        sb = new StringBuilder();
    }

    /**
     * Gets the active command.
     * 
     * @return the active command
     */
    public Command getActiveCommand() {
        return activeCommand;
    }

    /**
     * Sets the active command.
     * 
     * @param activeCommand the new active command
     */
    public void setActiveCommand( Command activeCommand ) {
        this.activeCommand = activeCommand;
    }

    /**
     * Gets the last query.
     * 
     * @return the last query
     */
    public String getLastQuery() {
        return lastQuery;
    }

    /**
     * Sets the last query.
     * 
     * @param lastQuery the new last query
     */
    public void setLastQuery( String lastQuery ) {
        if (lastQuery == null) {
            this.lastQuery = "";
        } else {
            this.lastQuery = lastQuery;
        }
    }

    /**
     * Gets the input.
     * 
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the input.
     * 
     * @param input the new input
     */
    public void setInput( String input ) {
        if (input == null) {
            this.input = "";
        } else {
            this.input = input;
        }
    }

    /**
     * Quit application.
     * 
     * @return true, if application should quit
     */
    public boolean quitApplication() {
        return quitApplication;
    }

    /**
     * Sets the quit application.
     * 
     * @param quitApplication the new quit application
     */
    public void setQuitApplication( boolean quitApplication ) {
        this.quitApplication = quitApplication;
    }

    /**
     * Gets the session.
     * 
     * @return the session
     */
    public SLGraphSession getSession() {
        return session;
    }

    /**
     * Gets the additional properties that should be displayed during query output result.
     * 
     * @return the additional properties
     */
    public Collection<String> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Adds the additional property that should be displayed during query output result.
     * 
     * @param additionalProperty the additional property
     */
    public void addAdditionalProperty( String additionalProperty ) {
        if (additionalProperty.trim().length() > 0) {
            this.additionalProperties.add(additionalProperty);
        }
    }

    /**
     * Removes additional property from query output result.
     * 
     * @param additionalProperty the additional property
     */
    public void removesAdditionalProperty( String additionalProperty ) {
        if (additionalProperty.trim().length() > 0 && this.additionalProperties.contains(additionalProperty)) {
            this.additionalProperties.remove(additionalProperty);
        }
    }

    /**
     * Reset additional properties.
     */
    public void resetAdditionalProperties() {
        additionalProperties = new HashSet<String>();
    }

}
