package com.huahui.datasphere.mdm.core.type.model.support;

import com.huahui.datasphere.mdm.core.context.DataRecordContext;
import com.huahui.datasphere.mdm.core.type.model.EntityElement;
/**
 * Generates external id value.
 * Extends this interface to run custom entity external ID value generation.
 * @author theseusyang on May 15, 2020
 */
public interface ExternalIdValueGenerator {
    /**
     * Generates a value, using (or not using) the input.
     * @param input the input
     * @return generated value
     */
    String generate(EntityElement entity, DataRecordContext input);
}
