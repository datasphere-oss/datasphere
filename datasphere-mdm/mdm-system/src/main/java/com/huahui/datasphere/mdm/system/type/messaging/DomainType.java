/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.huahui.datasphere.mdm.system.type.messaging;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

/**
 * A messaging domain is, essentially, a number of Apache Camel routes, grouped by a system-wide ID.
 * Route definitions can be edited from the UI and get saved to configuration, thus overwriting initial content.
 * @author theseusyang on Jul 10, 2020
 */
public class DomainType {
    /**
     * Empty domains array.
     */
    public static final DomainType[] EMPTY_DOMAINS_ARRAY = new DomainType[0];
    /**
     * The domain id.
     */
    private final String id;
    /**
     * Domain description.
     */
    private String description;
    /**
     * Initial content. May be overwritten by configuration.
     */
    private final String content;
    /**
     * Exposed types.
     */
    private final Set<MessageType> messageTypes = new HashSet<>();
    /**
     * Constructor.
     */
    protected DomainType(String id, String content) {
        super();
        this.id = id;
        this.content = content;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return Objects.isNull(description) ? StringUtils.EMPTY : description;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @return the messageTypes
     */
    public Set<MessageType> getMessageTypes() {
        return messageTypes;
    }
    /**
     * Adds description string to this domain type.
     * @param description the description string
     * @return self
     */
    public DomainType withDescription(String description) {
        this.description = description;
        return this;
    }
    /**
     * Adds message types to this domain type.
     * @param types the types
     * @return self
     */
    public DomainType withMessageTypes(MessageType... types) {
        return withMessageTypes(Arrays.asList(types));
    }
    /**
     * Adds message types to this domain type.
     * @param types the types
     * @return self
     */
    public DomainType withMessageTypes(Collection<MessageType> types) {
        messageTypes.addAll(types);
        return this;
    }
    /**
     * Creates a MDD from three sttrings.
     * @param id the MDD ID
     * @param content the content (routes)
     * @return MDD
     */
    public static @Nonnull DomainType of(String id, String content) {
        Objects.requireNonNull(id, "Messaging domain ID must not be null.");
        Objects.requireNonNull(content, "Messaging domain content must not be null.");
        return new DomainType(id, content);
    }
    /**
     * Creates a MDD from three sttrings.
     * @param id the MDD ID
     * @param content the content (routes)
     * @return MDD
     */
    public static @Nonnull DomainType ofLocalized(String id, String content) {
        Objects.requireNonNull(id, "Messaging domain ID must not be null.");
        Objects.requireNonNull(content, "Messaging domain content must not be null.");
        return new LocalizedDomainType(id, content);
    }
}
