/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.datasphere.mdm.core.type.job;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;

/**
 * @author Mikhail Mikhailov on Jun 11, 2021
 */
public class JobParameterDescriptor<X> {
    /**
     * The input type (i. e. the type, visible on the UD side).
     */
    private final Class<X> valueType;
    /**
     * Param name.
     */
    private final String name;
    /**
     * Display name.
     */
    private final Supplier<String> displayName;
    /**
     * Display description supplier (can be used for i18n purposes).
     */
    private final Supplier<String> descriptionSupplier;
    /**
     * Outer type.
     */
    private final JobParameterType type;
    /**
     * Layout type.
     */
    private final JobParameterKind kind;
    /**
     * Is the parameter required.
     */
    private final boolean required;
    /**
     * The parameter is hidden.
     */
    private final boolean hidden;
    /**
     * The parameter is read only.
     */
    private final boolean readOnly;
    /**
     * Values supplier (for read only selectors).
     */
    private final Supplier<Map<X, String>> selector;
    /**
     * Input validator (one slot occupied).
     */
    private final Predicate<?>[] validators;
    /**
     * The default value (set if the input is null).
     */
    private final Object[] defaults;
    /**
     * Custom type definitions factory.
     */
    private final BiFunction<String, X, ? extends JobParameterDefinition<X>> factory;
    /**
     * Constructor.
     */
    private JobParameterDescriptor(JobParameterDescriptorBuilder<X> b) {
        super();
        this.valueType = b.valueType;
        this.name = b.name;
        this.displayName = Objects.isNull(b.displayNameSupplier) ? () -> b.displayName : b.displayNameSupplier;
        this.descriptionSupplier = Objects.isNull(b.descriptionSupplier) ? () -> b.description : b.descriptionSupplier;
        this.type = b.type;
        this.kind = b.kind;
        this.required = b.required;
        this.hidden = b.hidden;
        this.readOnly = b.readOnly;
        this.validators = b.validators;
        this.selector = b.selector;
        this.defaults = b.defaults;
        this.factory = b.factory;
    }
    /**
     * @return the inputType
     */
    public Class<X> getValueType() {
        return valueType;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName.get();
    }
    /**
     * @return the descriptionSupplier
     */
    public String getDescription() {
        return descriptionSupplier.get();
    }
    /**
     * @return the type
     */
    public JobParameterType getType() {
        return type;
    }
    /**
     * @return the kind
     */
    public JobParameterKind getKind() {
        return kind;
    }
    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }
    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }
    /**
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }
    /**
     * @return the multiValue
     */
    public boolean isCollection() {
        return kind == JobParameterKind.COLLECTION;
    }
    /**
     * @return the mappedValue
     */
    public boolean isMap() {
        return kind == JobParameterKind.MAP;
    }
    /**
     * @return the single
     */
    public boolean isSingle() {
        return kind == JobParameterKind.SINGLE;
    }
    /**
     * @return the custom mark
     */
    public boolean isCustom() {
        return kind == JobParameterKind.CUSTOM;
    }
    /**
     * @return the supplier
     */
    public Supplier<Map<X, String>> getSelector() {
        return selector;
    }
    /**
     * Has selector value or not.
     * @return true, if has, false otherwise
     */
    public boolean hasSelector() {
        return Objects.nonNull(selector);
    }
    /**
     * @return the defaultValue
     */
    @SuppressWarnings("unchecked")
    public<Y> Y getDefaultValue() {
        return (Y) defaults[kind.ordinal()];
    }
    /**
     * Returns true if this descriptor has default value.
     * @return true if this descriptor has default value
     */
    public boolean hasDefaultValue() {
        return Objects.nonNull(getDefaultValue());
    }
    /**
     * Validates single and custom values.
     * @return true, if no validator defined or the validator return true
     */
    @SuppressWarnings("unchecked")
    public boolean validate(X obj) {

        if (kind != JobParameterKind.SINGLE && kind != JobParameterKind.CUSTOM) {
            throw new JobException("Validation failed. Parameter descriptor is not SINGLE or CUSTOM.",
                    CoreExceptionIds.EX_JOB_PARAMETER_VALIDATE_NOT_SINGLE_OR_CUSTOM);
        }

        Predicate<X> validator = (Predicate<X>) validators[kind.ordinal()];
        return Objects.isNull(validator) || validator.test(obj);
    }
    /**
     * Validates collection values.
     * @return true, if no validator defined or the validator return true
     */
    @SuppressWarnings("unchecked")
    public boolean validate(Collection<X> obj) {

        if (kind != JobParameterKind.COLLECTION) {
            throw new JobException("Validation failed. Parameter descriptor is not a COLLECTION.",
                    CoreExceptionIds.EX_JOB_PARAMETER_VALIDATE_NOT_COLLECTION);
        }

        Predicate<Collection<X>> validator = (Predicate<Collection<X>>) validators[kind.ordinal()];
        return Objects.isNull(validator) || validator.test(obj);
    }
    /**
     * Validates map values.
     * @return true, if no validator defined or the validator return true
     */
    @SuppressWarnings("unchecked")
    public boolean validate(Map<String, X> obj) {

        if (kind != JobParameterKind.MAP) {
            throw new JobException("Validation failed. Parameter descriptor is not a MAP.",
                    CoreExceptionIds.EX_JOB_PARAMETER_VALIDATE_NOT_MAP);
        }

        Predicate<Map<String, X>> validator = (Predicate<Map<String, X>>) validators[kind.ordinal()];
        return Objects.isNull(validator) || validator.test(obj);
    }
    /**
     * Creates custom definitions from value.
     * @param name the parameter name
     * @param value the parameter value
     * @return job parameter definition
     */
    public JobParameterDefinition<X> custom(String name, Object value) {
        return factory.apply(name, valueType.cast(value));
    }
    /**
     * String.
     */
    public static JobParameterDescriptorBuilder<String> string() {
        return new JobParameterDescriptorBuilder<>(String.class, JobParameterType.STRING, JobParameterKind.SINGLE);
    }
    /**
     * String.
     */
    public static JobParameterDescriptorBuilder<String> strings() {
        return new JobParameterDescriptorBuilder<>(String.class, JobParameterType.STRING, JobParameterKind.COLLECTION);
    }
    /**
     * String.
     */
    public static JobParameterDescriptorBuilder<String> mappedStrings() {
        return new JobParameterDescriptorBuilder<>(String.class, JobParameterType.STRING, JobParameterKind.MAP);
    }
    /**
     * Integer.
     */
    public static JobParameterDescriptorBuilder<Long> integer() {
        return new JobParameterDescriptorBuilder<>(Long.class, JobParameterType.INTEGER, JobParameterKind.SINGLE);
    }
    /**
     * Integer.
     */
    public static JobParameterDescriptorBuilder<Long> integers() {
        return new JobParameterDescriptorBuilder<>(Long.class, JobParameterType.INTEGER, JobParameterKind.COLLECTION);
    }
    /**
     * Integer.
     */
    public static JobParameterDescriptorBuilder<Long> mappedIntegers() {
        return new JobParameterDescriptorBuilder<>(Long.class, JobParameterType.INTEGER, JobParameterKind.MAP);
    }
    /**
     * Number.
     */
    public static JobParameterDescriptorBuilder<Double> number() {
        return new JobParameterDescriptorBuilder<>(Double.class, JobParameterType.NUMBER, JobParameterKind.SINGLE);
    }
    /**
     * Number.
     */
    public static JobParameterDescriptorBuilder<Double> numbers() {
        return new JobParameterDescriptorBuilder<>(Double.class, JobParameterType.NUMBER, JobParameterKind.COLLECTION);
    }
    /**
     * Number.
     */
    public static JobParameterDescriptorBuilder<Double> mappedNumbers() {
        return new JobParameterDescriptorBuilder<>(Double.class, JobParameterType.NUMBER, JobParameterKind.MAP);
    }
    /**
     * Boolean.
     */
    public static JobParameterDescriptorBuilder<Boolean> bool() {
        return new JobParameterDescriptorBuilder<>(Boolean.class, JobParameterType.BOOLEAN, JobParameterKind.SINGLE);
    }
    /**
     * Boolean.
     */
    public static JobParameterDescriptorBuilder<Boolean> bools() {
        return new JobParameterDescriptorBuilder<>(Boolean.class, JobParameterType.BOOLEAN, JobParameterKind.COLLECTION);
    }
    /**
     * Boolean.
     */
    public static JobParameterDescriptorBuilder<Boolean> mappedBools() {
        return new JobParameterDescriptorBuilder<>(Boolean.class, JobParameterType.BOOLEAN, JobParameterKind.MAP);
    }
    /**
     * CLOB.
     */
    public static JobParameterDescriptorBuilder<String> clob() {
        return new JobParameterDescriptorBuilder<>(String.class, JobParameterType.CLOB, JobParameterKind.SINGLE);
    }
    /**
     * CLOB.
     */
    public static JobParameterDescriptorBuilder<String> clobs() {
        return new JobParameterDescriptorBuilder<>(String.class, JobParameterType.CLOB, JobParameterKind.COLLECTION);
    }
    /**
     * CLOB.
     */
    public static JobParameterDescriptorBuilder<String> mappedClobs() {
        return new JobParameterDescriptorBuilder<>(String.class, JobParameterType.CLOB, JobParameterKind.MAP);
    }
    /**
     * LD.
     */
    public static JobParameterDescriptorBuilder<LocalDate> date() {
        return new JobParameterDescriptorBuilder<>(LocalDate.class, JobParameterType.DATE, JobParameterKind.SINGLE);
    }
    /**
     * LD.
     */
    public static JobParameterDescriptorBuilder<LocalDate> dates() {
        return new JobParameterDescriptorBuilder<>(LocalDate.class, JobParameterType.DATE, JobParameterKind.COLLECTION);
    }
    /**
     * LD.
     */
    public static JobParameterDescriptorBuilder<LocalDate> mappedDates() {
        return new JobParameterDescriptorBuilder<>(LocalDate.class, JobParameterType.DATE, JobParameterKind.MAP);
    }
    /**
     * LT.
     */
    public static JobParameterDescriptorBuilder<LocalTime> time() {
        return new JobParameterDescriptorBuilder<>(LocalTime.class, JobParameterType.TIME, JobParameterKind.SINGLE);
    }
    /**
     * LT.
     */
    public static JobParameterDescriptorBuilder<LocalTime> times() {
        return new JobParameterDescriptorBuilder<>(LocalTime.class, JobParameterType.TIME, JobParameterKind.COLLECTION);
    }
    /**
     * LT.
     */
    public static JobParameterDescriptorBuilder<LocalTime> mappedTimes() {
        return new JobParameterDescriptorBuilder<>(LocalTime.class, JobParameterType.TIME, JobParameterKind.MAP);
    }
    /**
     * LD.
     */
    public static JobParameterDescriptorBuilder<LocalDateTime> timestamp() {
        return new JobParameterDescriptorBuilder<>(LocalDateTime.class, JobParameterType.TIMESTAMP, JobParameterKind.SINGLE);
    }
    /**
     * LD.
     */
    public static JobParameterDescriptorBuilder<LocalDateTime> timestamps() {
        return new JobParameterDescriptorBuilder<>(LocalDateTime.class, JobParameterType.TIMESTAMP, JobParameterKind.COLLECTION);
    }
    /**
     * LD.
     */
    public static JobParameterDescriptorBuilder<LocalDateTime> mappedTimestamps() {
        return new JobParameterDescriptorBuilder<>(LocalDateTime.class, JobParameterType.TIMESTAMP, JobParameterKind.MAP);
    }
    /**
     * Instant.
     */
    public static JobParameterDescriptorBuilder<Instant> instant() {
        return new JobParameterDescriptorBuilder<>(Instant.class, JobParameterType.INSTANT, JobParameterKind.SINGLE);
    }
    /**
     * Instant.
     */
    public static JobParameterDescriptorBuilder<Instant> instants() {
        return new JobParameterDescriptorBuilder<>(Instant.class, JobParameterType.INSTANT, JobParameterKind.COLLECTION);
    }
    /**
     * Instant.
     */
    public static JobParameterDescriptorBuilder<Instant> mappedInstant() {
        return new JobParameterDescriptorBuilder<>(Instant.class, JobParameterType.INSTANT, JobParameterKind.MAP);
    }
    /**
     * Instant.
     */
    public static<T> JobParameterDescriptorBuilder<T> custom(Class<T> valueType) {
        return new JobParameterDescriptorBuilder<>(valueType, JobParameterType.CUSTOM, JobParameterKind.CUSTOM);
    }
    /**
     * @author Mikhail Mikhailov on Jun 12, 2021
     * Parameter descriptor builder.
     */
    public static class JobParameterDescriptorBuilder<X> {
        /**
         * The input type (i. e. the type, visible on the UD side).
         */
        private Class<X> valueType;
        /**
         * Outer type.
         */
        private final JobParameterType type;
        /**
         * Layout type.
         */
        private final JobParameterKind kind;
        /**
         * Name.
         */
        private String name;
        /**
         * Display name.
         */
        private String displayName;
        /**
         * Display name supplier (can be used for i18n purposes).
         */
        private Supplier<String> displayNameSupplier;
        /**
         * Display description.
         */
        private String description;
        /**
         * Display description supplier (can be used for i18n purposes).
         */
        private Supplier<String> descriptionSupplier;
        /**
         * Is the parameter required.
         */
        private boolean required;
        /**
         * The parameter is hidden.
         */
        private boolean hidden;
        /**
         * The parameter is read only.
         */
        private boolean readOnly;
        /**
         * Values supplier (for read only selectors).
         */
        private Supplier<Map<X, String>> selector;
        /**
         * Input validator (one slot occupied).
         */
        private Predicate<?>[] validators = new Predicate<?>[JobParameterKind.values().length];
        /**
         * The default value (set if the input is null).
         */
        private Object[] defaults = new Object[JobParameterKind.values().length];
        /**
         * Custom type definitions factory.
         */
        private BiFunction<String, X, ? extends JobParameterDefinition<X>> factory;
        /**
         * The only constructor.
         * @param valueType the input type
         * @param type the type
         * @param kind the layout kind
         */
        private JobParameterDescriptorBuilder(Class<X> valueType, JobParameterType type, JobParameterKind kind) {
            super();
            this.valueType = valueType;
            this.type = type;
            this.kind = kind;
        }
        /**
         * Name.
         * @param name the name
         * @return self
         */
        public JobParameterDescriptorBuilder<X> name(String name) {
            this.name = name;
            return this;
        }
        /**
         * Display name.
         * @param displayName the display name
         * @return self
         */
        public JobParameterDescriptorBuilder<X> displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        /**
         * Display name supplier (can be used for i18n purposes).
         * @param displayName the display name
         * @return self
         */
        public JobParameterDescriptorBuilder<X> displayName(Supplier<String> displayNameSupplier) {
            this.displayNameSupplier = displayNameSupplier;
            return this;
        }
        /**
         * Display description.
         * @param description the description
         * @return self
         */
        public JobParameterDescriptorBuilder<X> description(String description) {
            this.description = description;
            return this;
        }
        /**
         * Display description (can be used for i18n purposes).
         * @param displayName the display name
         * @return self
         */
        public JobParameterDescriptorBuilder<X> description(Supplier<String> description) {
            this.descriptionSupplier = description;
            return this;
        }
        /**
         * @param required the required to set
         */
        public JobParameterDescriptorBuilder<X> required(boolean required) {
            this.required = required;
            return this;
        }
        /**
         * @param hidden the hidden flag to set
         */
        public JobParameterDescriptorBuilder<X> hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }
        /**
         * @param readOnly the readonly to set
         */
        public JobParameterDescriptorBuilder<X> readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }
        /**
         * Values supplier (for read only selectors).
         */
        public JobParameterDescriptorBuilder<X> selector(Supplier<Map<X, String>> supplier) {
            this.selector = supplier;
            return this;
        }
        /**
         * Input validator.
         * Is used for both - SINGLE and CUSTOM variants.
         */
        public JobParameterDescriptorBuilder<X> singleValidator(Predicate<X> validator) {
            this.validators[JobParameterKind.SINGLE.ordinal()] = validator;
            return this;
        }
        /**
         * Input validator for collection layout.
         */
        public JobParameterDescriptorBuilder<X> collectionValidator(Predicate<Collection<X>> validator) {
            this.validators[JobParameterKind.COLLECTION.ordinal()] = validator;
            return this;
        }
        /**
         * Input validator for map layout.
         */
        public JobParameterDescriptorBuilder<X> mapValidator(Predicate<Map<String, X>> validator) {
            this.validators[JobParameterKind.MAP.ordinal()] = validator;
            return this;
        }
        /**
         * Input validator used for CUSTOM variants.
         */
        public JobParameterDescriptorBuilder<X> customValidator(Predicate<X> validator) {
            this.validators[JobParameterKind.CUSTOM.ordinal()] = validator;
            return this;
        }
        /**
         * The default value (set if the input is null).
         * Is used for both - SINGLE and CUSTOM variants.
         */
        public JobParameterDescriptorBuilder<X> defaultSingle(X defaultValue) {
            this.defaults[JobParameterKind.SINGLE.ordinal()] = defaultValue;
            return this;
        }
        /**
         * The default value (set if the input is null).
         */
        public JobParameterDescriptorBuilder<X> defaultCollection(Collection<X> defaultValue) {
            this.defaults[JobParameterKind.COLLECTION.ordinal()] = defaultValue;
            return this;
        }
        /**
         * The default value (set if the input is null).
         */
        public JobParameterDescriptorBuilder<X> defaultMap(Map<String, X> defaultValue) {
            this.defaults[JobParameterKind.MAP.ordinal()] = defaultValue;
            return this;
        }
        /**
         * The default value (set if the input is null).
         */
        public JobParameterDescriptorBuilder<X> defaultCustom(Map<String, X> defaultValue) {
            this.defaults[JobParameterKind.CUSTOM.ordinal()] = defaultValue;
            return this;
        }
        /**
         * The default value (set if the input is null).
         */
        public JobParameterDescriptorBuilder<X> customFactory(BiFunction<String, X, ? extends JobParameterDefinition<X>> factory) {
            this.factory = factory;
            return this;
        }
        /**
         * Builder method.
         * @return parameter descriptor instance.
         */
        public JobParameterDescriptor<X> build() {
            Objects.requireNonNull(this.name, "Parameter name must not be null.");

            if (kind == JobParameterKind.CUSTOM) {
                Objects.requireNonNull(valueType, "Value type must not be null for custom types.");
                Objects.requireNonNull(factory, "Definitions factory must not be null for custom types.");
            }

            return new JobParameterDescriptor<>(this);
        }
    }
}
