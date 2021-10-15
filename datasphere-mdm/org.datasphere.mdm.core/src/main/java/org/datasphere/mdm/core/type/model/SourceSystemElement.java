package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Oct 6, 2020
 * Source system definition.
 */
public interface SourceSystemElement extends IdentityElement, NamedDisplayableElement, CustomPropertiesElement {
    /**
     * Gets the weight.
     * @return weight
     */
    int getWeight();
    /**
     * True for admin source system .
     * @return true for admin source system
     */
    boolean isAdmin();
}
