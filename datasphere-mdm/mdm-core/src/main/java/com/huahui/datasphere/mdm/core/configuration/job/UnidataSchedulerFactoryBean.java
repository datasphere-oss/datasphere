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

package com.huahui.datasphere.mdm.core.configuration.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import com.huahui.datasphere.mdm.system.service.AfterModuleStartup;

/**
 * @author theseusyang
 * Thin wrapper around {@link SchedulerFactoryBean} to postpone initialization.
 */
public class UnidataSchedulerFactoryBean extends SchedulerFactoryBean implements AfterModuleStartup {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnidataSchedulerFactoryBean.class);

    public static final String MEMORY_GROUP = "memoryGroup";

    /**
     * Constructor.
     */
    public UnidataSchedulerFactoryBean() {
        super();
    }

    @Override
    public void afterModuleStartup() {
        try {
            super.afterPropertiesSet();
            start();
        } catch (Exception e) {
            LOGGER.error("Scheduler factory afterContextRefresh failed!", e);
        }
    }

    /**
     * Restore previous fire time for jobs with group in memoryGroup
     * @throws SchedulerException
     */
    @Override
    protected void registerJobsAndTriggers() throws SchedulerException {
        Set<TriggerKey> triggersForRestore = getScheduler().getTriggerKeys(GroupMatcher.groupEquals(MEMORY_GROUP));
        Map<TriggerKey, Date> lastFireDatesMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(triggersForRestore)) {
            for (TriggerKey triggerKey : triggersForRestore) {
                lastFireDatesMap.put(triggerKey, getScheduler().getTrigger(triggerKey).getPreviousFireTime());
            }
        }

        super.registerJobsAndTriggers();

        if (CollectionUtils.isNotEmpty(triggersForRestore)) {
            for (TriggerKey triggerKey : triggersForRestore) {
                Trigger trigger = getScheduler().getTrigger(triggerKey);
                if (trigger instanceof CronTriggerImpl) {
                    ((CronTriggerImpl) trigger).setPreviousFireTime(lastFireDatesMap.get(triggerKey));
                    getScheduler().rescheduleJob(triggerKey, trigger);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Quartz scheduler factory started.");
    }
}
