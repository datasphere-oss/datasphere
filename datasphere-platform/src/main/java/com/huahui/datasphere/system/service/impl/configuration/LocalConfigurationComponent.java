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

package com.huahui.datasphere.system.service.impl.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.system.type.configuration.ConfigurationValueUpdatesListener;
import com.huahui.datasphere.system.type.configuration.impl.MutableConfigurationValue;

/**
 * Holds a simple value map and also serves updatable properties.
 * @author Alexander Malyshev
 */
@Component
public class LocalConfigurationComponent {
    /**
     * All known configuration properties, keyed by property key.
     */
    private final Map<String, ConfigurationProperty<?>> properties = new HashMap<>();
    /**
     * The values.
     */
    private final ConcurrentMap<String, Optional<?>> storage = new ConcurrentHashMap<>();
    /**
     * Updatable property values, keyed by property instances.
     */
    private final Map<String, Set<MutableConfigurationValue<?>>> updatables = new HashMap<>();
    /**
     * Does values upsert.
     * @param properties the values to set
     */
    public void upsert(Map<ConfigurationProperty<?>, Optional<?>> properties) {

        if (MapUtils.isEmpty(properties)) {
            return;
        }

        properties.forEach((k, v) -> {
            storage.put(k.getKey(), v);
            mutate(k, v);
        });
    }

    /**
     * Gets currently saved value
     * @param name the property name
     * @return optional wrapped value
     */
    public Optional<?> value(String name) {
        Optional<?> v = storage.get(name);
        return Objects.isNull(v) ? Optional.empty() : v;
    }
    /**
     * Gets the property definition for the given name.
     * @param name the property name
     * @return property or null
     */
    @Nullable
    public ConfigurationProperty<?> property(String name) {
        return properties.get(name);
    }
    /**
     * Gets all known properties.
     * @return collection of properties
     */
    public Collection<ConfigurationProperty<?>> properties() {
        return properties.values();
    }
    /**
     * Initially pull a collection of property definitions.
     * @param cp collection
     */
    public void pull(Collection<ConfigurationProperty<?>> cp) {

        for (ConfigurationProperty<?> p : cp) {

            // 1. Put props definitions to the map
            properties.put(p.getKey(), p);

            // 2. Special case. Wire static values.
            if (!p.isReadOnly() && Objects.nonNull(p.getSetter())) {
                mutable(null, p);
            }

            // 3. Populate cache with defaults. They will not be overwritten, if the value was never changed.
            upsert(Map.of(p, p.wrapDefaultValue()));
        }
    }
    /**
     * Creates mutable configuration value.
     * @param bean the holding bean or null for static settings
     * @param p the property definition
     * @return mutable configuration value
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ConfigurationValue<?> mutable(@Nullable Object bean, @Nonnull ConfigurationProperty<?> p) {

        ConfigurationValueUpdatesListener l = null;
        if (Objects.nonNull(bean) && ConfigurationValueUpdatesListener.class.isAssignableFrom(bean.getClass())) {
            l = (ConfigurationValueUpdatesListener) bean;
        }

        MutableConfigurationValue<?> v = new MutableConfigurationValue(p, value(p.getKey()).orElse(null), l);
        updatables.computeIfAbsent(p.getKey(), k -> Collections.newSetFromMap(new WeakHashMap<MutableConfigurationValue<?>, Boolean>())).add(v);

        return v;
    }

    private void mutate(ConfigurationProperty<?> key, Optional<?> value) {

        Set<MutableConfigurationValue<?>> mutables = updatables.get(key.getKey());
        if (CollectionUtils.isNotEmpty(mutables)) {
            mutables.forEach(m -> m.update(value.orElse(null)));
        }
    }
}
