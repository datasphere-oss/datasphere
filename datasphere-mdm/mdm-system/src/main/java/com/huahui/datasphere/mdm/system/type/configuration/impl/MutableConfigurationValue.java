package com.huahui.datasphere.mdm.system.type.configuration.impl;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValueUpdatesListener;

/**
 * Mutable configuration value.
 * @author theseusyang on Apr 21, 2020
 */
public class MutableConfigurationValue<T> implements ConfigurationValue<T> {
    /**
     * The property desсription.
     */
    private final ConfigurationProperty<T> property;
    /**
     * Current value.
     */
    private final AtomicReference<T> value;
    /**
     * A possibly set updates listener.
     */
    private final ConfigurationValueUpdatesListener listener;
    /**
     * Constructor.
     * @param property the property description
     * @param initialValue initial value
     */
    public MutableConfigurationValue(ConfigurationProperty<T> property, T initialValue, ConfigurationValueUpdatesListener listener) {
        super();
        this.property = property;
        this.value = new AtomicReference<>(initialValue);
        this.listener = listener;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationProperty<T> getProperty() {
        return property;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public T getValue() {

        if (isEmpty()) {
            return null;
        }

        T v = value.get();
        return Objects.isNull(v) ? property.getDefaultValue() : v;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(property) && Objects.isNull(value.get());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasValue() {
        return Objects.nonNull(getValue());
    }
    /**
     * Updates current value with a new one, invoking any listeners and / or static setters, if defined.
     * @param update the value to set
     */
    @SuppressWarnings("unchecked")
    public void update(Object update) {

        // 1. Set internal
        value.set((T) update);

        // 2. Invoke static stuff, if defined
        if (Objects.nonNull(property.getSetter())) {
            property.getSetter().accept(getValue());
        }

        // 3. Invoke listener, if registered
        if (Objects.nonNull(listener)) {
            listener.configurationValueUpdated(property);
        }
    }
}
