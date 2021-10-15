package org.datasphere.mdm.core.type.model;

import java.util.Collection;

/**
 * @author Mikhail Mikhailov on Oct 19, 2020
 * A general purpose top level entities grouping capability.
 */
public interface EntitiesGroupElement extends IdentityElement, NamedDisplayableElement {
    /**
     * This group relative path.
     * @return path
     */
    String getPath();
    /**
     * Gets subscribed registers.
     * @return registers collection
     */
    Collection<RegisterElement> getRegisters();
    /**
     * Gets lookup collection.
     * @return lookups collection
     */
    Collection<LookupElement> getLookups();
    /**
     * Gets children.
     * @return children collection
     */
    Collection<EntitiesGroupElement> getChildren();
}
