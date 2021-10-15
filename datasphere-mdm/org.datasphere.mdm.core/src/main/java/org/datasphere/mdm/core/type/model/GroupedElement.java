package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Oct 21, 2020
 */
public interface GroupedElement {
    /**
     * Gets assigned group name.
     * @return group name or null, if unassigned
     */
    String getGroupName();
}
