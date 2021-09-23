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
package com.huahui.datasphere.system.service.impl;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.unidata.mdm.system.configuration.SystemConfigurationProperty;

import com.huahui.datasphere.system.exception.PlatformValidationException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.exception.ValidationResult;
import com.huahui.datasphere.system.service.RuntimePropertiesService;
import com.huahui.datasphere.system.service.impl.configuration.LocalConfigurationComponent;
import com.huahui.datasphere.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationUpdatesReader;
import com.huahui.datasphere.system.type.configuration.ConfigurationUpdatesWriter;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.system.type.configuration.impl.ImmutableConfigurationValue;
import com.huahui.datasphere.system.type.module.Module;

@Service
public class RuntimePropertiesServiceImpl implements RuntimePropertiesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimePropertiesServiceImpl.class);

    public static final String NIL = "@_null_";
    /**
     * Local cache.
     */
    private final LocalConfigurationComponent cache;
    /**
     * The writers chain.
     */
    private WritersChain writers;
    /**
     * The writers chain.
     */
    private ReadersChain readers;
    /**
     * Constructor.
     * @param localConfigurationComponent cache (see above)
     * @param configurationUpdatesReaders producers (see above)
     * @param configurationUpdatesByUserWriters consumers (see above)
     */
    public RuntimePropertiesServiceImpl(
            final LocalConfigurationComponent localConfigurationComponent,
            final List<ConfigurationUpdatesReader> configurationUpdatesReaders,
            final List<ConfigurationUpdatesWriter> configurationUpdatesByUserWriters) {
        super();
        this.cache = localConfigurationComponent;
        this.readers = new ReadersChain(configurationUpdatesReaders, localConfigurationComponent);
        this.writers = new WritersChain(configurationUpdatesByUserWriters, localConfigurationComponent);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        load(List.of(SystemConfigurationProperty.values()));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void load(final Collection<ConfigurationProperty<?>> cp) {

        if (CollectionUtils.isEmpty(cp)) {
            return;
        }

        // 1. Prepare values
        cache.pull(cp);

        // 2. Read / update saved values from ENV, DB.
        readers.read(cp);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ConfigurationValue<?>> getAll() {
        return cache.properties().stream()
                .map(this::getByProperty)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ConfigurationValue<?>> getByGroup(final String groupCode) {
        return cache.properties().stream()
                .filter(p -> groupCode.equals(p.getGroupKey()))
                .map(this::getByProperty)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationValue<?> getByKey(final String name) {

        ConfigurationProperty<?> p = cache.property(name);
        if (Objects.isNull(p)) {
            return ConfigurationValue.empty();
        }

        return getByProperty(p);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public<T> ConfigurationValue<T> getByProperty(ConfigurationProperty<T> p) {

        if (Objects.isNull(p)) {
            return (ConfigurationValue<T>) ConfigurationValue.empty();
        }

        return new ImmutableConfigurationValue(p, cache.value(p.getKey()).orElse(null));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Map<String, String> updates) {

        // 1. Filter updatable properties only (note NIL string, which is a placeholder for null)
        final Map<String, Optional<String>> values = updates.entrySet().stream()
                .filter(e -> { ConfigurationProperty<?> p = cache.property(e.getKey()); return Objects.nonNull(p) && !p.isReadOnly(); })
                .collect(Collectors.toMap(Entry::getKey, e -> Optional.ofNullable(NIL.equals(e.getValue()) ? null : e.getValue())));

        // 2. Validate values
        validate(values);

        // 3. Convert strings to target type, comparing with cached values, discarding identical values
        final Map<ConfigurationProperty<?>, Optional<?>> toUpdate = values.entrySet().stream()
                .map(p -> Pair.of(cache.property(p.getKey()), p.getValue()))
                .map(p -> Pair.of(p.getKey(), p.getValue().map(value -> p.getKey().getDeserializer().apply(value))))
                .filter(p -> !Objects.equals(p.getValue(), cache.value(p.getKey().getKey())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        // 4. Run updates
        writers.write(toUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        ReflectionUtils.doWithFields(bean.getClass(),
            f -> {

                final String propertyName = f.getAnnotation(ConfigurationRef.class).value();
                if (StringUtils.isBlank(propertyName)) {
                    LOGGER.warn("Ignoring @ConfigurationRef annotation with empty name on bean [{}].", beanName);
                    return;
                }

                if (f.getType() != ConfigurationValue.class) {
                    LOGGER.warn("Ignoring @ConfigurationRef annotation, annotating not a ConfigurationValue instance on bean [{}].", beanName);
                    return;
                }

                ConfigurationProperty<?> p = cache.property(propertyName);
                if (Objects.isNull(p)) {
                    LOGGER.warn("Configuration property with name [{}] not found and won't be set.", propertyName);
                    return;
                }

                boolean wasAccessiible = true;
                if ((!Modifier.isPublic(f.getModifiers())
                  || !Modifier.isPublic(f.getDeclaringClass().getModifiers())
                  ||  Modifier.isFinal(f.getModifiers())) && !f.isAccessible()) {
                    f.setAccessible(true);
                    wasAccessiible = false;
                }

                // Caution - the set will cause CCE, if the type parameters do not match.
                f.set(bean, wire(bean, p));
                if (!wasAccessiible) {
                    f.setAccessible(false);
                }
            },
            f -> f.getAnnotation(ConfigurationRef.class) != null);

        return bean;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeforeInitialization(Module m) {
        // 1. Load
        load(List.of(m.getConfigurationProperties()));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessAfterInitialization(Module m) {
        // 2. Wire
        postProcessBeforeInitialization(m, m.getId());
    }

    private ConfigurationValue<?> wire(@Nullable Object bean, @Nonnull ConfigurationProperty<?> p) {

        if (p.isReadOnly()) {
            return getByProperty(p);
        }

        return cache.mutable(bean, p);
    }

    private void validate(Map<String, Optional<String>> updates) {

        final List<String> invalidProperties = updates.entrySet().stream()
                .map(e -> cache.property(e.getKey()).getValidator().test(e.getValue()) ? null : e.getKey())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!invalidProperties.isEmpty()) {
            throw new PlatformValidationException(
                    "Invalid properties values: " + invalidProperties,
                    SystemExceptionIds.EX_CONFIGURATION_PROPERTIES_INVALID,
                    invalidProperties.stream()
                        .map(propertyKey -> new ValidationResult("Invalid property value - " + propertyKey, propertyKey))
                        .collect(Collectors.toList()));
        }
    }
    /**
     * Writers chain, executing on local updates.
     * @author Mikhail Mikhailov on Apr 21, 2020
     */
    private class WritersChain {
        /**
         * The writers.
         */
        private final List<ConfigurationUpdatesWriter> writers;
        /**
         * Local target.
         */
        private final LocalConfigurationComponent target;
        /**
         * Ctor.
         * @param writers the writers to use
         */
        public WritersChain(List<ConfigurationUpdatesWriter> writers, LocalConfigurationComponent target) {
            super();
            this.writers = writers;
            this.target = target;
        }
        /**
         * Writes updates
         * @param update the updates
         */
        public void write(Map<ConfigurationProperty<?>, Optional<?>> update) {

            for (int i = 0; CollectionUtils.isNotEmpty(writers) && i < writers.size(); i++) {
                try {
                    writers.get(i).write(update);
                } catch (Exception e) {
                    LOGGER.warn("Failed to write configuration properties update.", e);
                }
            }

            target.upsert(update);
        }
    }
    /**
     * Readers chain, executing on properties load.
     * @author Mikhail Mikhailov on Apr 21, 2020
     */
    private class ReadersChain {
        /**
         * The readers.
         */
        private final List<ConfigurationUpdatesReader> readers;
        /**
         * Local target.
         */
        private final LocalConfigurationComponent target;
        /**
         * Ctor.
         * @param readers the readers to use
         */
        public ReadersChain(List<ConfigurationUpdatesReader> readers, LocalConfigurationComponent target) {
            super();
            this.readers = readers;
            this.target = target;
        }
        /**
         * Reads updates.
         * @param update the updates
         */
        public void read(Collection<ConfigurationProperty<?>> properties) {

            Map<ConfigurationProperty<?>, Optional<?>> result = new HashMap<>();
            for (int i = 0; CollectionUtils.isNotEmpty(readers) && i < readers.size(); i++) {
                try {

                    Map<ConfigurationProperty<?>, Optional<?>> local = readers.get(i).read(properties);
                    if (MapUtils.isNotEmpty(local)) {
                        result.putAll(local);
                    }

                } catch (Exception e) {
                    LOGGER.warn("Failed to read configuration properties values.", e);
                }
            }

            target.upsert(result);
        }
    }
}
