package org.datasphere.mdm.core.type.model.instance;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.datasphere.mdm.core.type.model.CustomPropertiesElement;
import org.datasphere.mdm.core.type.model.CustomPropertyElement;
import org.datasphere.mdm.core.type.model.source.CustomProperty;

/**
 * @author Mikhail Mikhailov on Oct 6, 2020
 * Custom properties holder.
 */
public abstract class AbstractCustomPropertiesImpl implements CustomPropertiesElement {
    /**
     * The values.
     */
    private final Map<String, CustomPropertyElement> values;
    /**
     * Constructor.
     */
    protected AbstractCustomPropertiesImpl(Collection<CustomProperty> properties) {
        super();
        values = properties.stream()
            .map(CustomPropertyImpl::new)
            .collect(Collectors.toMap(CustomPropertyImpl::getName, Function.identity()));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CustomPropertyElement> getCustomProperties() {
        return values.values();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean propertyExists(String name) {
        return values.containsKey(name);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public CustomPropertyElement getCustomProperty(String name) {
        return values.get(name);
    }
    /**
     * @author Mikhail Mikhailov on Oct 6, 2020
     * The property.
     */
    private class CustomPropertyImpl implements CustomPropertyElement {
        /**
         * Name.
         */
        private final String name;
        /**
         * Value.
         */
        private final String value;
        /**
         * Constructor.
         * @param property serialized property
         */
        private CustomPropertyImpl(CustomProperty property) {
            super();
            this.name = property.getName();
            this.value = property.getValue();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String getValue() {
            return value;
        }
    }
}
