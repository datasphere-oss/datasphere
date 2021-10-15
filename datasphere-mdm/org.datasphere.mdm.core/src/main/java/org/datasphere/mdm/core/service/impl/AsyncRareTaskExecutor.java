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

package org.datasphere.mdm.core.service.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import org.datasphere.mdm.core.configuration.CoreConfigurationProperty;
import org.datasphere.mdm.core.service.AsyncExecutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import org.datasphere.mdm.system.type.annotation.ConfigurationRef;
import org.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import org.datasphere.mdm.system.type.configuration.ConfigurationValue;
import org.datasphere.mdm.system.type.configuration.ConfigurationValueUpdatesListener;

import io.prometheus.client.Counter;

@Service
public class AsyncRareTaskExecutor implements AsyncExecutor, InitializingBean, ConfigurationValueUpdatesListener {

    // TODO: 30.10.2018 move QUEUE_SIZE to config
    public static final int QUEUE_SIZE = 100;
    private static final String METRIC_NAME = "unidata_async_rare_task_executor_queue_size";
    private static final String METRIC_HELP_TEXT = "Unidata async rare task executor queue size";

    private static final String TASKS_SUBMITTED_TO_HANDLE_METRIC_NAME = "udidata_async_rare_task_total";
    private static final String TASKS_SUBMITTED_TO_HANDLE_HELP_TEXT = "Rare tasks' counter";

    private static final Counter TASKS_SUBMITTED_TO_HANDLE_COUNTER = Counter.build()
            .name(TASKS_SUBMITTED_TO_HANDLE_METRIC_NAME)
            .help(TASKS_SUBMITTED_TO_HANDLE_HELP_TEXT)
            .create()
            .register();

    private static final CustomizableThreadFactory THREAD_FACTORY =
            new CustomizableThreadFactory("UnidataRareTaskExecutor");

    private ThreadPoolExecutor threadPoolExecutor;

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_ASYNC_TASK_EXECUTOR_POOL_SIZE)
    private ConfigurationValue<Long> poolSize;

    public AsyncRareTaskExecutor() {
        super();
        // TODO @Modules
        // QueueSizeCollector.createAndRegister(workQueue, METRIC_NAME, METRIC_HELP_TEXT);
    }

    @Override
    public void afterPropertiesSet() {
        final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        threadPoolExecutor = new InternalThreadPoolExecutor(
                poolSize.getValue().intValue(),
                workQueue,
                THREAD_FACTORY,
                TASKS_SUBMITTED_TO_HANDLE_COUNTER);
    }

    @Override
    public <T> CompletableFuture<T> async(final Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, threadPoolExecutor);
    }

    @Override
    public CompletableFuture<Void> async(final Runnable runnable) {
        return CompletableFuture.runAsync(runnable, threadPoolExecutor);
    }

    @Override
    public void execute(@Nonnull final Runnable command) {
        threadPoolExecutor.execute(command);
    }

    @Override
    public void configurationValueUpdated(ConfigurationProperty<?> p) {

        if (p != CoreConfigurationProperty.CORE_ASYNC_TASK_EXECUTOR_POOL_SIZE) {
            return;
        }

        threadPoolExecutor.setCorePoolSize(poolSize.getValue().intValue());
        threadPoolExecutor.setMaximumPoolSize(poolSize.getValue().intValue());
    }

    // Called from Module's stop()
    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    private static class InternalThreadPoolExecutor extends ThreadPoolExecutor {
        private final Counter counter;
        InternalThreadPoolExecutor(
                final int poolSize,
                final BlockingQueue<Runnable> workQueue,
                final ThreadFactory threadFactory,
                final Counter counter
        ) {
            super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, workQueue, threadFactory);
            this.counter = counter;
        }

        @Override
        public void execute(Runnable command) {
            counter.inc();
            super.execute(command);
        }
    }
}
