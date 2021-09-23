package com.huahui.datasphere.system.util;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Fields, needed for time period bounded calculations.
 * @author Mikhail Mikhailov on Feb 19, 2020
 */
public final class TimeBoundaryUtils {
    /**
     * Positive infinity and max period id.
     *
     */
    public static final long TIME_POSITIVE_INFINITY = 9223372036825200000L;
    /**
     * Negative infinity.
     */
    public static final long TIME_NEGATIVE_INFINITY = -9223372036832400000L;
    /**
     * Constructor.
     */
    private TimeBoundaryUtils() {
        super();
    }
    /**
     * Ensures valid max period id for null dates and returns ts millis or upper bound.
     *
     * @param d the ts
     * @return millis
     */
    public static long toUpperBound(@Nullable Date d) {
        return Objects.isNull(d) ? TIME_POSITIVE_INFINITY : d.getTime();
    }
    /**
     * Ensures valid min period id for null dates and returns ts millis or lower bound.
     *
     * @param d the ts
     * @return millis
     */
    public static long toLowerBound(@Nullable Date d) {
        return Objects.isNull(d) ? TIME_NEGATIVE_INFINITY : d.getTime();
    }
    /**
     * Bounded version of compareTo.
     * If either one (or both) dates are null - {@link #TIME_POSITIVE_INFINITY} will be taken.
     * @param what the date to compare
     * @param to another date to compare to
     * @return -1 if 'what' is less then, 0 if 'what' is equal to, 1 if 'what' is greater then 'to'.
     */
    public static int upperBoundCompareTo(@Nullable Date what, @Nullable Date to) {

        long w = toUpperBound(what);
        long t = toUpperBound(to);

        if (w < t) {
            return -1;
        }

        return w == t ? 0 : 1;
    }
    /**
     * Bounded version of compareTo.
     * If either one (or both) dates are null - {@link #TIME_NEGATIVE_INFINITY} will be taken.
     * @param what the date to compare
     * @param to another date to compare to
     * @return -1 if 'what' is less then, 0 if 'what' is equal to, 1 if 'what' is greater then 'to'.
     */
    public static int lowerBoundCompareTo(Date what, Date to) {

        long w = toLowerBound(what);
        long t = toLowerBound(to);

        if (w < t) {
            return -1;
        }

        return w == t ? 0 : 1;
    }
}
