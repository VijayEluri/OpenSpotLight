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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.clapper.util.classutil.AbstractClassFilter;
import org.clapper.util.classutil.AndClassFilter;
import org.clapper.util.classutil.ClassFilter;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;
import org.clapper.util.classutil.InterfaceOnlyClassFilter;
import org.clapper.util.classutil.NotClassFilter;
import org.clapper.util.classutil.SubclassClassFilter;
import org.openspotlight.graph.query.console.command.system.ClearSystemCommand;
import org.openspotlight.graph.query.console.command.system.ExitSystemCommand;
import org.openspotlight.graph.query.console.command.system.HelpSystemCommand;
import org.openspotlight.graph.query.console.command.system.VersionSystemCommand;

/**
 * Helper class for DynamicComamnd loading.
 * 
 * @author porcelli
 */
public class DynamicCommandSupport {

    /**
     * Gets all non abstract classes that implements {@link DynamicCommand}.
     * 
     * @return the registered dynamic commands
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static List<Command> getRegisteredDynamicCommands()
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<Command> result = new LinkedList<Command>();
        ClassFinder finder = new ClassFinder();
        finder.addClassPath();

        ClassFilter filter =
            new AndClassFilter(
                                       //Must not be an interface
        new NotClassFilter(new InterfaceOnlyClassFilter()),
                                       //Must implement the ClassFilter interface
        new SubclassClassFilter(DynamicCommand.class),
                                       // Must not be abstract
        new NotClassFilter(new AbstractClassFilter()));

        Collection<ClassInfo> foundClasses = new ArrayList<ClassInfo>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            Class<?> clasz = Class.forName(classInfo.getClassName());

            DynamicCommand generatedCommand = (DynamicCommand)clasz.newInstance();
            result.add(generatedCommand);
        }
        Command clear = new ClearSystemCommand();
        result.add(clear);

        ExitSystemCommand exit = new ExitSystemCommand();
        result.add(exit);

        VersionSystemCommand version = new VersionSystemCommand();
        result.add(version);

        Command help = new HelpSystemCommand(result);
        result.add(help);

        return result;
    }
}
