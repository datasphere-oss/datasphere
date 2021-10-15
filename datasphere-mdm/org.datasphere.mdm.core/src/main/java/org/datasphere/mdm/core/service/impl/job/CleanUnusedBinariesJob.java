/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package org.datasphere.mdm.core.service.impl.job;

import org.datasphere.mdm.core.configuration.CoreConfigurationProperty;
import org.datasphere.mdm.core.dao.LargeObjectsDAO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.datasphere.mdm.system.service.RuntimePropertiesService;
import org.datasphere.mdm.system.type.configuration.ConfigurationValue;
import org.datasphere.mdm.system.util.ContextUtils;

/**
 * Quartz job for clean unused clob/blob data
 *
 * @author Dmitry Kopin on 10.04.2017
 */
@DisallowConcurrentExecution
public class CleanUnusedBinariesJob extends QuartzJobBean {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("clean-unused-lob-data-job");
    /**
     * RT service.
     */
    @Autowired
    private RuntimePropertiesService runtimePropertiesService;
    /**
     * LOB DAO.
     */
    @Autowired
    private LargeObjectsDAO largeObjectsDAO;

    private void execute() {

        ConfigurationValue<Boolean> jobDisabled = runtimePropertiesService
                .getByProperty(CoreConfigurationProperty.JOB_CLEAN_BINARIES_DISABLED);

        if (jobDisabled.getValue().booleanValue()) {
            return;
        }

        LOGGER.info("Process started.");
        ConfigurationValue<Long> binaryLifetime = runtimePropertiesService
                .getByProperty(CoreConfigurationProperty.JOB_CLEAN_BINARIES_LIFETIME);

        long count = largeObjectsDAO.cleanUnusedBinaryData(binaryLifetime.getValue());
        LOGGER.info("Process finished. Removed [{}] items.", count);
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        // Process @Autowired injection for the given target object,
        // based on the last application context in modular context stack.
        ContextUtils.autowireOrphanBean(this);

        execute();
    }
}
