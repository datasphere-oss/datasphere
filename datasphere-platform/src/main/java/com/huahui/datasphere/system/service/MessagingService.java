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

package com.huahui.datasphere.system.service;

import java.util.Collection;

import org.springframework.beans.factory.config.BeanPostProcessor;

import com.huahui.datasphere.system.type.messaging.DomainInstance;
import com.huahui.datasphere.system.type.messaging.DomainType;
import com.huahui.datasphere.system.type.messaging.Message;

/**
 * @author Mikhail Mikhailov on Jun 29, 2020
 * A service, serving notification domains.
 * Notification domains are essentially collections of apache camel routes,
 * that can be addressed using notification domain name.
 * The service can be used for various purposes, including external messaging, using all kinds of transport, that Apache Camel supports.
 * Among others, this service is used as the transport for system audit and user notifications by the platform.
 */
public interface MessagingService extends BeanPostProcessor, ModulePostProcessor, AfterPlatformStartup {
    /**
     * Registers a notification domain.
     * @param type the domain type
     * @throws {@link PlatformFailureException} if a domain with the given id can't be registerd
     */
    DomainInstance register(DomainType type);
    /**
     * Returns current state of a notification domain by id.
     * @param id the domain id
     * @return {@link DomainInstance} or null, if such a domain was not registered
     */
    DomainInstance getDomain(String id);
    /**
     * Gets all registerd domain instances.
     * @return collection of registered domains
     */
    Collection<DomainInstance> getDomains();
    /**
     * Attempts to send a notification via given domain instance.
     * Operation will fail, if notification message type is not registered at this domain.
     * @param i a domain instance to send the message though
     * @param n the message
     */
    void send(DomainInstance i, Message n);
}
