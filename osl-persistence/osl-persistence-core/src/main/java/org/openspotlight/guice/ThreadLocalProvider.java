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

package org.openspotlight.guice;

import java.util.ArrayList;
import java.util.List;

import org.openspotlight.common.Disposable;

import com.google.inject.Provider;

/**
 * This is a Guice provider used for thread locals variables. It is mandatory to use the {com.google.inject.Singleton} annotation
 * on the class that extends this type. Also if this provider implementation depends on other Thread locals, inject the provider
 * instead of the instance.
 * 
 * @author feuteston
 * @author porcelli
 * @param <T> the type that will be instantiated
 */
public abstract class ThreadLocalProvider<T> implements Provider<T>, Disposable {

    private final List<Disposable> disposableItems = new ArrayList<Disposable>();

    private final ThreadLocal<T>   threadLocal     = new ThreadLocal<T>();

    /**
     * Creates a new instance of <T>
     * 
     * @return the new instance
     */
    protected abstract T createInstance();

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeResources() {
        List<Disposable> disposables;
        synchronized (disposableItems) {
            disposables = new ArrayList<Disposable>(disposableItems);
            disposableItems.clear();
        }
        for (final Disposable d: disposables) {
            d.closeResources();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        T t = threadLocal.get();
        if (t == null) {
            t = createInstance();
            threadLocal.set(t);
            if (t instanceof Disposable) {
                synchronized (disposableItems) {
                    disposableItems.add((Disposable) t);
                }
            }
        }
        return t;
    }

}
