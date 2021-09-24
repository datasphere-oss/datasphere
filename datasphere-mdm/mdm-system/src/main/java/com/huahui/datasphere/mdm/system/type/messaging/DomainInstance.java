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

import java.util.Collection;

import javax.annotation.Nonnull;
/**
 * Messaging domain instance.
 * @author theseusyang on Jul 10, 2020
 */
public interface DomainInstance {
    /**
     * Domain id.
     * @return domain id unique accross the platform
     */
    String getId();
    /**
     * A possibly localized domain description.
     * @return description string
     */
    String getDescription();
    /**
     * Camel routes XML - current content.
     * @return initial routes configuration
     */
    String getContent();
    /**
     * Gets the defining type.
     * @return type descriptor
     */
    DomainType getType();
    /**
     * Gets the currently registered message types.
     * @return message types
     */
    Collection<MessageType> getMessageTypes();
    /**
     * Gets headers of a registered message type.
     * @param type the message type
     * @return collection of headers
     */
    Collection<Header> getHeadersByMessageType(MessageType type);
    /**
     * Adds a new message type to this domain.
     * @param type the new message type
     */
    void add(@Nonnull MessageType type);
    /**
     * Adds a new message type to this domain.
     * Just registers additional headers for this type, if the type is already registered.
     * @param type the new message type
     * @param headers the headers collection to append
     */
    void add(@Nonnull MessageType type, Collection<Header> headers);
    /**
     * Sends a notification through this domain instance.
     * Will just return on null input.
     * Will discard message, if the type is null.
     * Will discard also unknown headers.
     * @param n the message to send
     */
    void send(Message n);
}
