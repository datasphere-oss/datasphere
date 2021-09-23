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

import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.IAtomicLong;
import com.huahui.datasphere.system.type.event.AbstractMulticastEvent;
import com.huahui.datasphere.system.type.event.Event;
import com.huahui.datasphere.system.type.event.EventReceiver;

/**
 * Simple subscription info holder.
 * @author Mikhail Mikhailov on Oct 30, 2019
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
