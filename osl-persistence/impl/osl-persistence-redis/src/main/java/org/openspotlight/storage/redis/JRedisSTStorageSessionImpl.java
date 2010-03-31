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
 * ***********************************************************************
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 * *
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

package org.openspotlight.storage.redis;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.jredis.JRedis;
import org.jredis.RedisException;
import org.openspotlight.common.exception.SLException;
import org.openspotlight.storage.AbstractSTStorageSession;
import org.openspotlight.storage.STPartition;
import org.openspotlight.storage.domain.key.STKeyEntry;
import org.openspotlight.storage.domain.key.STUniqueKey;
import org.openspotlight.storage.domain.node.STNodeEntry;
import org.openspotlight.storage.domain.node.STProperty;
import org.openspotlight.storage.domain.node.STPropertyImpl;
import org.openspotlight.storage.redis.guice.JRedisFactory;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Class.forName;
import static java.text.MessageFormat.format;
import static org.jredis.ri.alphazero.support.DefaultCodec.toStr;
import static org.openspotlight.common.util.Conversion.convert;

/**
 * Created by User: feu - Date: Mar 23, 2010 - Time: 4:46:25 PM
 */
public class JRedisSTStorageSessionImpl extends AbstractSTStorageSession {

    private final String SET_WITH_ALL_KEYS = "all-unique-keys";
    private final String SET_WITH_ALL_LOCAL_KEYS = "local-keys:{0}:unique-keys";
    private final String SET_WITH_NODE_KEYS_NAMES = "node-unique-key:{0}:key-names";
    private final String SET_WITH_NODE_CHILDREN_KEYS = "node-unique-key:{0}:children-unique-keys";
    private final String SET_WITH_NODE_CHILDREN_NAMED_KEYS = "node-unique-key:{0}:node-name:{1}:children-unique-keys";
    private final String SET_WITH_NODE_PROPERTY_NAMES = "node-unique-key:{0}:property-names";
    private final String SET_WITH_PROPERTY_NODE_IDS = "property-name:{0}:property-type:{1}:property-value:{2}:node-unique-keys";
    private final String KEY_WITH_PROPERTY_VALUE = "node-unique-key:{0}:property-name:{1}:value";
    private final String KEY_WITH_PROPERTY_DESCRIPTION = "node-unique-key:{0}:property-name:{1}:description";
    private final String KEY_WITH_PROPERTY_PARAMETERIZED_1 = "node-unique-key:{0}:property-name:{1}:parameterized-1";
    private final String KEY_WITH_PROPERTY_PARAMETERIZED_2 = "node-unique-key:{0}:property-name:{1}:parameterized-2";
    private final String KEY_WITH_PROPERTY_TYPE = "node-unique-key:{0}:property-name:{1}:type";
    private final String KEY_WITH_PARENT_UNIQUE_ID = "node-unique-key:{0}:parent-unique-id";
    private final String KEY_WITH_NODE_ENTRY_NAME = "node-unique-key:{0}:node-entry-name";


    private final JRedisFactory factory;

    @Inject
    public JRedisSTStorageSessionImpl(STFlushMode flushMode, JRedisFactory factory) {
        super(flushMode);
        this.factory = factory;
    }

    @Override
    protected Set<STNodeEntry> internalFindByCriteria(STPartition partition, STCriteria criteria) throws Exception {
        List<String> inter = Lists.newLinkedList();
        for (STCriteriaItem c : criteria.getCriteriaItems()) {
            if (c instanceof STPropertyCriteriaItem) {
                STPropertyCriteriaItem p = (STPropertyCriteriaItem) c;
                inter.add(format(SET_WITH_PROPERTY_NODE_IDS, p.getPropertyName(), p.getType().getName(), convert(p.getValue(), String.class)).replaceAll(" ", ""));
            }
        }
        JRedis jredis = factory.getFrom(partition);
        List<String> ids;
        if (inter.size() > 1) {
            ids = listBytesToListString(jredis.sinter(inter.get(0), inter.subList(1, inter.size()).toArray(new String[0])));
        } else if (inter.size() == 1) {
            ids = listBytesToListString(jredis.sinter(inter.get(0)));

        } else {
            ids = Collections.emptyList();
        }
        Set<STNodeEntry> nodeEntries = newHashSet();
        for (String id : ids) {
            String parentKey = id;
            STNodeEntry nodeEntry = loadNode(parentKey, criteria.getPartition());
            nodeEntries.add(nodeEntry);
        }

        return ImmutableSortedSet.copyOf(nodeEntries);
    }

