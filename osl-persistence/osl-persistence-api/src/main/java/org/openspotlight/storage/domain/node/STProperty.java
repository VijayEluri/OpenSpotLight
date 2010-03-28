package org.openspotlight.storage.domain.node;

import org.openspotlight.storage.STStorageSession;

/**
 * Created by IntelliJ IDEA.
 * User: feuteston
 * Date: 28/03/2010
 * Time: 10:27:26
 * To change this template use File | Settings | File Templates.
 */
public interface STProperty {
    
    STNodeEntry getParent();
    
    <T> void setValue(STStorageSession session, T value);
    
    String getPropertyName();

    <T> Class<T> getPropertyType();

    <T> Class<T> getFirstParameterizedType();

    <T> Class<T> getFirstSecondType();

    boolean isSerialized();

    boolean isDifficultToLoad();

    <T> T getValueAs(STStorageSession session, Class<T> type);

    STPropertyDescription getDescription();

    enum STPropertyDescription {
        SIMPLE(STSerializedType.NOT_SERIALIZED, STLoadWeight.EASY),
        LIST(STSerializedType.NOT_SERIALIZED, STLoadWeight.DIFFICULT),
        SET(STSerializedType.NOT_SERIALIZED, STLoadWeight.DIFFICULT),
        MAP(STSerializedType.NOT_SERIALIZED, STLoadWeight.DIFFICULT),
        SERIALIZED_LIST(STSerializedType.SERIALIZED, STLoadWeight.DIFFICULT),
        SERIALIZED_SET(STSerializedType.SERIALIZED, STLoadWeight.DIFFICULT),
        SERIALIZED_MAP(STSerializedType.SERIALIZED, STLoadWeight.DIFFICULT),
        SERIALIZED_POJO(STSerializedType.SERIALIZED, STLoadWeight.DIFFICULT),
        INPUT_STREAM(STSerializedType.SERIALIZED, STLoadWeight.DIFFICULT)
                        ;

        private final STSerializedType serialized;
        private final STLoadWeight loadWeight;

        STPropertyDescription(STSerializedType serialized, STLoadWeight loadWeight) {
            this.serialized = serialized;
            this.loadWeight = loadWeight;
        }

        enum STSerializedType {
            SERIALIZED, NOT_SERIALIZED
        }

        enum STLoadWeight {
            EASY, DIFFICULT
        }

        STSerializedType getSerialized() {
            return serialized;
        }

        STLoadWeight getLoadWeight() {
            return loadWeight;
        }
    }


}
