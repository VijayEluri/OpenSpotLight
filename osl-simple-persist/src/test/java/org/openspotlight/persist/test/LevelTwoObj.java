package org.openspotlight.persist.test;

import org.openspotlight.persist.annotation.KeyProperty;
import org.openspotlight.persist.annotation.ParentProperty;
import org.openspotlight.persist.annotation.SimpleNodeType;

public class LevelTwoObj implements SimpleNodeType {
    private String      key;

    private String      property;

    private LevelOneObj parentObj;

    private PropertyObj propertyObj;

    @KeyProperty
    public String getKey() {
        return this.key;
    }

    @ParentProperty
    public LevelOneObj getLevelOneObj() {
        return this.parentObj;
    }

    public String getProperty() {
        return this.property;
    }

    public PropertyObj getPropertyObj() {
        return this.propertyObj;
    }

    public void setKey( final String key ) {
        this.key = key;
    }

    public void setLevelOneObj( final LevelOneObj parentObj ) {
        this.parentObj = parentObj;
    }

    public void setProperty( final String property ) {
        this.property = property;
    }

    public void setPropertyObj( final PropertyObj propertyObj ) {
        this.propertyObj = propertyObj;
    }

}