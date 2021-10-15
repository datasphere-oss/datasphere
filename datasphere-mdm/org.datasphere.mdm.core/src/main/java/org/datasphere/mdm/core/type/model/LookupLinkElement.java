package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Oct 19, 2020
 * Lookup link element info.
 */
public interface LookupLinkElement {
    /**
     * Gets the presentation info for this lookup link.
     * @return presentation info
     */
    ReferencePresentationElement getPresentation();
    /**
     * gets the name of the referenced lookup element.
     * @return lookup name
     */
    String getLookupLinkName();
}
