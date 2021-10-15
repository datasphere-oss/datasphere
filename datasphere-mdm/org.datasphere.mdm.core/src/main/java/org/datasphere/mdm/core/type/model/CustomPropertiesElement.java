package org.datasphere.mdm.core.type.model;

import java.util.Collection;

/**
 * @author Mikhail Mikhailov on Oct 6, 2020
 */
public interface CustomPropertiesElement {
    /**
     * Gets all custom properties.
     * @return properties.
     */
    Collection<CustomPropertyElement> getCustomProperties();
    /**
     * Returns true, if the property with the name 'name' exists.
     * @param name the name
     * @return true, if exists
     */
    boolean propertyExists(String name);
    /**
     * Gets an element by name.
     * @param name the name
     * @return property or null
     */
    CustomPropertyElement getCustomProperty(String name);
}
