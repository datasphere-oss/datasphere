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

import java.util.Collection;

import javax.annotation.Nonnull;
/**
 * Messaging domain instance.
 * @author Mikhail Mikhailov on Jul 10, 2020
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
