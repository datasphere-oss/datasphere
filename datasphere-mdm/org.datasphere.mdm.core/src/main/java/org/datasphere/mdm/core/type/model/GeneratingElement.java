package org.datasphere.mdm.core.type.model;

import org.datasphere.mdm.core.context.DataRecordContext;

/**
 * ID/field value generator.
 * @author Mikhail Mikhailov on May 15, 2020
 */
public interface GeneratingElement {
    /**
     * Generates a value, using (or not using) the input.
     * @param input the input
     * @return generated value
     */
    Object generate(DataRecordContext input);
    /**
     * Gets the type of this generation value.
     * @return type
     */
    GenerationStrategyType getStrategyType();
}
