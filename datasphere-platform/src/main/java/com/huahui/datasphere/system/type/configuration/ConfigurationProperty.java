package com.huahui.datasphere.system.type.configuration;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Mikhail Mikhailov on Apr 17, 2020
 */
public class ConfigurationProperty<T> {

    public static final ConfigurationProperty<?>[] EMPTY_PROPERTIES_ARRAY = new ConfigurationProperty[0];

    private final Class<T> targetType;
    private final String key;
    private final String moduleId;
    private final String groupKey;
    private final ConfigurationPropertyType propertyType;
    private final Predicate<Optional<String>> validator;
    private final Function<String, T> deserializer;
    private final Function<T, String> serializer;
    private final Consumer<T> setter;
    private final T defaultValue;
    private final Map<Serializable, String> availableValues;
    private final boolean required;
    private final boolean readOnly;
    /**
     * Constructor.
     * @param b the builder
     */
    private ConfigurationProperty(ConfigurationPropertyBuilder<T> b) {
        super();
        this.targetType = b.targetType;
        this.key = b.key;
        this.moduleId = b.moduleId;
        this.groupKey = b.groupKey;
        this.propertyType = b.propertyType;
        this.validator = b.validator;
        this.deserializer = b.deserializer;
        this.serializer = b.serializer;
        this.setter = b.setter;
        this.defaultValue = b.defaultValue;
        this.availableValues = b.availableValues;
        this.required = b.required;
        this.readOnly = b.readOnly;
    }
    /**
     * @return the targetType
     */
    public Class<T> getTargetType() {
        return targetType;
    }
    /**
     * The configuration property key name, such as org.unidata.mdm.important.param
     * @return configuration key name
     */
    public String getKey() {
        return key;
    }
    /**
     * Module id, where this property is from.
     * @return the moduleId
     */
    public String getModuleId() {
        return moduleId;
    }
    /**
     * The logical group name.
     * @return group name
     */
    public String getGroupKey() {
        return groupKey;
    }
    /**
     * Gets the property value type.
     * @return property value type
     */
    public ConfigurationPropertyType getPropertyType() {
        return propertyType;
    }
    /**
     * Gets a possibly defined default value.
     * @return
     */
    public T getDefaultValue() {
        return defaultValue;
    }
    /**
     * Wraps default value in an {@linkplain Optional} object.
     * @return optional
     */
    public Optional<T> wrapDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
    /**
     * Gets a possibly defined value validator.
     * @return validator for supplied values.
     */
    public Predicate<Optional<String>> getValidator() {
        return Objects.isNull(validator) ? propertyType.getValidator() : validator;
    }
    /**
     * Gets a possibly defined value deserializer.
     * @return value deserializer
     */
    @SuppressWarnings("unchecked")
    public Function<String, T> getDeserializer() {
        return Objects.isNull(deserializer) ? (Function<String, T>) propertyType.getDeserializer() : deserializer;
    }
    /**
     * Gets a possibly defined value serializer.
     * @return the serializer
     */
    @SuppressWarnings("unchecked")
    public Function<T, String> getSerializer() {
        return Objects.isNull(serializer) ? (Function<T, String>) propertyType.getSerializer() : serializer;
    }
    /**
     * Gets global static setter for values.
     * @return setter
     */
    public Consumer<T> getSetter() {
        return setter;
    }
    /**
     * List of values, allowed for this property.
     * @return list of allowed values
     */
    public Map<Serializable, String> getAvailableValues() {
        return Objects.isNull(availableValues) ? Collections.emptyMap() : availableValues;
    }
    /**
     * Is this property required?
     * @return whether the property is required or not.
     */
    public boolean isRequired() {
        return required;
    }
    /**
     * Is this property editable?
     * @return whether the property is read only or not.
     */
    public boolean isReadOnly() {
        return readOnly;
    }
    @SuppressWarnings("unchecked")
    public T narrow(Object obj) {
        return (T) obj;
    }
    /**
     * String property builder.
     * @return builder
     */
    public static ConfigurationPropertyBuilder<String> string() {
        ConfigurationPropertyBuilder<String> b = new ConfigurationPropertyBuilder<>(String.class);
        b.propertyType(ConfigurationPropertyType.STRING);
        return b;
    }
    /**
     * Integer property builder.
     * @return builder
     */
    public static ConfigurationPropertyBuilder<Long> integer() {
        ConfigurationPropertyBuilder<Long> b = new ConfigurationPropertyBuilder<>(Long.class);
        b.propertyType(ConfigurationPropertyType.INTEGER);
        return b;
    }
    /**
     * Number property builder.
     * @return builder
     */
    public static ConfigurationPropertyBuilder<Double> number() {
        ConfigurationPropertyBuilder<Double> b = new ConfigurationPropertyBuilder<>(Double.class);
        b.propertyType(ConfigurationPropertyType.NUMBER);
        return b;
    }
    /**
     * Boolean property builder.
     * @return builder
     */
    public static ConfigurationPropertyBuilder<Boolean> bool() {
        ConfigurationPropertyBuilder<Boolean> b = new ConfigurationPropertyBuilder<>(Boolean.class);
        b.propertyType(ConfigurationPropertyType.BOOLEAN);
        return b;
    }
    /**
     * Custom type property builder.
     * @return builder
     */
    public static<X> ConfigurationPropertyBuilder<X> custom(Class<X> target) {
        ConfigurationPropertyBuilder<X> b = new ConfigurationPropertyBuilder<>(target);
        b.propertyType(ConfigurationPropertyType.CUSTOM);
        return b;
    }
    /**
     * Builder class.
     * @author Mikhail Mikhailov on Apr 17, 2020
     */
    public static class ConfigurationPropertyBuilder<T> {

