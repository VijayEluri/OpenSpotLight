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

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.junit.Test;
import org.openspotlight.bundle.dap.language.java.asm.tool.CompiledTypesExtractorTask;

public class XmlGenerationAndBeanShellProcessingIntegratedTest {

    @Test
    public void shouldCreateBeanShellScript() throws Exception {
        final CompiledTypesExtractorTask task = new CompiledTypesExtractorTask();
        task.setProject(new Project());

        final FileSet jreFileSet = new FileSet();
        jreFileSet.setDir(new File("."));
        jreFileSet.setIncludes("**/dynamo*.jar");
        //        jreFileSet.setIncludes("**/*.class");
        task.addCompiledArtifacts(jreFileSet);
        task.setContextName("Dynamo");
        task.setContextVersion("1.0.1");
        task.setXmlOutputFileName("./target/test-data/TestBeanShellScriptGenerationAndExecution/small-result.xml");

        task.execute();
        final TemplateTask anotherTask = new TemplateTask();
        anotherTask.setProject(new Project());
        anotherTask.setExecuteBeanShellScript(true);
        anotherTask.setTemplatePath("src/test/resources/template/beanshell/");
        anotherTask.addTemplateFiles("JavaInitialData.ftl");
        final FileSet xmls = new FileSet();
        xmls.setDir(new File("target/test-data/TestBeanShellScriptGenerationAndExecution/"));
        xmls.setIncludes("small*.xml");
        anotherTask.addXmlFiles(xmls);
        anotherTask.setOutputDirectory("./target/test-data/TestBeanShellScriptGenerationAndExecution/output/");
        anotherTask.execute();
        //        assertThat(new File(linkDir).exists(), is(true));
        //        assertThat(new File(linkDir).list().length, is(not(0)));
    }
}
