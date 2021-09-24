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

import com.huahui.datasphere.mdm.system.type.event.Event;
import com.huahui.datasphere.mdm.system.type.event.EventReceiver;
/**
 * The platform event service - simple wrapper around two HZ topics.
 */
public interface EventService {
    /**
     * Fire an event without reply.
     * @param event the event
     */
    void fire(Event event);
    /**
     * Fire an event and wait for 'receive' reply from all receivers or timeout.
     * @param event the event
     * @return true if the event was successfully processed by all receivers, false, if the timeout occured earlier
     */
    boolean fireAndWait(Event event);
    /**
     * Fire an event and wait for 'receive' reply from all receivers or timeout.
     * @param event the event
     * @param timeout the timeout in millis
     * @return true if the event was successfully processed by all receivers, false, if the timeout occured earlier
     */
    boolean fireAndWait(Event event, long timeout);
    /**
     * Receive ALL events. Not to be used by ordinary receivers but system ones (audit, distributed logging etc.)
     * @param subscriber the receiver
     */
    void register(EventReceiver subscriber);
    /**
     * Unregister system receiver to receive ALL events.
     * @param subscriber the receiver
     */
    void unregister(EventReceiver subscriber);
    /**
     * Receive only events of particular type. This should normally be used for everything.
     * @param <T> the event type
     * @param subscriber the receiver
     * @param eventType the event type class
     */
    <T extends Event> void register(EventReceiver subscriber, Class<T> eventType);
    /**
     * Unregister rceiver.
     * @param <T> the event type
     * @param subscriber the receiver
     * @param eventType the event type class
     */
    <T extends Event> void unregister(EventReceiver subscriber, Class<T> eventType);
}