        private Class<T> targetType;
        private String key;
        private String moduleId;
        private String groupKey;
        private ConfigurationPropertyType propertyType;
        private Predicate<Optional<String>> validator;
        private Function<String, T> deserializer;
        private Function<T, String> serializer;
        private Consumer<T> setter;
        private T defaultValue;
        private Map<Serializable, String> availableValues;
        private boolean required;
        private boolean readOnly;

        /**
         * Constructor.
         */
        private ConfigurationPropertyBuilder(Class<T> targetType) {
            super();
            this.targetType = targetType;
        }
        /**
         * @param key the key to set
         */
        public ConfigurationPropertyBuilder<T> key(String key) {
            this.key = key;
            return self();
        }
        /**
         * @param moduleId the moduleId to set
         */
        public ConfigurationPropertyBuilder<T> moduleId(String moduleId) {
            this.moduleId = moduleId;
            return self();
        }
        /**
         * @param groupKey the groupKey to set
         */
        public ConfigurationPropertyBuilder<T> groupKey(String groupKey) {
            this.groupKey = groupKey;
            return self();
        }
        /**
         * @param propertyType the propertyType to set
         */
        public ConfigurationPropertyBuilder<T> propertyType(ConfigurationPropertyType propertyType) {
            this.propertyType = propertyType;
            return self();
        }
        /**
         * @param validator the validator to set
         */
        public ConfigurationPropertyBuilder<T> validator(Predicate<Optional<String>> validator) {
            this.validator = validator;
            return self();
        }
        /**
         * @param deserializer the deserializer to set
         */
        public ConfigurationPropertyBuilder<T> deserializer(Function<String, T> deserializer) {
            this.deserializer = deserializer;
            return self();
        }
        /**
         * @param serializer the serializer to set
         */
        public ConfigurationPropertyBuilder<T> serializer(Function<T, String> serializer) {
            this.serializer = serializer;
            return self();
        }
        /**
         * @param setter the global static value setter
         */
        public ConfigurationPropertyBuilder<T> setter(Consumer<T> setter) {
            this.setter = setter;
            return self();
        }
        /**
         * @param defaultValue the defaultValue to set
         */
        public ConfigurationPropertyBuilder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return self();
        }
        /**
         * @param availableValues the availableValues to set
         */
        public ConfigurationPropertyBuilder<T> availableValues(Map<Serializable, String> availableValues) {
            this.availableValues = availableValues;
            return self();
        }
        /**
         * @param required the required to set
         */
        public ConfigurationPropertyBuilder<T> required(boolean required) {
            this.required = required;
            return self();
        }
        /**
         * @param readOnly the readonly to set
         */
        public ConfigurationPropertyBuilder<T> readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return self();
        }
        /**
         * Self-to sub type casting.
         * @return this
         */
        protected ConfigurationPropertyBuilder<T> self() {
            return this;
        }
        /**
         * The builder method.
         * @return a {@linkplain ConfigurationProperty} instance.
         */
        public ConfigurationProperty<T> build() {

            Objects.requireNonNull(this.key, "Property key cannot be null.");
            Objects.requireNonNull(this.moduleId, "Module ID cannot be null.");

            if (this.propertyType == ConfigurationPropertyType.CUSTOM) {
                Objects.requireNonNull(this.deserializer, "Deserializer cannot be null for custom types. Property: " + this.key);
                if (!this.readOnly) {
                    Objects.requireNonNull(this.serializer, "Serializer cannot be null for custom types. Property: " + this.key);
                }
            }

            return new ConfigurationProperty<>(this);
        }
    }
}
