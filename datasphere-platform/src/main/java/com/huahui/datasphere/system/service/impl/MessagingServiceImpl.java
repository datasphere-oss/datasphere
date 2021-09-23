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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.camel.spring.CamelContextFactoryBean;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.unidata.mdm.system.configuration.SystemConfiguration;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;
import org.unidata.mdm.system.configuration.SystemConfigurationProperty;

import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.service.MessagingService;
import com.huahui.datasphere.system.service.RuntimePropertiesService;
import com.huahui.datasphere.system.service.impl.module.ModularAnnotationConfigWebApplicationContext;
import com.huahui.datasphere.system.type.annotation.DomainRef;
import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.system.type.configuration.ConfigurationValueUpdatesListener;
import com.huahui.datasphere.system.type.messaging.DomainInstance;
import com.huahui.datasphere.system.type.messaging.DomainType;
import com.huahui.datasphere.system.type.messaging.Message;
import com.huahui.datasphere.system.type.module.Module;

@Service
public class MessagingServiceImpl implements MessagingService, ConfigurationValueUpdatesListener  {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingServiceImpl.class);

    private final RuntimePropertiesService runtimePropertiesService;

    private final ConcurrentMap<String, DomainInstance> domains = new ConcurrentHashMap<>(4);

    private final ApplicationContext applicationContext;

    private SpringCamelContext camelContext;

    private boolean camelContextStarted = false;
    /**
     * Camel trace enable/disable online switch.
     */
    private ConfigurationValue<Boolean> camelTraceEnabled;

    public MessagingServiceImpl(
            final SystemConfiguration configuration,
            final RuntimePropertiesService runtimePropertiesService) {
        this.runtimePropertiesService = runtimePropertiesService;
        this.applicationContext = configuration.getConfiguredApplicationContext();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPlatformStartup() {

        camelTraceEnabled = runtimePropertiesService
                .getByProperty(SystemConfigurationProperty.SYSTEM_MESSAGING_TRACE_ENABLED);

        ModularAnnotationConfigWebApplicationContext modularContext =
                (ModularAnnotationConfigWebApplicationContext) applicationContext;

        AbstractApplicationContext top = modularContext.getLast();

        final CamelContextFactoryBean camelContextFactoryBean = new CamelContextFactoryBean();
        camelContextFactoryBean.setId(SystemConfigurationConstants.SYSTEM_CAMEL_CONTEXT);
        camelContextFactoryBean.setTrace(camelTraceEnabled.getValue().toString());
        camelContextFactoryBean.setApplicationContext(top);

        try {
            camelContextFactoryBean.afterPropertiesSet();
            camelContextFactoryBean.start();
        } catch (Exception e) {
            throw new PlatformFailureException("Failed to initialize messaging subsystem.",
                    e, SystemExceptionIds.EX_SYSTEM_MESSAGING_STARTUP_FAILED);
        }

        camelContext = camelContextFactoryBean.getContext();
        camelContextStarted = true;
        domains.values().forEach(d -> ((DomainInstanceImpl) d).init(camelContext));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeforeInitialization(Module m) {
        DomainType[] types = m.getMessagingDomains();
        for (int i = 0; ArrayUtils.isNotEmpty(types) && i < types.length; i++) {
            register(types[i]);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void configurationValueUpdated(ConfigurationProperty<?> property) {
        if (property == SystemConfigurationProperty.SYSTEM_MESSAGING_TRACE_ENABLED) {
            camelContext.setTracing(camelTraceEnabled.getValue());
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessAfterInitialization(Module m) {
        postProcessBeforeInitialization(m, m.getId());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainInstance register(DomainType domain) {

        Objects.requireNonNull(domain, "Notification domain type must not be null.");
        Objects.requireNonNull(domain.getId(), "Notification domain type ID must not be null.");

        DomainInstance result = domains.computeIfAbsent(domain.getId(), k -> new DomainInstanceImpl(domain, runtimePropertiesService));
        if (camelContextStarted) {
            ((DomainInstanceImpl) result).init(camelContext);
        }

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainInstance getDomain(String id) {
        return domains.get(id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DomainInstance> getDomains() {
        return domains.values();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void send(DomainInstance i, Message n) {
        if (Objects.nonNull(i) && Objects.nonNull(n)) {
            i.send(n);
        }
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(),
            f -> {

                final String domainName = f.getAnnotation(DomainRef.class).value();
                if (StringUtils.isBlank(domainName)) {
                    LOGGER.warn("Ignoring @DomainRef annotation with empty name on bean [{}].", beanName);
                    return;
                }

                if (f.getType() != DomainInstance.class) {
                    LOGGER.warn("Ignoring @DomainRef annotation, annotating not a DomainInstance instance on bean [{}].", beanName);
                    return;
                }

                DomainInstance di = domains.get(domainName);
                if (Objects.isNull(di)) {
                    LOGGER.warn("Messaging domain with name [{}] not found and won't be set.", domainName);
                    return;
                }

                boolean wasAccessiible = true;
                if ((!Modifier.isPublic(f.getModifiers())
                  || !Modifier.isPublic(f.getDeclaringClass().getModifiers())
                  ||  Modifier.isFinal(f.getModifiers())) && !f.isAccessible()) { // NOSONAR
                    f.setAccessible(true); // NOSONAR - The rule is good for common case, but not for DI
                    wasAccessiible = false;
                }

                // Caution - the set will cause CCE, if the type parameters do not match.
                f.set(bean, di); // NOSONAR - The rule is good for common case, but not for DI
                if (!wasAccessiible) {
                    f.setAccessible(false);
                }
            },
            f -> f.getAnnotation(DomainRef.class) != null);

        return bean;
    }
}
