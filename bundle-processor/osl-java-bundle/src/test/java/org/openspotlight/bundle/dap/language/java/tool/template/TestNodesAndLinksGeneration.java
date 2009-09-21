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
package org.openspotlight.bundle.dap.language.java.tool.template;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.junit.Test;
import org.openspotlight.bundle.dap.language.java.tool.template.TemplateTask;

public class TestNodesAndLinksGeneration {

    @Test
    public void shouldCreateClassFiles() throws Exception {
        final TemplateTask task = new TemplateTask();
        task.setProject(new Project());
        task.setExecuteBeanShellScript(false);
        task.setTemplatePath("src/test/resources/template/sourcecode/");
        task.addTemplateFiles("OslNode.ftl", "OslLink.ftl");
        final FileSet xmls = new FileSet();
        xmls.setDir(new File("src/test/resources/data/sourcecode/"));
        xmls.setIncludes("*.xml");
        task.addXmlFiles(xmls);
        task.setOutputDirectory("./target/test-data/TestNodesAndLinksGeneration/output/");
        task.execute();
        final String linkDir = "target/test-data/TestNodesAndLinksGeneration/output/bundle-processor/osl-java-bundle/src/main/java/org/openspotlight/bundle/dap/language/java/metamodel/link";
        assertThat(new File(linkDir).exists(), is(true));
        assertThat(new File(linkDir).list().length, is(not(0)));

        final String nodeDir = "target/test-data/TestNodesAndLinksGeneration/output/bundle-processor/osl-java-bundle/src/main/java/org/openspotlight/bundle/dap/language/java/metamodel/node";
        assertThat(new File(nodeDir).exists(), is(true));
        assertThat(new File(nodeDir).list().length, is(not(0)));
    }
}