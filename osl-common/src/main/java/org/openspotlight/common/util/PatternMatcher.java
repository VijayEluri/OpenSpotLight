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


package org.openspotlight.common.util;

import static java.util.Collections.unmodifiableSet;
import static org.apache.tools.ant.types.selectors.SelectorUtils.match;
import static org.openspotlight.common.util.Assertions.checkNotNull;
import static org.openspotlight.common.util.Exceptions.logAndThrow;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.openspotlight.common.MutableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc

/**
 * This class has the necessary logic to filter names using the apache ant pattern.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
public class PatternMatcher {

    /**
     * Result class with the results of a filter matching using the Ant expression syntax.
     * 
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     */
    public static final class FilterResult implements Serializable {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -6700182758852743670L;

        /**
         * The all names.
         */
        private final Set<String> allNames;

        /**
         * The excluded names.
         */
        private final Set<String> excludedNames;

        /**
         * The ignored names.
         */
        private final Set<String> ignoredNames;

        /**
         * The included names.
         */
        private final Set<String> includedNames;

        /**
         * Creates a empty filterResult.
         * 
         * @param type the type
         */
        public FilterResult(
                             final MutableType type) {
            this(new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), type);
        }

        /**
         * Immutable result.
         * 
         * @param allNames the all names
         * @param includedNames the included names
         * @param excludedNames the excluded names
         * @param ignoredNames the ignored names
         */
        public FilterResult(
                             final Set<String> allNames, final Set<String> includedNames, final Set<String> excludedNames,
                             final Set<String> ignoredNames) {
            this(allNames, includedNames, excludedNames, ignoredNames, MutableType.IMMUTABLE);
        }

        /**
         * Mutability optional on this {@link Constructor}.
         * 
         * @param allNames the all names
         * @param includedNames the included names
         * @param excludedNames the excluded names
         * @param ignoredNames the ignored names
         * @param type the type
         */
        public FilterResult(
                             final Set<String> allNames, final Set<String> includedNames, final Set<String> excludedNames,
                             final Set<String> ignoredNames, final MutableType type) {
            if (MutableType.IMMUTABLE.equals(type)) {
                this.allNames = unmodifiableSet(allNames);
                this.includedNames = unmodifiableSet(includedNames);
                this.excludedNames = unmodifiableSet(excludedNames);
                this.ignoredNames = unmodifiableSet(ignoredNames);
            } else {
                this.allNames = allNames;
                this.includedNames = includedNames;
                this.excludedNames = excludedNames;
                this.ignoredNames = ignoredNames;
            }
        }

        /**
         * Gets the all names.
         * 
         * @return all names passed to be processed
         */
        public Set<String> getAllNames() {
            return allNames;
        }

        /**
         * Gets the excluded names.
         * 
         * @return a set of excluded names
         */
        public Set<String> getExcludedNames() {
            return excludedNames;
        }

        /**
         * Gets the ignored names.
         * 
         * @return a set of ignored names
         */
        public Set<String> getIgnoredNames() {
            return ignoredNames;
        }

        /**
         * Gets the included names.
         * 
         * @return a set of included names
         */
        public Set<String> getIncludedNames() {
            return includedNames;
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(PatternMatcher.class);

    /**
     * Filter the names using the Apache Ant expression syntax (and also the {@link SelectorUtils} class.
     * 
     * @param namesToFilter the names to filter
     * @param includedPatterns the included patterns
     * @param excludedPatterns the excluded patterns
     * @param caseSensitive the case sensitive
     * @return a filter result
     */
    public static FilterResult filterNamesByPattern(final String rootPattern,
                                                     final Set<String> namesToFilter,
                                                     final Set<String> includedPatterns,
                                                     final Set<String> excludedPatterns,
                                                     final boolean caseSensitive) {
        checkNotNull("namesToFilter", namesToFilter); //$NON-NLS-1$
        checkNotNull("includedPatterns", includedPatterns); //$NON-NLS-1$
        checkNotNull("excludedPatterns", excludedPatterns); //$NON-NLS-1$

        final Set<String> includedNames = new HashSet<String>();
        final Set<String> excludedNames = new HashSet<String>();

        for (final String included: includedPatterns) {
            for (final String nameToFilter: namesToFilter) {
                if (match(Strings.concatPaths(rootPattern, included), nameToFilter, caseSensitive)) {
                    includedNames.add(nameToFilter);
                }
            }
        }
        for (final String excluded: excludedPatterns) {
            for (final String nameToFilter: namesToFilter) {
                if (match(Strings.concatPaths(rootPattern, excluded), nameToFilter, caseSensitive)) {
                    excludedNames.add(nameToFilter);
                    includedNames.remove(nameToFilter);
                }
            }
        }
        final Set<String> ignoredNames = new HashSet<String>(namesToFilter);
        ignoredNames.removeAll(includedNames);
        ignoredNames.removeAll(excludedNames);

        if (logger.isDebugEnabled()) {
            final String prefix = "root " + rootPattern + " with parameters included=" + includedPatterns.toString()
                                  + " and excluded=" + excludedPatterns;
            logger.debug(prefix + " filtering names " + Strings.bigCollectionsToString(namesToFilter));
            logger.debug(prefix + " accepting names " + Strings.bigCollectionsToString(includedNames));
            logger.debug(prefix + " Ignoring names " + Strings.bigCollectionsToString(ignoredNames));
            logger.debug(prefix + " Excluding names " + Strings.bigCollectionsToString(excludedNames));
        }

        final FilterResult result = new FilterResult(namesToFilter, includedNames, excludedNames, ignoredNames);
        return result;
    }

    /**
     * Verify if the first parameter matches the pattern passed on second parameter in a non case sensitivness way.
     * 
     * @param nameToMatch the name to match
     * @param patternToMatch the pattern to match
     * @return true if nameToMatch matches patternToMatch
     */
    public static boolean isMatchingWithoutCaseSentitiveness(final String nameToMatch,
                                                              final String patternToMatch) {
        return match(patternToMatch, nameToMatch, false);
    }

    /**
     * Should not be instantiated.
     */
    private PatternMatcher() {
        logAndThrow(new IllegalStateException(Messages.getString("invalidConstructor"))); //$NON-NLS-1$
    }

}
