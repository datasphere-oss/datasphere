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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.service.ConfigurationActionService;
import com.huahui.datasphere.mdm.system.type.action.ConfigurationAction;

/**
 * @author theseusyang
 */
@Service
public class ConfigurationActionServiceImpl implements ConfigurationActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationActionServiceImpl.class);

    private final IMap<String, Integer> actions;

    private static final int MAX_RETRIES = 3;

    private static final long TIMEOUT = 1500L;

    public ConfigurationActionServiceImpl(HazelcastInstance hazelcastInstance) {
        super();
        this.actions = hazelcastInstance.getMap(SystemConfigurationConstants.SYSTEM_ACTIONS_MAP_NAME);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(ConfigurationAction action) {

        Objects.requireNonNull(action, "Configuration action must not be null.");
        Objects.requireNonNull(action.getId(), "Configuration action ID must not be null.");

        for (int attempts = 0; attempts < MAX_RETRIES; attempts++) {

            boolean acquired = false;
            try {

                acquired = actions.tryLock(action.getId(), TIMEOUT, TimeUnit.MILLISECONDS);
                if (!acquired) {
                    continue;
                }

                return doExecute(action);

            } catch (InterruptedException e) {
                LOGGER.warn("Cluster configuration action execution interrupted.", e);
                Thread.currentThread().interrupt();
            } finally {

                if (acquired) {
                    actions.unlock(action.getId());
                }
            }
        }

        return false;
    }
    /*
     * Performs real execution and increases control counter.
     */
    private boolean doExecute(ConfigurationAction action) {

        Integer val = actions.get(action.getId());
        if (action.getTimes() <= (Objects.isNull(val) ? 0 : val.intValue())) {
            return false;
        }

        boolean result = action.execute();
        if (result) {
            actions.set(action.getId(), Integer.valueOf(Objects.isNull(val) ? 1 : val.intValue() + 1));
        }

        return result;
    }
}
