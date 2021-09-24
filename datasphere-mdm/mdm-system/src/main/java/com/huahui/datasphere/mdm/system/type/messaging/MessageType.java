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
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.mdm.system.type.messaging.Header.HeaderType;
import com.huahui.datasphere.mdm.system.util.TextUtils;

/**
 * @author theseusyang on Jul 6, 2020
 * Hash code and equals are calculated using the id field only.
 * Thus, you won't be able to override a registered message type with a new one with the same name but a different set of other fields.
 */
public class MessageType {
    /**
     * This type id.
     */
    private final String id;
    /**
     * This type description.
     */
    private String description;
    /**
     * An optional subsystem mark.
     */
    private String subsystem;
    /**
     * Set of headers.
     */
    private final Set<Header> headers = new HashSet<>();
    /**
     * Constructor.
     * @param id this type id
     */
    public MessageType(@Nonnull String id) {
        super();

        Objects.requireNonNull(id, "Message type id must not be null.");
        this.id = id;
    }
    /**
     * Gets the id of this message type.
     * @return id of the message type
     */
    public String getId() {
        return id;
    }
    /**
     * Description of this message type.
     * @return description
     */
    public String getDescription() {
        return Objects.isNull(description) ? StringUtils.EMPTY : TextUtils.getText(description);
    }
    /**
     * Gets an optional subsytem tag.
     * @return the subsystem
     */
    @Nullable
    public String getSubsystem() {
        return subsystem;
    }
    /**
     * Collection of headers.
     * Additional headers can be registered for each value by
     * @return collection of headers.
     */
    public Collection<Header> getHeaders() {
        return headers;
    }
    /**
     * Adds description to this message type.
     * @param description the description
     * @return self
     */
    public MessageType withDescription(String description) {
        this.description = description;
        return this;
    }
    /**
     * Adds subsystem mark to this message type.
     * @param subsystem the subsystem mark
     * @return self
     */
    public MessageType withSubsystem(String subsystem) {
        this.subsystem = subsystem;
        return this;
    }
    /**
     * Adds a header to this message type.
     * @param headers the headers
     * @return self
     */
    public MessageType withHeader(String name, HeaderType type) {
        return withHeaders(Collections.singletonList(new Header(name, type)));
    }
    /**
     * Adds headers to this message type.
     * @param headers the headers
     * @return self
     */
    public MessageType withHeaders(Header... headers) {
        return withHeaders(Arrays.asList(headers));
    }
    /**
     * Adds headers to this message type.
     * @param headers the headers
     * @return self
     */
    public MessageType withHeaders(Collection<Header> headers) {
        this.headers.addAll(headers);
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        MessageType other = (MessageType) obj;
        return id.equals(other.id);
    }
}
