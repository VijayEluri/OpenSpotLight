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

package org.openspotlight.common.util;

import static org.openspotlight.common.util.Assertions.checkCondition;
import static org.openspotlight.common.util.Assertions.checkNotEmpty;
import static org.openspotlight.common.util.Assertions.checkNotNull;
import static org.openspotlight.common.util.Exceptions.logAndReturnNew;
import static org.openspotlight.common.util.Exceptions.logAndThrowNew;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.openspotlight.common.exception.SLException;
import org.openspotlight.common.exception.SLRuntimeException;

/**
 * Helper class to deal with multiple files.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * @author Vitor Hugo Chagas
 */
public class Files {

    /**
     * Delete multiple files.
     * 
     * @param path the path
     * @throws SLException the SL exception
     */
    public static void delete( final File path ) throws SLException {
        checkNotNull("path", path); //$NON-NLS-1$
        if (!path.exists()) {
            return;
        }
        try {
            if (path.isFile()) {
                deleteFile(path);
            } else {
                deleteDir(path);
            }
        } catch (final Exception e) {
            logAndThrowNew(e, SLException.class);
        }
    }

    /**
     * Delete multiple files.
     * 
     * @param path the path
     * @throws SLException the SL exception
     */
    public static void delete( final String path ) throws SLException {
        checkNotEmpty("path", path); //$NON-NLS-1$
        delete(new File(path));

    }

    /**
     * Deletes directory in a recursive way, first excluding its contents.
     * 
     * @param dir the dir
     * @throws Exception the exception
     */
    private static void deleteDir( final File dir ) throws Exception {
        checkNotNull("dir", dir); //$NON-NLS-1$
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isFile()) {
                    deleteFile(f);
                } else {
                    deleteDir(f);
                }
            }
        }
        dir.delete();
    }

    /**
     * Delete a file itself.
     * 
     * @param file the file
     */
    private static void deleteFile( final File file ) {
        checkNotNull("file", file); //$NON-NLS-1$
        file.delete();
    }

    /**
     * Returns the normalized path (in a unix like way).
     * 
     * @param f the f
     * @return a normalized file name
     */
    public static String getNormalizedFileName( final File f ) {
        try {
            return f.getCanonicalPath().replaceAll("\\\\", "/"); //$NON-NLS-1$//$NON-NLS-2$
        } catch (final Exception e) {
            throw logAndReturnNew(e, SLRuntimeException.class);
        }
    }

    /**
     * Execute the file listing using recursion to fill the file name set.
     * 
     * @param setOfFiles the set of files
     * @param basePath the base path
     * @param file the file
     * @throws Exception the exception
     */
    private static void listFileNamesFrom( final Set<String> setOfFiles,
                                           final String basePath,
                                           final File file ) throws Exception {
        checkNotNull("setOfFiles", setOfFiles); //$NON-NLS-1$
        checkNotEmpty("basePath", basePath); //$NON-NLS-1$
        checkNotNull("file", file); //$NON-NLS-1$
        if (file.isFile()) {
            setOfFiles.add(getNormalizedFileName(file));
        } else if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (final File f : files) {
                    listFileNamesFrom(setOfFiles, basePath, f);
                }
            }
        }
    }

    /**
     * Returns a relative path list from an initial directory, or the file path itself if the initialPath is a file.
     * 
     * @param basePath the base path
     * @return a relative path list
     * @throws SLException the SL exception
     */
    public static Set<String> listFileNamesFrom( final String basePath,
                                                 final boolean silent ) throws SLException {
        checkNotEmpty("basePath", basePath); //$NON-NLS-1$
        final File basePathAsFile = new File(basePath);
        if (!silent) {
            checkCondition(
                           "basePathExists:" + basePath, basePathAsFile.exists()); //$NON-NLS-1$
            checkCondition("basePathIsDirectory", basePathAsFile.isDirectory()); //$NON-NLS-1$
        }
        try {
            String normalizedBasePath = getNormalizedFileName(basePathAsFile);
            if (!normalizedBasePath.endsWith("/")) { //$NON-NLS-1$
                normalizedBasePath = normalizedBasePath + "/"; //$NON-NLS-1$
            }
            final Set<String> result = new HashSet<String>();
            final File initial = new File(normalizedBasePath);
            listFileNamesFrom(result, normalizedBasePath, initial);
            return result;
        } catch (final Exception e) {
            throw logAndReturnNew(e, SLException.class);
        }
    }

    /**
     * Reads an streams content and writes it on a byte array.
     * 
     * @param inputStream the input stream
     * @return the stream content as bytes
     * @throws SLException the SL exception
     */
    public static byte[] readBytesFromStream( final InputStream inputStream )
            throws SLException {
        checkNotNull("inputStream", inputStream); //$NON-NLS-1$
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (inputStream.available() > 0) {
                baos.write(inputStream.read());
            }
            final byte[] content = baos.toByteArray();
            return content;
        } catch (final Exception e) {
            throw logAndReturnNew(e, SLException.class);
        }
    }

    /**
     * Reads text file lines.
     * 
     * @param inputStream
     * @return collection of string lines.
     * @throws SLException
     */
    public static Collection<String> readLines( final InputStream inputStream )
            throws SLException {
        checkNotNull("inputStream", inputStream);
        final Collection<String> lines = new ArrayList<String>();
        final FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            String line = null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (final IOException e) {
            throw logAndReturnNew(e, SLException.class);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (final IOException e) {
            }
        }
        return lines;
    }

    /**
     * Should not be instantiated.
     */
    private Files() {
        throw new IllegalStateException(Messages
                                                .getString("invalidConstructor")); //$NON-NLS-1$
    }

}
