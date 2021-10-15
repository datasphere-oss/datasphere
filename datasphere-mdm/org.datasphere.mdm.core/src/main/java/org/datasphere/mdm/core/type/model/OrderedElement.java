package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Nov 6, 2020
 * Basic ordered element.
 */
public interface OrderedElement {
    /**
     * Gets the order of the element in the record.
     * @return the order of the element
     */
    int getOrder();
}