    private STNodeEntry loadNode(String parentKey, STPartition partition) throws Exception {
        STUniqueKeyBuilder keyBuilder = null;
        do {
            JRedis jredis = factory.getFrom(partition);

            List<String> keyPropertyNames = listBytesToListString(jredis.smembers(format(SET_WITH_NODE_KEYS_NAMES, parentKey)));
            String nodeEntryName = toStr(jredis.get(format(KEY_WITH_NODE_ENTRY_NAME, parentKey)));
            keyBuilder = keyBuilder == null ? withPartition(partition).createKey(nodeEntryName) : keyBuilder.withParent(nodeEntryName);

            for (String keyName : keyPropertyNames) {
                String typeName = toStr(jredis.get(format(KEY_WITH_PROPERTY_TYPE, parentKey, keyName)));
                String typeValueAsString = toStr(jredis.get(format(KEY_WITH_PROPERTY_VALUE, parentKey, keyName)));
                Class<? extends Serializable> type = (Class<? extends Serializable>) forName(typeName);
                Serializable value = convert(typeValueAsString, type);
                keyBuilder.withEntry(keyName, type, value);
            }
            parentKey = toStr(jredis.get(format(KEY_WITH_PARENT_UNIQUE_ID, parentKey)));

        } while (parentKey != null);
        STUniqueKey uniqueKey = keyBuilder.andCreate();
        STNodeEntry nodeEntry = createEntryWithKey(uniqueKey);
        return nodeEntry;
    }

    private List<String> listBytesToListString(List<byte[]> ids) {
        List<String> idsAsString = newLinkedList();
        if (ids != null) {
            for (byte[] b : ids) {
                String s = toStr(b);
                idsAsString.add(s);
            }
        }
        return idsAsString;
    }

