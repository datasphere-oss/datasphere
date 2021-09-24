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

package com.huahui.datasphere.mdm.system.service.impl;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.hazelcast.topic.impl.reliable.ReliableMessageListenerAdapter;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.service.EventService;
import com.huahui.datasphere.mdm.system.service.impl.event.ReceiverRegistry;
import com.huahui.datasphere.mdm.system.service.impl.event.ReplayTrap;
import com.huahui.datasphere.mdm.system.service.impl.event.SystemReplayEvent;
import com.huahui.datasphere.mdm.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.mdm.system.type.event.AbstractEvent;
import com.huahui.datasphere.mdm.system.type.event.Event;
import com.huahui.datasphere.mdm.system.type.event.EventReceiver;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private static final String OUT_TOPIC_NAME = EventService.class.getName() + "#OUT";

    private static final String IN_TOPIC_NAME = EventService.class.getName() + "#IN";

    private final ITopic<Event> outTopic;

    private final ITopic<Event> inTopic;

    private final ReceiverRegistry registry;

    private final ReplayTrap trap;

    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_EVENT_REPLAY_TIMEOUT)
    private ConfigurationValue<Long> defaultReplayTimeout;

    public EventServiceImpl(final HazelcastInstance hazelcastInstance) {
        super();
        this.outTopic = hazelcastInstance.getTopic(OUT_TOPIC_NAME);
        this.inTopic = hazelcastInstance.getTopic(IN_TOPIC_NAME);
        this.registry = new ReceiverRegistry(hazelcastInstance);
        this.trap = new ReplayTrap(registry);

        outTopic.addMessageListener(new ReliableMessageListenerAdapter<>(new OutMessageListener()));
        inTopic.addMessageListener(new ReliableMessageListenerAdapter<>(new InMessageListener()));
    }

    @Override
    public void fire(final Event event) {
        outTopic.publish(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean fireAndWait(Event event, long timeout) {

        // This event is not replay capable. Fire and forget.
        if (!event.withReplay() && !(event instanceof AbstractEvent)) {
            fire(event);
            return true;
        }

        if (event instanceof AbstractEvent) {
            ((AbstractEvent) event).setReplay(true);
        }

        CountDownLatch latch = trap.init(event);
        if (Objects.isNull(latch)) {
            // No known subscribers, just return.
            return true;
        }

        fire(event);
        try {
            return latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("InterruptedException caught while waiting for replay from subscribers", e);
        } finally {
            trap.fini(event);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean fireAndWait(Event event) {
        return fireAndWait(event, defaultReplayTimeout.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(EventReceiver subscriber) {
        registry.register(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> void register(EventReceiver subscriber, Class<T> eventType) {
        registry.register(subscriber, eventType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(EventReceiver subscriber) {
        registry.unregister(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> void unregister(EventReceiver subscriber, Class<T> eventType) {
        registry.unregister(subscriber, eventType);
    }
    /**
     * The output topic listener.
     * @author theseusyang on Oct 30, 2019
     */
    private class OutMessageListener implements MessageListener<Event> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onMessage(Message<Event> message) {

            Event evt = message.getMessageObject();
            int processed = registry.receive(evt, message.getPublishingMember().localMember());
            // Receive.
            // This replays also to locally / foreign filtered events.
            // See {@SubscriptionInfo#receive for details.
            if (evt.withReplay()) {
                inTopic.publish(new SystemReplayEvent(evt.getId(), processed));
            }
        }
    }
    /**
     * Replay topic listener.
     * @author theseusyang on Oct 30, 2019
     */
    private class InMessageListener implements MessageListener<Event> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onMessage(Message<Event> message) {
            trap.replay(message.getMessageObject());
        }
    }
}
