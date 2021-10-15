package org.datasphere.mdm.core.type.model;

import java.util.Map;

import org.datasphere.mdm.search.type.IndexField;

/**
 * Holds and returns indexing information.
 * @author Mikhail Mikhailov on Mar 1, 2020
 */
public interface IndexedElement {
    /**
     * Retruns cached index fields.
     * @return index fields
     */
    Map<String, IndexField> getIndexFields();
}
