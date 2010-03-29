package org.openspotlight.storage.domain.node;

import org.openspotlight.storage.STStorageSession;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: feuteston
 * Date: 29/03/2010
 * Time: 08:49:51
 * To change this template use File | Settings | File Templates.
 */
public class STPropertyImpl implements STProperty {
    public STPropertyImpl(STNodeEntry parent, String propertyName, STPropertyDescription description,
                          Class<?> propertyType, Class<?> firstParameterizedType,
                          Class<?> secondParameterizedType, boolean key) {
        this.parent = parent;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.firstParameterizedType = firstParameterizedType;
        this.secondParameterizedType = secondParameterizedType;
        this.key = key;
        hasParameterizedTypes = true;
        serialized = description.getSerialized().equals(STPropertyDescription.STSerializedType.SERIALIZED);
        difficultToLoad = description.getLoadWeight().equals(STPropertyDescription.STLoadWeight.DIFFICULT);
        this.description = description;
    }

    public STPropertyImpl(STNodeEntry parent, String propertyName, STPropertyDescription description,
                          Class<?> propertyType, Class<?> firstParameterizedType, boolean key) {
        this.parent = parent;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.firstParameterizedType = firstParameterizedType;
        this.key = key;
        this.secondParameterizedType = null;
        hasParameterizedTypes = true;
        serialized = description.getSerialized().equals(STPropertyDescription.STSerializedType.SERIALIZED);
        difficultToLoad = description.getLoadWeight().equals(STPropertyDescription.STLoadWeight.DIFFICULT);
        this.description = description;
    }

    public STPropertyImpl(STNodeEntry parent, String propertyName, STPropertyDescription description,
                          Class<?> propertyType, boolean key) {
        this.parent = parent;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.key = key;
        this.firstParameterizedType = null;
        this.secondParameterizedType = null;
        this.hasParameterizedTypes = false;
        serialized = description.getSerialized().equals(STPropertyDescription.STSerializedType.SERIALIZED);
        difficultToLoad = description.getLoadWeight().equals(STPropertyDescription.STLoadWeight.DIFFICULT);
        this.description = description;
    }


    private final STNodeEntry parent;

    private final String propertyName;

    private final Class<?> propertyType;

    private final Class<?> firstParameterizedType;

    private final Class<?> secondParameterizedType;

    private final boolean hasParameterizedTypes;

    private final boolean serialized;

    private final boolean difficultToLoad;

    private final boolean key;

    private final STPropertyDescription description;

    private WeakReference<?> weakReference;

    private Object value;

    public <T> void setValue(STStorageSession session, T value) {
        if (key) throw new IllegalStateException("key properties are immutable");

        session.getInternalMethods().propertySetProperty(this, value);
        if(isDifficultToLoad()){
            weakReference = new WeakReference<T>(value);
        }else{
            this.value = value;
        }
    }

    public <T, R> R getValueAs(STStorageSession session, Class<T> type) {
        R result;
        if (isDifficultToLoad()) {
            result = (R) weakReference != null ? (R) weakReference.get() : null;
            if (result == null) {
                result = (R) session.getInternalMethods().propertyGetPropertyAs(this, type);
                if (result != null) {
                    weakReference = new WeakReference<T>((T) result);
                }
            }
            return result;
        } else {
            if (value == null) value = (R) session.getInternalMethods().propertyGetPropertyAs(this, type);
            result = (R) value;
        }
        return result;
    }

    public STNodeEntry getParent() {
        return parent;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isKey() {
        return key;
    }

    public <T> Class<T> getPropertyType() {
        return (Class<T>) propertyType;
    }

    public <T> Class<T> getFirstParameterizedType() {
        return (Class<T>) firstParameterizedType;
    }

    public <T> Class<T> getSecondParameterizedType() {
        return (Class<T>) secondParameterizedType;
    }

    public boolean hasParameterizedTypes() {
        return hasParameterizedTypes;
    }

    public boolean isSerialized() {
        return serialized;
    }

    public boolean isDifficultToLoad() {
        return difficultToLoad;
    }

    public STPropertyDescription getDescription() {
        return description;
    }

}
