package org.datasphere.mdm.core.type.model;

import java.util.Date;

import javax.annotation.Nullable;

/**
 * @author Mikhail Mikhailov on Oct 16, 2020
 * Possibly defined validity period settings for top level entities.
 */
public interface ValidityPeriodElement {
    /**
     * @author Mikhail Mikhailov on Oct 21, 2020
     * Granularity mode for this validity period element.
     * FIXME: Change label names.
     */
    public enum Granularity {
        DATE,
        DATETIME,
        DATETIMEMILLIS;
    }
    /**
     * Returns true, if validity period settings were defined for this element.
     * @return true, if validity period settings were defined for this element.
     */
    boolean isDefined();
    /**
     * Gets the validity period start.
     * @return the validity period start
     */
    @Nullable
    Date getFrom();
    /**
     * Gets the validity period end.
     * @return the validity period end
     */
    @Nullable
    Date getTo();
    /**
     * Gets the granularity.
     * @return granularity
     */
    Granularity getGranularity();
}
