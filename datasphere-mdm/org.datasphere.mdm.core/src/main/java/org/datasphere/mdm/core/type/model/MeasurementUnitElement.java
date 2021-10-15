package org.datasphere.mdm.core.type.model;

import org.datasphere.mdm.core.type.data.MeasuredValue;

/**
 * @author Mikhail Mikhailov on Nov 5, 2020
 * The measurement unit.
 */
public interface MeasurementUnitElement extends IdentityElement, NamedDisplayableElement {
    /**
     * Gets the string representation of the conversion function (calculation without body).
     * @return string representation of the conversion function.
     */
    String getConversionFunction();
    /**
     * Returns true if this unit is the base unit for its parent category.
     * @return true if this unit is the base unit for its parent category
     */
    boolean isBase();
    /**
     * Converts the given initial value to its base representation.
     * @param initial the initial value
     * @return converted to base
     */
    Double convert(Double initial);
    /**
     * Does conversion of the given measured value, if the value needs conversion (is of different unit).
     * @param v the value to process
     */
    void process(MeasuredValue v);
}
