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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.huahui.datasphere.system.service.ConfigurationActionService;
import com.huahui.datasphere.system.type.action.ConfigurationAction;

/**
 * @author Alexander Malyshev
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
