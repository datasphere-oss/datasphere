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

package com.huahui.datasphere.mdm.system.service;

import java.util.Collection;

import org.springframework.beans.factory.config.BeanPostProcessor;

import com.huahui.datasphere.mdm.system.type.messaging.DomainInstance;
import com.huahui.datasphere.mdm.system.type.messaging.DomainType;
import com.huahui.datasphere.mdm.system.type.messaging.Message;

/**
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
