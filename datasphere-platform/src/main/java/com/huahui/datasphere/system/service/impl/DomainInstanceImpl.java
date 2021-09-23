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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ModelHelper;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.spi.RouteController;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;
import org.unidata.mdm.system.module.SystemModule;

import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.service.RuntimePropertiesService;
import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.messaging.DomainInstance;
import com.huahui.datasphere.system.type.messaging.DomainType;
import com.huahui.datasphere.system.type.messaging.Header;
import com.huahui.datasphere.system.type.messaging.Message;
import com.huahui.datasphere.system.type.messaging.MessageType;
import com.huahui.datasphere.system.type.messaging.SystemHeaders;

/**
 * The routes definition holder.
 * @author Mikhail Mikhailov on Jul 6, 2020
 */
class DomainInstanceImpl implements DomainInstance {
    /**
     * This domain instance logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainInstanceImpl.class);
    /**
     * Unknown system field value.
     */
    private static final String UNKNOWN = "unknown";
    /**
     * The target name.
     * In Camel tradition direct: + domain ID.
     */
    private final String target;
    /**
     * The routes to hold.
     */
    private final DomainType type;
    /**
     * Runtime properties service instance.
     */
    private final RuntimePropertiesService runtimePropertiesService;
    /**
     * Current routes.
     */
    private final Set<String> routes = new HashSet<>();
    /**
     * Types to registered headers mappings.
     */
    private final Map<MessageType, Set<Header>> mappings = new HashMap<>();
    /**
     * The camel context.
     */
    private ModelCamelContext modelCamelContext;
    /**
     * Constructor.
     * @param type the domain to hold
     */
    public DomainInstanceImpl(DomainType type, RuntimePropertiesService runtimePropertiesService) {
        super();
        this.type = type;
        this.target = "direct:" + type.getId();
        this.runtimePropertiesService = runtimePropertiesService;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return type.getId();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return type.getDescription();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getContent() {
        return type.getContent();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getType() {
        return type;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MessageType> getMessageTypes() {
        return mappings.keySet();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Header> getHeadersByMessageType(MessageType type) {
        Collection<Header> headers = mappings.get(type);
        return Objects.isNull(headers) ? Collections.emptyList() : headers;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(MessageType type) {
        add(type, type.getHeaders());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(MessageType type, Collection<Header> headers) {
        Objects.requireNonNull(type, "Message type must not be null.");
        mappings.computeIfAbsent(type, k -> new HashSet<>()).addAll(headers);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Message n) {

        // Nothing
        if (Objects.isNull(n) || Objects.isNull(n.getType())) {
            return;
        }

        Collection<Header> headerTypes = mappings.get(n.getType());

        // Not registered
        if (Objects.isNull(headerTypes)) {
            return;
        }

        // Verify headers. Discard unregistered.
        final Map<String, Object> headers = new HashMap<>();
        for (Header h : headerTypes) {
            Object v = n.getHeaders().get(h);
            if (Objects.nonNull(v)) {
                headers.put(h.getName(), v);
            }
        }

        // Add system headers
        addSystemInfo(headers, n);

        // Send
        try (final ProducerTemplate producerTemplate = modelCamelContext.createProducerTemplate()) {
            producerTemplate.sendBodyAndHeaders(this.target, n.getBody(), headers);
        } catch (IOException e) {
            LOGGER.error("Can't send message to {}", target, e);
        }
    }
    /**
     * Performs init.
     */
    public void init(ModelCamelContext context) {

        Objects.requireNonNull(context, "Camel context cannot be null.");
        modelCamelContext = context;

        try {

            type.getMessageTypes().forEach(t -> add(t, t.getHeaders()));
            ConfigurationProperty<String> p = ConfigurationProperty.string()
                    .key(SystemModule.MODULE_ID + ".messaging.domains." + type.getId())
                    .groupKey(SystemConfigurationConstants.PROPERTY_MESSAGING_GROUP)
                    .moduleId(SystemModule.MODULE_ID)
                    .defaultValue(type.getContent())
                    .setter(this::addRoutesToContext)
                    .readOnly(false)
                    .build();

            runtimePropertiesService.load(Collections.singleton(p));

        } catch (Exception e) {
            throw new PlatformFailureException("Failed to register messaging domain ID '{}'.",
                    SystemExceptionIds.EX_SYSTEM_DOMAIN_INITIALIZATION_FAILED,
                    type.getId());
        }
    }

    private void addRoutesToContext(final String content) {

        try {

            // 1. Stop possibly existing stuff
            stopRoutes();

            if (StringUtils.isBlank(content)) {
                return;
            }

            // 2. Run update
            addRoutes(content);

        } catch (Exception e) {
            LOGGER.error("Can't add messaging domain '{}' definition {} to context", type.getId(), content, e);
            throw new PlatformFailureException("Domain initialization with ID '{}' and content [{}] failed.",
                    SystemExceptionIds.EX_SYSTEM_DOMAIN_STARTUP_FAILED,
                    type.getId(), content);
        }
    }

    private void stopRoutes() throws Exception {

        if (CollectionUtils.isEmpty(routes)) {
            return;
        }

        final RouteController routeController = modelCamelContext.getRouteController();
        for (String routeId : routes) {
            routeController.stopRoute(routeId);
            modelCamelContext.removeRoute(routeId);
        }

        routes.clear();
    }

    private void addRoutes(String content) throws Exception {

        RoutesDefinition def
            = ModelHelper.loadRoutesDefinition(modelCamelContext,
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));

        List<String> collected = new ArrayList<>();
        for (RouteDefinition r : def.getRoutes()) {
            modelCamelContext.addRouteDefinition(r);
            collected.add(r.getRouteId());
        }

        routes.addAll(collected);
    }

    private void addSystemInfo(Map<String, Object> headers, Message message) {

        // 1. Login
        Object login = message.getHeaders().get(SystemHeaders.LOGIN);
        if (Objects.isNull(login)) {
            login = ReflectionCalls.getCurrentUserName();
        }
        headers.put(SystemHeaders.LOGIN.getName(), login);

        // 2. Server IP
        Object serverIp = message.getHeaders().get(SystemHeaders.SERVER_IP);
        if (Objects.isNull(serverIp)) {
            String tokenServerIp = ReflectionCalls.getCurrentUserServerIp();
            serverIp = tokenServerIp != null ? tokenServerIp : UNKNOWN;
        }
        headers.put(SystemHeaders.SERVER_IP.getName(), serverIp);

        // 3. Client IP
        Object clientIp = message.getHeaders().get(SystemHeaders.CLIENT_IP);
        if (Objects.isNull(clientIp)) {
            String tokenClientIp = ReflectionCalls.getCurrentUserClientIp();
            clientIp = tokenClientIp != null ? tokenClientIp : UNKNOWN;
        }
        headers.put(SystemHeaders.CLIENT_IP.getName(), clientIp);

        // 4. Endpoint
        Object endpoint = message.getHeaders().get(SystemHeaders.ENDPOINT);
        if (Objects.isNull(endpoint)) {
            String tokenEndpoint = ReflectionCalls.getCurrentUserEndpoint();
            endpoint = tokenEndpoint != null ? tokenEndpoint : UNKNOWN;
        }
        headers.put(SystemHeaders.ENDPOINT.getName(), endpoint);

        // 5. Others
        headers.put(SystemHeaders.WHEN_HAPPENED.getName(), Instant.now());
        headers.put(SystemHeaders.TYPE.getName(), message.getType().getId());
        headers.put(SystemHeaders.DOMAIN.getName(), type.getId());

        // 6. Exceptions/Errors
        Throwable cause = message.getCause();
        if (Objects.nonNull(cause)) {
            headers.put(SystemHeaders.THROWABLE.getName(), cause);
        }
    }
}