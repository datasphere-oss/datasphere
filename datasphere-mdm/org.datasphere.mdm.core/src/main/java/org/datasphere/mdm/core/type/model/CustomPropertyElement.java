package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Oct 6, 2020
 * A simple custom property model element.
 */
public interface CustomPropertyElement {
    /**
     * Gets the name of the property.
     * @return name
     */
    String getName();
    /**
     * Gets the value of the property.
     * @return value
     */
    String getValue();
}
