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

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.type.event.Event;

/**
 * Simple replay counter.
 * @author theseusyang on Oct 30, 2019
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
