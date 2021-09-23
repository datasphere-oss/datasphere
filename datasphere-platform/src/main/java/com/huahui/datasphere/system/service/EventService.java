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

import com.huahui.datasphere.system.type.event.Event;
import com.huahui.datasphere.system.type.event.EventReceiver;
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
