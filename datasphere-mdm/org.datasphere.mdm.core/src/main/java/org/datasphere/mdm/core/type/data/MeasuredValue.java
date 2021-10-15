package org.datasphere.mdm.core.type.data;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikhail Mikhailov on Sep 3, 2020
 * Measured value container.
 * Measured values are such values, that can be given in conjunction with particular measurement units.
 * For instance, for an attribute, configured as mass units in the model,
 * the values can be sent in grams, kylograms, pounds, tons or whatever.
 * There is a base measurement unit for each measurement category.
 * For example, it can be grams for mass units.
 * The values, that were given by the user, are considered to be initial values.
 * If the input was given not in base units, the value in base units is also calculated, possibly, using user-defined conversion functions.
 * The value in base units is used as the reference value for DQ processing, search, indexing etc. afterwards.
 */
public class MeasuredValue {
    /**
     * Measured value category id - mass, distance, acceleration, denisity, etc.
     */
    private String categoryId;
    /**
     * Measurement unit of the value - kylograms, pounds, picogram for category "mass".
     */
    private String unitId;
    /**
     * User defined value of attribute.
     */
    private Double initialValue;
    /**
     * User defined value of attribute.
     */
    private Double baseValue;
    /**
     * Constructor.
     */
    public MeasuredValue() {
        super();
    }
    /**
     * Gets the user-defined or default pre-configured category id.
     * @return the category id.
     */
    public String getCategoryId() {
        return categoryId;
    }
    /**
     * Sets the user-defined or default pre-configured category id.
     * @param id the category id to set
     */
    public void setCategoryId(String id) {
        this.categoryId = id;
    }
    /**
     * Gets the user-defined or default unit id for units category .
     */
    public String getUnitId() {
        return unitId;
    }
    /**
     * Sets the user-defined or default unit id for units category .
     * @param id the unit id to set
     */
    public void setUnitId(String id) {
        this.unitId = id;
    }
    /**
     * User defined value of the attribute.
     * @return double value
     */
    public Double getInitialValue() {
        return initialValue;
    }
    /**
     * Sets initial value.
     * @param value the value to set
     */
    public void setInitialValue(Double value) {
        this.initialValue = value;
    }
    /**
     * Gets the value in base units.
     * @return the baseValue
     */
    public Double getBaseValue() {
        return baseValue;
    }
    /**
     * Sets the value in base units.
     * @param baseValue the baseValue to set
     */
    public void setBaseValue(Double baseValue) {
        this.baseValue = baseValue;
    }
    /**
     * Set category id fluently.
     * @param categoryId the category id
     * @return self
     */
    public MeasuredValue withCategoryId(String categoryId) {
        setCategoryId(categoryId);
        return this;
    }
    /**
     * Sets unit id fluently.
     * @param unitId - the unit id
     * @return self
     */
    public MeasuredValue withUnitId(String unitId) {
        setUnitId(unitId);
        return this;
    }
    /**
     * Sets initial value fluently.
     * @param initialValue the initial value
     * @return self
     */
    public MeasuredValue withInitialValue(Double initialValue) {
        setInitialValue(initialValue);
        return this;
    }
    /**
     * Sets initial value fluently.
     * @param initialValue the initial value
     * @return self
     */
    public MeasuredValue withBaseValue(Double initialValue) {
        setBaseValue(initialValue);
        return this;
    }
    /**
     * Tells, whether metadata fields (categoryId and unitId)
     * have been set on this measured value.
     * @return true, if so, false otherwise
     */
    public boolean hasMetadata() {
        return StringUtils.isNotBlank(getCategoryId())
            && StringUtils.isNotBlank(getUnitId());
    }
    /**
     * Tells whether initial value has been set.
     * @return true, if so, false otherwise
     */
    public boolean hasInitialValue() {
        return Objects.nonNull(getInitialValue());
    }
    /**
     * Tells whether base value has been set.
     * @return true, if so, false otherwise
     */
    public boolean hasBaseValue() {
        return Objects.nonNull(getBaseValue());
    }
    /**
     * Tells whether this MV has all fields set (basically, this means, it has already been processed).
     * @return true, if so, false otherwise
     */
    public boolean isComplete() {
        return hasMetadata()
            && hasInitialValue()
            && hasBaseValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
        result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
        result = prime * result + ((initialValue == null) ? 0 : initialValue.hashCode());
        result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        MeasuredValue other = (MeasuredValue) obj;
        return Objects.equals(baseValue, other.baseValue)
            && Objects.equals(initialValue, other.initialValue)
            && Objects.equals(categoryId, other.categoryId)
            && Objects.equals(unitId, other.unitId);
    }
    /**
     * Static shortcut. Will be treated as value in base units. Category will be taken from MM.
     * @param value the initial value
     * @return {@linkplain MeasuredValue}
     */
    public static MeasuredValue of(Double value) {
        return of(null, null, value);
    }
    /**
     * Static combo. Base value will be calculated by post-processing.
     * @param categoryId the category id
     * @param unitId the unit id
     * @param value the value itself
     * @return {@linkplain MeasuredValue}
     */
    public static MeasuredValue of(String categoryId, String unitId, Double value) {
        return of(categoryId, unitId, value, null);
    }
    /**
     * Static combo.
     * @param categoryId the category id
     * @param unitId the unit id
     * @param initial the value itself
     * @return {@linkplain MeasuredValue}
     */
    public static MeasuredValue of(String categoryId, String unitId, Double initial, Double base) {
        return new MeasuredValue()
                .withUnitId(unitId)
                .withCategoryId(categoryId)
                .withInitialValue(initial)
                .withBaseValue(base);
    }
}
