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
package org.openspotlight.graph.query.console.command.dynamic;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import jline.ConsoleReader;

import org.openspotlight.common.util.Assertions;
import org.openspotlight.graph.query.console.ConsoleState;
import org.openspotlight.graph.query.console.command.DynamicCommand;

/**
 * The Class OpenFileCommand. This command opens a file and loads its content into active query buffer.
 * 
 * @author porcelli
 */
public class OpenFileCommand implements DynamicCommand {

    /**
     * {@inheritDoc}
     */
    public void execute( ConsoleReader reader,
                         PrintWriter out,
                         ConsoleState state ) {
        Assertions.checkNotNull("reader", reader);
        Assertions.checkNotNull("out", out);
        Assertions.checkNotNull("state", state);
        if (!accept(state)) {
            return;
        }
        String fileName = state.getInput().substring(3).trim();

        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            try {
                state.clearBuffer();
                LineNumberReader fileReader = new LineNumberReader(new FileReader(file));
                while (fileReader.ready()) {
                    state.appendLineBuffer(fileReader.readLine());
                }
                state.setLastQuery(state.getBuffer());
                state.clearBuffer();
                out.println("query loaded into buffer.");
            } catch (Exception e) {
                state.clearBuffer();
                out.print("ERROR: ");
                out.println(e.getMessage());
            }
        } else {
            out.print("ERROR: ");
            out.println("invalid file name.");
        }
        out.flush();
        state.setInput(null);
        state.clearBuffer();
    }

    /**
     * {@inheritDoc}
     */
    public String getCommand() {
        return "get";
    }

    /**
     * {@inheritDoc}
     */
    public String getAutoCompleteCommand() {
        return "get";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "gets the slql query from file";
    }

    /**
     * {@inheritDoc}
     */
    public String getFileCompletionCommand() {
        return "get";
    }

    /**
     * {@inheritDoc}
     */
    public FileCompletionMode getFileCompletionMode() {
        return FileCompletionMode.STARTS_WITH;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasFileCompletion() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept( ConsoleState state ) {
        Assertions.checkNotNull("state", state);
        if (state.getActiveCommand() == null && state.getInput().trim().startsWith("get ")) {
            return true;
        }
        return false;
    }
}
