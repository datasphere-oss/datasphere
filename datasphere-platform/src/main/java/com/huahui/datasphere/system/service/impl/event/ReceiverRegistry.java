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

package com.huahui.datasphere.system.service.impl.event;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.hazelcast.core.HazelcastInstance;
import com.huahui.datasphere.system.type.event.Event;
import com.huahui.datasphere.system.type.event.EventReceiver;

/**
 * Simple subscription registry.
 * @author Mikhail Mikhailov on Oct 30, 2019
 */
public class ReceiverRegistry {

    private final Map<Class<? extends Event>, ReceiverWorker> classified;

    private final HazelcastInstance hazelcastInstance;

    public ReceiverRegistry(final HazelcastInstance hazelcastInstance) {
        super();
        this.hazelcastInstance = hazelcastInstance;
        this.classified = new ConcurrentHashMap<>();
    }

    public ReceiverWorker info(Event event) {
        return classified.computeIfAbsent(
                event.getClass(),
                type -> new ReceiverWorker(hazelcastInstance.getAtomicLong(type.getClass().getName() + "#S")));
    }

    public<T extends Event> void register(EventReceiver subscriber, Class<T> eventType) {

        if (Objects.isNull(subscriber) || Objects.isNull(eventType)) {
            return;
        }

        classified.computeIfAbsent(
                eventType,
                type -> new ReceiverWorker(hazelcastInstance.getAtomicLong(eventType.getClass().getName() + "#S")))
            .subscribe(subscriber);
    }

    public void register(EventReceiver subscriber) {

        if (Objects.isNull(subscriber)) {
            return;
        }

        classified.keySet().forEach(key -> register(subscriber, key));
    }

    public <T extends Event> void unregister(EventReceiver subscriber, Class<T> eventType) {
        classified.computeIfPresent(eventType, (k, v) -> { v.unsubscribe(subscriber); return v; });
    }

    public void unregister(EventReceiver subscriber) {
        classified.keySet().forEach(key -> unregister(subscriber, key));
    }

    public int receive(Event event, boolean fromLocalMember) {
        ReceiverWorker info = info(event);
        return info.receive(event, fromLocalMember);
    }
}
