package com.huahui.datasphere.system.type.variables;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mikhail Mikhailov on Sep 10, 2020
 * Class, implementing a named variable.
 */
public abstract class Variable<T> {

    public enum Type {
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
        DATE,
        TIME,
        TIMESTAMP,
        INSTANT,
        BOOLEAN
    }
    /**
     * Var name.
     */
    protected String name;
    /**
     * The value.
     */
    protected T value;
    /**
     * Constructor for serialization.
     */
    protected Variable() {
        super();
    }
    /**
     * Constructor.
     */
    protected Variable(String name, T value) {
        super();

        Objects.requireNonNull(name, "Variable name must not be null");

        this.name = name;
        this.value = value;
    }
    /**
     * Gets the type
     * @return type
     */
    public abstract Type getType();
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }
    /**
     * Seta the value.
     * @param value the value to set
     */
    public void setValue(T value) {
        this.value = value;
    }
    /**
     * Returns true if value is set.
     * @return true, if value set, false otherwise
     */
    public boolean isSet() {
        return Objects.nonNull(value);
    }
    /**
     * Int.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<Integer> of(String name, Integer value) {
        return new IntegerVariable(name, value);
    }
    /**
     * Long.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<Long> of(String name, Long value) {
        return new LongVariable(name, value);
    }
    /**
     * Float.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<Float> of(String name, Float value) {
        return new FloatVariable(name, value);
    }
    /**
     * Double.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<Double> of(String name, Double value) {
        return new DoubleVariable(name, value);
    }
    /**
     * String.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<String> of(String name, String value) {
        return new StringVariable(name, value);
    }
    /**
     * Date.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<LocalDate> of(String name, LocalDate value) {
        return new DateVariable(name, value);
    }
    /**
     * Time.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<LocalTime> of(String name, LocalTime value) {
        return new TimeVariable(name, value);
    }
    /**
     * DT.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<LocalDateTime> of(String name, LocalDateTime value) {
        return new TimestampVariable(name, value);
    }
    /**
     * Instant.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<Instant> of(String name, Instant value) {
        return new InstantVariable(name, value);
    }
    /**
     * Boolean.
     * @param name the name
     * @param value the value
     * @return var
     */
    public static Variable<Boolean> of(String name, Boolean value) {
        return new BooleanVariable(name, value);
    }
    /**
     * Returns variables as map.
     * @param variables the variables
     * @return map
     */
    public static Map<String, Variable<?>> asMap(Variable<?>... variables) {

        if (ArrayUtils.isNotEmpty(variables)) {

            Map<String, Variable<?>> result = new HashMap<>(variables.length);
            for (int i = 0; i < variables.length; i++) {

                if (Objects.isNull(variables[i]) || Objects.isNull(variables[i].getName())) {
                    continue;
                }

                result.put(variables[i].getName(), variables[i]);
            }

            return result;
        }

        return Collections.emptyMap();
    }
    /**
     * Int impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class IntegerVariable extends Variable<Integer> {
        /**
         * Constructor.
         */
        protected IntegerVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public IntegerVariable(String name, Integer value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.INTEGER;
        }
    }
    /**
     * Long impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class LongVariable extends Variable<Long> {
        /**
         * Constructor.
         */
        protected LongVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public LongVariable(String name, Long value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.LONG;
        }
    }
    /**
     * Float impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class FloatVariable extends Variable<Float> {
        /**
         * Constructor.
         */
        protected FloatVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public FloatVariable(String name, Float value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.FLOAT;
        }
    }
    /**
     * Double impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class DoubleVariable extends Variable<Double> {
        /**
         * Constructor.
         */
        protected DoubleVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public DoubleVariable(String name, Double value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.DOUBLE;
        }
    }
    /**
     * String impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class StringVariable extends Variable<String> {
        /**
         * Constructor.
         */
        protected StringVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public StringVariable(String name, String value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.STRING;
        }
    }
    /**
     * Date impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class DateVariable extends Variable<LocalDate> {
        /**
         * Constructor.
         */
        protected DateVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public DateVariable(String name, LocalDate value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.DATE;
        }
    }
    /**
     * Time impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class TimeVariable extends Variable<LocalTime> {
        /**
         * Constructor.
         */
        protected TimeVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public TimeVariable(String name, LocalTime value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.TIME;
        }
    }
    /**
     * Timestamp impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class TimestampVariable extends Variable<LocalDateTime> {
        /**
         * Constructor.
         */
        protected TimestampVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public TimestampVariable(String name, LocalDateTime value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.TIMESTAMP;
        }
    }
    /**
     * Instant impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class InstantVariable extends Variable<Instant> {
        /**
         * Constructor.
         */
        protected InstantVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public InstantVariable(String name, Instant value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.INSTANT;
        }
    }
    /**
     * Timestamp impl,
     * @author Mikhail Mikhailov on Sep 10, 2020
     */
    public static class BooleanVariable extends Variable<Boolean> {
        /**
         * Constructor.
         */
        protected BooleanVariable() {
            super();
        }
        /**
         * Constructor.
         * @param name the name
         * @param value the value
         */
        public BooleanVariable(String name, Boolean value) {
            super(name, value);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Type getType() {
            return Type.BOOLEAN;
        }
    }
}