    @Override
    protected void flushNewItem(STPartition partition, STNodeEntry entry) throws Exception {
        JRedis jredis = factory.getFrom(partition);

        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(entry.getUniqueKey());
        jredis.sadd(SET_WITH_ALL_KEYS, uniqueKey);
        jredis.set(format(KEY_WITH_NODE_ENTRY_NAME, uniqueKey), entry.getNodeEntryName());
        STUniqueKey parentKey = entry.getUniqueKey().getParentKey();
        if (parentKey != null) {
            String parentAsString = supportMethods.getUniqueKeyAsStringHash(parentKey);
            jredis.set(format(KEY_WITH_PARENT_UNIQUE_ID, uniqueKey), parentAsString);
            jredis.sadd(format(SET_WITH_NODE_CHILDREN_KEYS, parentAsString), uniqueKey);
            jredis.sadd(format(SET_WITH_NODE_CHILDREN_NAMED_KEYS, parentAsString, entry.getNodeEntryName()), uniqueKey);
        }
        String localKey = supportMethods.getLocalKeyAsStringHash(entry.getLocalKey());
        jredis.sadd(format(SET_WITH_ALL_LOCAL_KEYS, localKey), uniqueKey);

        for (STKeyEntry<?> k : entry.getLocalKey().getEntries()) {
            jredis.sadd(format(SET_WITH_NODE_KEYS_NAMES, uniqueKey), k.getPropertyName());
            jredis.sadd(format(SET_WITH_NODE_PROPERTY_NAMES, uniqueKey), k.getPropertyName());
            jredis.sadd(format(SET_WITH_PROPERTY_NODE_IDS, k.getPropertyName(), k.getType().getName(),
                    convert(k.getValue(), String.class)).replaceAll(" ", ""), uniqueKey);
            jredis.set(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, k.getPropertyName()), k.getType().getName());
            jredis.set(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, k.getPropertyName()), convert(k.getValue(), String.class));
            jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, k.getPropertyName()), convert(STProperty.STPropertyDescription.KEY, String.class));
        }

    }

    @Override
    protected void flushRemovedItem(STPartition partition, STNodeEntry entry) throws Exception {
        JRedis jredis = factory.getFrom(partition);

        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(entry.getUniqueKey());
        jredis.srem(SET_WITH_ALL_KEYS, uniqueKey);

        String localKey = supportMethods.getLocalKeyAsStringHash(entry.getLocalKey());
        jredis.srem(format(SET_WITH_ALL_LOCAL_KEYS, localKey), uniqueKey);

        for (STKeyEntry<?> k : entry.getLocalKey().getEntries()) {
            jredis.del(format(SET_WITH_NODE_KEYS_NAMES, uniqueKey));
            jredis.del(format(SET_WITH_NODE_PROPERTY_NAMES, uniqueKey));
            jredis.srem(format(SET_WITH_PROPERTY_NODE_IDS, k.getPropertyName(), k.getType().getName(),
                    convert(k.getValue(), String.class)).replaceAll(" ", ""), uniqueKey);
            jredis.del(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, k.getPropertyName()));
            jredis.del(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, k.getPropertyName()));
        }
    }

    @Override
    protected Set<STNodeEntry> internalNodeEntryGetNamedChildren(STPartition partition, STNodeEntry stNodeEntry, String name) throws Exception {
        JRedis jredis = factory.getFrom(partition);

        String parentKey = supportMethods.getUniqueKeyAsStringHash(stNodeEntry.getUniqueKey());
        String keyName = name == null ? format(SET_WITH_NODE_CHILDREN_KEYS, parentKey) : format(SET_WITH_NODE_CHILDREN_NAMED_KEYS, parentKey, name);
        List<String> childrenKeys = listBytesToListString(jredis.smembers(keyName));
        ImmutableSet.Builder<STNodeEntry> builder = ImmutableSet.builder();
        for (String id : childrenKeys) {
            builder.add(loadNode(id, stNodeEntry.getUniqueKey().getPartition()));
        }
        return builder.build();
    }

    @Override
    protected boolean internalHasSavedProperty(STPartition partition, STProperty stProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(stProperty.getParent().getUniqueKey());
        JRedis jredis = factory.getFrom(partition);

        return jredis.exists(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, stProperty.getPropertyName()));

    }

    @Override
    protected Class<?> internalPropertyDiscoverType(STPartition partition, STProperty stProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(stProperty.getParent().getUniqueKey());
        JRedis jredis = factory.getFrom(partition);

        String typeName = toStr(jredis.get(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, stProperty.getPropertyName())));

        Class<?> type = forName(typeName);
        return type;


    }

    @Override
    protected Set<STNodeEntry> internalNodeEntryGetChildren(STPartition partition, STNodeEntry stNodeEntry) throws Exception {
        return internalNodeEntryGetNamedChildren(partition, stNodeEntry, null);
    }

    @Override
    protected STNodeEntry internalNodeEntryGetParent(STPartition partition, STNodeEntry stNodeEntry) throws Exception {
        STUniqueKey parentKey = stNodeEntry.getUniqueKey().getParentKey();
        if (parentKey == null) return null;
        return createEntryWithKey(parentKey);
    }

    @Override
    protected <T> T internalPropertyGetKeyPropertyAs(STPartition partition, STProperty stProperty, Class<T> type) throws Exception {
        return this.<T>internalPropertyGetSimplePropertyAs(partition, stProperty, type);
    }

    @Override
    protected InputStream internalPropertyGetInputStreamProperty(STPartition partition, STProperty stProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(stProperty.getParent().getUniqueKey());
        JRedis jredis = factory.getFrom(partition);

        byte[] fromStorage = jredis.get(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, stProperty.getPropertyName()));

        return new ByteArrayInputStream(fromStorage);
    }

    @Override
    protected <T> T internalPropertyGetSerializedPojoPropertyAs(STPartition partition, STProperty stProperty, Class<T> type) throws Exception {
        InputStream inputStream = internalPropertyGetInputStreamProperty(partition, stProperty);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (T) objectInputStream.readObject();

    }

    @Override
    protected <T> T internalPropertyGetSerializedMapPropertyAs(STPartition partition, STProperty stProperty, Class<T> type) throws Exception {
        return internalPropertyGetSerializedPojoPropertyAs(partition, stProperty, type);
    }

    @Override
    protected <T> T internalPropertyGetSerializedSetPropertyAs(STPartition partition, STProperty stProperty, Class<T> type) throws Exception {
        return internalPropertyGetSerializedPojoPropertyAs(partition, stProperty, type);
    }

    @Override
    protected <T> T internalPropertyGetSerializedListPropertyAs(STPartition partition, STProperty stProperty, Class<T> type) throws Exception {
        return internalPropertyGetSerializedPojoPropertyAs(partition, stProperty, type);
    }

    @Override
    protected <T> T internalPropertyGetSimplePropertyAs(STPartition partition, STProperty stProperty, Class<T> type) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(stProperty.getParent().getUniqueKey());
        JRedis jredis = factory.getFrom(partition);

        String typeValueAsString = toStr(jredis.get(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, stProperty.getPropertyName())));
        T value = convert(typeValueAsString, type);
        return value;
    }

    @Override
    protected Set<STProperty> internalNodeEntryLoadProperties(STPartition partition, STNodeEntry stNodeEntry) throws Exception {
        JRedis jredis = factory.getFrom(partition);

        String parentKey = supportMethods.getUniqueKeyAsStringHash(stNodeEntry.getUniqueKey());
        List<String> propertyNames = listBytesToListString(jredis.smembers(format(SET_WITH_NODE_PROPERTY_NAMES, parentKey)));
        Set<STProperty> result = newHashSet();
        for (String propertyName : propertyNames) {
            STProperty property = loadProperty(partition, stNodeEntry, parentKey, propertyName);

            result.add(property);

        }

        return result;
    }

    private STProperty loadProperty(STPartition partition, STNodeEntry stNodeEntry, String parentKey, String propertyName) throws RedisException, ClassNotFoundException, SLException {
        JRedis jredis = factory.getFrom(partition);

        String typeName = toStr(jredis.get(format(KEY_WITH_PROPERTY_TYPE, parentKey, propertyName)));
        String descriptionAsString = toStr(jredis.get(format(KEY_WITH_PROPERTY_DESCRIPTION, parentKey, propertyName)));
        STProperty.STPropertyDescription description = STProperty.STPropertyDescription.valueOf(descriptionAsString);
        Class<? extends Serializable> type = (Class<? extends Serializable>) forName(typeName);

        Class<?> parameterized1 = null;
        Class<?> parameterized2 = null;
        if (jredis.exists(format(KEY_WITH_PROPERTY_PARAMETERIZED_1, parentKey, propertyName))) {
            String typeName1 = toStr(jredis.get(format(KEY_WITH_PROPERTY_PARAMETERIZED_1, parentKey, propertyName)));
            parameterized1 = forName(typeName1);

        }
        if (jredis.exists(format(KEY_WITH_PROPERTY_PARAMETERIZED_2, parentKey, propertyName))) {
            String typeName2 = toStr(jredis.get(format(KEY_WITH_PROPERTY_PARAMETERIZED_2, parentKey, propertyName)));
            parameterized2 = forName(typeName2);

        }

        STProperty property = new STPropertyImpl(stNodeEntry, propertyName, description, type, parameterized1, parameterized2);
        if (description.getLoadWeight().equals(STProperty.STPropertyDescription.STLoadWeight.EASY)) {
            String typeValueAsString = toStr(jredis.get(format(KEY_WITH_PROPERTY_VALUE, parentKey, propertyName)));
            Serializable value = convert(typeValueAsString, type);
            property.getInternalMethods().setValueOnLoad(value);
        }
        return property;
    }

    @Override
    protected void internalFlushInputStreamProperty(STPartition partition, STProperty dirtyProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(dirtyProperty.getParent().getUniqueKey());

        InputStream valueAsStream = dirtyProperty.getInternalMethods().<InputStream>getTransientValue();
        if (valueAsStream.markSupported()) {
            valueAsStream.reset();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(valueAsStream, outputStream);
        JRedis jredis = factory.getFrom(partition);

        jredis.sadd(format(SET_WITH_NODE_PROPERTY_NAMES, uniqueKey), dirtyProperty.getPropertyName());

        jredis.set(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getPropertyType().getName());
        jredis.set(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, dirtyProperty.getPropertyName()), outputStream.toByteArray());
        jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, dirtyProperty.getPropertyName()), convert(STProperty.STPropertyDescription.INPUT_STREAM, String.class));
    }

    @Override
    protected void internalFlushSerializedPojoProperty(STPartition partition, STProperty dirtyProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(dirtyProperty.getParent().getUniqueKey());
        flushStream(partition, dirtyProperty, uniqueKey);
        JRedis jredis = factory.getFrom(partition);

        jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, dirtyProperty.getPropertyName()), convert(STProperty.STPropertyDescription.SERIALIZED_POJO, String.class));
    }

    private void flushStream(STPartition partition, STProperty dirtyProperty, String uniqueKey) throws IOException, RedisException {
        Serializable value = dirtyProperty.getInternalMethods().<Serializable>getTransientValue();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(value);
        objectOutputStream.flush();
        JRedis jredis = factory.getFrom(partition);

        jredis.sadd(format(SET_WITH_NODE_PROPERTY_NAMES, uniqueKey), dirtyProperty.getPropertyName());

        jredis.set(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getPropertyType().getName());
        jredis.set(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, dirtyProperty.getPropertyName()), byteArrayOutputStream.toByteArray());
    }

    @Override
    protected void internalFlushSerializedMapProperty(STPartition partition, STProperty dirtyProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(dirtyProperty.getParent().getUniqueKey());
        flushStream(partition, dirtyProperty, uniqueKey);
        JRedis jredis = factory.getFrom(partition);
        jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, dirtyProperty.getPropertyName()), convert(STProperty.STPropertyDescription.SERIALIZED_SET, String.class));
        jredis.set(format(KEY_WITH_PROPERTY_PARAMETERIZED_1, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getFirstParameterizedType().getName());
        jredis.set(format(KEY_WITH_PROPERTY_PARAMETERIZED_2, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getSecondParameterizedType().getName());
    }

    @Override
    protected void internalFlushSerializedSetProperty(STPartition partition, STProperty dirtyProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(dirtyProperty.getParent().getUniqueKey());
        flushStream(partition, dirtyProperty, uniqueKey);
        JRedis jredis = factory.getFrom(partition);
        jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, dirtyProperty.getPropertyName()), convert(STProperty.STPropertyDescription.SERIALIZED_SET, String.class));
        jredis.set(format(KEY_WITH_PROPERTY_PARAMETERIZED_1, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getFirstParameterizedType().getName());
    }

    @Override
    protected void internalFlushSerializedListProperty(STPartition partition, STProperty dirtyProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(dirtyProperty.getParent().getUniqueKey());
        flushStream(partition, dirtyProperty, uniqueKey);
        JRedis jredis = factory.getFrom(partition);
        jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, dirtyProperty.getPropertyName()), convert(STProperty.STPropertyDescription.SERIALIZED_LIST, String.class));
        jredis.set(format(KEY_WITH_PROPERTY_PARAMETERIZED_1, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getFirstParameterizedType().getName());
    }

    @Override
    protected void internalFlushSimpleProperty(STPartition partition, STProperty dirtyProperty) throws Exception {
        String uniqueKey = supportMethods.getUniqueKeyAsStringHash(dirtyProperty.getParent().getUniqueKey());

        String transientValueAsString = convert(dirtyProperty.getInternalMethods().<Object>getTransientValue(), String.class);

        JRedis jredis = factory.getFrom(partition);
        jredis.sadd(format(SET_WITH_NODE_PROPERTY_NAMES, uniqueKey), dirtyProperty.getPropertyName());
        jredis.sadd(format(SET_WITH_PROPERTY_NODE_IDS, dirtyProperty.getPropertyName(), dirtyProperty.getInternalMethods().<Object>getPropertyType().getName(),
                convert(dirtyProperty.getValue(this), String.class)).replaceAll(" ", ""), uniqueKey);

        jredis.set(format(KEY_WITH_PROPERTY_TYPE, uniqueKey, dirtyProperty.getPropertyName()), dirtyProperty.getInternalMethods().<Object>getPropertyType().getName());
        jredis.set(format(KEY_WITH_PROPERTY_VALUE, uniqueKey, dirtyProperty.getPropertyName()), transientValueAsString);
        jredis.set(format(KEY_WITH_PROPERTY_DESCRIPTION, uniqueKey, dirtyProperty.getPropertyName()), convert(STProperty.STPropertyDescription.SIMPLE, String.class));

    }
}
