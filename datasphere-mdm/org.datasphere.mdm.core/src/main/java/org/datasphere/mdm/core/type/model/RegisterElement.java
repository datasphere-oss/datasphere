package org.datasphere.mdm.core.type.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author Mikhail Mikhailov on Oct 16, 2020
 * Top level register.
 */
public interface RegisterElement extends EntityElement {
    /**
     * Gets outgoing (from this register) relations.
     *
     * @return outgoing relations map.
     */
    @Nonnull
    Map<RelationElement, EntityElement> getOutgoingRelations();
    /**
     * Gets incoming (to this register) relations.
     *
     * @return incoming relations map.
     */
    @Nonnull
    Map<RelationElement, EntityElement> getIncomingRelations();
    /**
     * Gets lookups, referenced from this register alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencedLookups();
    /**
     * Gets all nested elements referenced by this register.
     * @return collection of nested elements
     */
    @Nonnull
    Collection<NestedElement> getReferencedNesteds();
    /**
     * Attributes presentation.
     * @return presntation
     */
    Collection<RelationPresentationBlockElement> getRelationsPresentation();
    /**
     * Relations presentation.
     * @return presntation
     */
    Collection<AttributesPresentationBlockElement> getAttributesPresentation();
    /**
     * Narrows visibility of self to EME.
     * @return EME view
     */
    default EntityElement narrow() {
        return this;
    }
}
