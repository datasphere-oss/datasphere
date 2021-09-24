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

package com.huahui.datasphere.mdm.system.service.impl.event;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.hazelcast.core.HazelcastInstance;
import com.huahui.datasphere.mdm.system.type.event.Event;
import com.huahui.datasphere.mdm.system.type.event.EventReceiver;

/**
 * Simple subscription registry.
 * @author theseusyang on Oct 30, 2019
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
