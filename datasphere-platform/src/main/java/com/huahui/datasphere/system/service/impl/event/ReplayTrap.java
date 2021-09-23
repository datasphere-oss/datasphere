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

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.type.event.Event;

/**
 * Simple replay counter.
 * @author Mikhail Mikhailov on Oct 30, 2019
 */
public class ReplayTrap {
    /**
     * The registry.
     */
    private final ReceiverRegistry registry;
    /**
     * Replay tracker.
     */
    private final ConcurrentHashMap<String, CountDownLatch> replays;
    /**
     * Constructor.
     */
    public ReplayTrap(final ReceiverRegistry registry) {
        super();
        this.registry = registry;
        this.replays = new ConcurrentHashMap<>();
    }
    /**
     * Gets a latch, initialized with the number of active subscribers cluster - wide.
     * @param event the event to set up the latch for
     * @return latch or null, if there're 0 active subscribers for the event type
     */
    public CountDownLatch init(Event event) {

        if (Objects.isNull(event.getId())) {
            throw new PlatformFailureException("No valid id for event [{}].",
                    SystemExceptionIds.EX_EVENT_NO_VALID_ID, event.getClass().getName());
        }

        if (replays.containsKey(event.getId())) {
            throw new PlatformFailureException("Already waiting for event [{}].",
                    SystemExceptionIds.EX_EVENT_ALREADY_WAITING, event.getId());
        }

        ReceiverWorker s = registry.info(event);
        if (s.knownCount() == 0) {
            return null;
        }

        CountDownLatch latch = new CountDownLatch(s.knownCount());
        replays.put(event.getId(), latch);

        return latch;
    }
    /**
     * Must be called from finally.
     * @param event the event
     */
    public void fini(Event event) {
        replays.remove(event.getId());
    }
    /**
     * Reacts to replay event
     * @param event the event
     */
    public void replay(Event event) {
        CountDownLatch latch = replays.get(event.getId());
        if (Objects.nonNull(latch)) {
            SystemReplayEvent sys = (SystemReplayEvent) event;
            for (int i = 0; i < sys.getReceiverCount(); i++) {
                latch.countDown();
            }
        }
    }
}
