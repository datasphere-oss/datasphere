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

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * The header. Can be added to a {@link MessageType}.
 * Headers from {@link SystemHeaders} are added to each message automatically.
 * Hash code and equals are calculated using the name field only.
 * Thus, for a message type, you won't be able to override a registered header with a header with the same name but a different type.
 * @author theseusyang on Jul 6, 2020
 */
public class Header {
    /**
     * Type of the header's value.
     * @author theseusyang on Jul 6, 2020
     */
    public enum HeaderType {
        STRING,
        INTEGER,
        BOOLEAN,
        INSTANT,
        OBJECT
    }
    /**
     * The header name.
     */
    private final String name;
    /**
     * The header type.
     */
    private final HeaderType type;
    /**
     * Constructor.
     * @param name the header name
     * @param type the header type
     */
    public Header(@Nonnull String name, @Nonnull HeaderType type) {
        super();

        Objects.requireNonNull(name, "Headers name must not be null.");
        Objects.requireNonNull(type, "Headers type must not be null.");
        this.name = name;
        this.type = type;
    }
    /**
     * Gets the name of the header.
     * @return the name of the header
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the type of the header's value.
     * @return type of the value
     */
    public HeaderType getType() {
        return type;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return name.hashCode();
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
        Header other = (Header) obj;
        if (!name.equals(other.name))
            return false;
        return true;
    }
}
