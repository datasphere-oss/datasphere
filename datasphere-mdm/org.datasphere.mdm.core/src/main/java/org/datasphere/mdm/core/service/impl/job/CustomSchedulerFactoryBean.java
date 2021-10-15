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

package org.datasphere.mdm.core.service.impl.job;

import org.quartz.CronTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.datasphere.mdm.system.service.AfterPlatformStartup;

/**
 * @author Mikhail Mikhailov
 * Thin wrapper around {@link SchedulerFactoryBean} to postpone initialization.
 */
public class CustomSchedulerFactoryBean extends SchedulerFactoryBean implements AfterPlatformStartup {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSchedulerFactoryBean.class);
    /**
     * CronTrigger beans collector.
     */
    private CustomCronTriggerBeanPostProcessor collector;
    /**
     * Constructor.
     */
    public CustomSchedulerFactoryBean() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPlatformStartup() {

        try {

            setTriggers(collector.getCollected().toArray(CronTrigger[]::new));
            super.afterPropertiesSet();
            start();
            collector.getCollected().clear();

        } catch (Exception e) {
            LOGGER.error("Scheduler factory afterPlatformStartup failed!", e);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Quartz scheduler factory started.");
    }
    /**
     * @param collector the collector to set
     */
    public void setCollector(CustomCronTriggerBeanPostProcessor collector) {
        this.collector = collector;
    }
}
