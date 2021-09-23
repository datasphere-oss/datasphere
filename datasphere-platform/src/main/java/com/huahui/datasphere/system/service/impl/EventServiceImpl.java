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

package com.huahui.datasphere.system.service.impl;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.hazelcast.topic.impl.reliable.ReliableMessageListenerAdapter;
import com.huahui.datasphere.system.service.EventService;
import com.huahui.datasphere.system.service.impl.event.ReceiverRegistry;
import com.huahui.datasphere.system.service.impl.event.ReplayTrap;
import com.huahui.datasphere.system.service.impl.event.SystemReplayEvent;
import com.huahui.datasphere.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.system.type.event.AbstractEvent;
import com.huahui.datasphere.system.type.event.Event;
import com.huahui.datasphere.system.type.event.EventReceiver;

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
     * @author Mikhail Mikhailov on Oct 30, 2019
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
     * @author Mikhail Mikhailov on Oct 30, 2019
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
