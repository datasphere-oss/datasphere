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

import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.IAtomicLong;
import com.huahui.datasphere.mdm.system.type.event.AbstractMulticastEvent;
import com.huahui.datasphere.mdm.system.type.event.Event;
import com.huahui.datasphere.mdm.system.type.event.EventReceiver;

/**
 * Simple subscription info holder.
 * @author theseusyang on Oct 30, 2019
 */
public class ReceiverWorker {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverWorker.class);
    /**
     * Known subscriber count.
     */
    private final IAtomicLong subscriberCount;
    /**
     * Receivers, registered locally.
     */
    private final CopyOnWriteArraySet<EventReceiver> receivers;
    /**
     * Constructor.
     * @param subscriberCount
     */
    public ReceiverWorker(IAtomicLong subscriberCount) {
        this.subscriberCount = subscriberCount;
        this.receivers = new CopyOnWriteArraySet<>();
    }
    /**
     * Returns the currently known number of subscribers cluster-wide.
     * @return count
     */
    public int knownCount() {
        return (int) subscriberCount.get();
    }

    public void subscribe(EventReceiver receiver) {
        receivers.add(receiver);
        subscriberCount.incrementAndGet();
    }

    public void unsubscribe(EventReceiver receiver) {
        receivers.remove(receiver);
        subscriberCount.decrementAndGet();
    }

    public int receive(Event event, boolean fromLocalMember) {

        switch (event.getBroadcastType()) {
        case FOREIGN:
            if (fromLocalMember) {
                return receivers.size();
            }
            break;
        case LOCAL:
            if (!fromLocalMember) {
                return receivers.size();
            }
            break;
        case BOTH:
            ((AbstractMulticastEvent) event).setLocal(fromLocalMember);
            break;
        }

        int count = 0;
        for (EventReceiver receiver : receivers) {
            try {
                receiver.receive(event);
            } catch (Exception e) {
                LOGGER.warn("Exception caught, while receiving event [{}].", event, e);
            } finally {
                count++;
            }
        }

        return count;
    }
}
