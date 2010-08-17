/**
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
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 * Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.graph.query.console.command;

import org.clapper.util.classutil.*;
import org.openspotlight.graph.query.console.command.system.ClearSystemCommand;
import org.openspotlight.graph.query.console.command.system.ExitSystemCommand;
import org.openspotlight.graph.query.console.command.system.HelpSystemCommand;
import org.openspotlight.graph.query.console.command.system.VersionSystemCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

        ClassFilter filter = new AndClassFilter(
        // Must not be an interface
                                                new NotClassFilter(new InterfaceOnlyClassFilter()),
                                                // Must implement the ClassFilter interface
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
