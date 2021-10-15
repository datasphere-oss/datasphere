package org.datasphere.mdm.core.type.model.support;

import org.datasphere.mdm.core.context.DataRecordContext;
import org.datasphere.mdm.core.type.model.AttributeElement;

/**
 * Generates a value for an attribute.
 * Extend this interface to run custom attribute value generation.
 * @author Mikhail Mikhailov on May 15, 2020
 */
public interface AttributeValueGenerator {
    /**
     * Generates a value, using (or not using) the input.
     * @param input the input
     * @return generated value
     */
    Object generate(AttributeElement attribute, DataRecordContext input);
    /**
     * Gets the output type class.
     * @return output type class
     */
    Class<?> getOutputType();
}
