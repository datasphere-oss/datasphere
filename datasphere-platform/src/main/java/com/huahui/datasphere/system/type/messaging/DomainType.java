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
package com.huahui.datasphere.system.type.messaging;

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
 * @author Mikhail Mikhailov on Jul 10, 2020
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
