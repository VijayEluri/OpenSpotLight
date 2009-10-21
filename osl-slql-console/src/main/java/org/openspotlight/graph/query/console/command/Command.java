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
package org.openspotlight.graph.query.console.command;

import java.io.PrintWriter;

import jline.ConsoleReader;

import org.openspotlight.graph.query.console.ConsoleState;

/**
 * The Interface Command.
 * 
 * @author porcelli
 */
public interface Command {

    /**
     * The Enum FileCompletionMode.
     * 
     * @author porcelli
     */
    public enum FileCompletionMode {

        /** The STARTS_WITH mode. */
        STARTS_WITH,

        /** The CONTAINS mode. */
        CONTAINS
    }

    /**
     * Execute.
     * 
     * @param reader the reader
     * @param out the out
     * @param state the state
     */
    public void execute( ConsoleReader reader,
                         PrintWriter out,
                         ConsoleState state );

    /**
     * Gets the console command.
     * 
     * @return the console command
     */
    public String getCommand();

    /**
     * Gets the auto complete command.
     * 
     * @return the auto complete command
     */
    public String getAutoCompleteCommand();

    /**
     * Gets the description. This will be printed at help statement.
     * 
     * @return the description
     */
    public String getDescription();

    /**
     * Checks if Command supports file completion.
     * 
     * @return true, if successful
     */
    public boolean hasFileCompletion();

    /**
     * Gets the file completion command.
     * 
     * @return the file completion command
     */
    public String getFileCompletionCommand();

    /**
     * Gets the file completion mode.
     * 
     * @return the file completion mode
     */
    public FileCompletionMode getFileCompletionMode();

    /**
     * Accept.
     * 
     * @param state the state
     * @return true, if successful
     */
    public boolean accept( ConsoleState state );

}
