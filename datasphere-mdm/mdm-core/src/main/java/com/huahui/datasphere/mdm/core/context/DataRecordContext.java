package com.huahui.datasphere.mdm.core.context;

import java.util.Objects;

import com.huahui.datasphere.mdm.core.type.data.DataRecord;

/**
 * Marks contexts
 * @author theseusyang on May 15, 2020
 */
public interface DataRecordContext {
    /**
     * Gets supplied data record.
     * @return the record
     */
    DataRecord getRecord();
    /**
     * Returns true is the context has a data record.
     * @return true is the context has a data record, false otherwise
     */
    default boolean hasData() {
        return Objects.nonNull(getRecord());
    }
}
