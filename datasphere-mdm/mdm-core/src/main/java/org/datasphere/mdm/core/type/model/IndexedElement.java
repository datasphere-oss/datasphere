package com.huahui.datasphere.mdm.core.type.model;

import java.util.Map;

import com.huahui.datasphere.mdm.search.type.IndexField;

/**
 * Holds and returns indexing information.
 * @author theseusyang on Mar 1, 2020
 */
public interface IndexedElement {
    /**
     * Retruns cached index fields.
     * @return index fields
     */
    Map<String, IndexField> getIndexFields();
}
