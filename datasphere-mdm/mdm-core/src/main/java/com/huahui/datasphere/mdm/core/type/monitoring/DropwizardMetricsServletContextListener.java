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

package com.huahui.datasphere.mdm.core.type.monitoring;


import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.jvm.ClassLoadingGaugeSet;
import io.dropwizard.metrics5.jvm.GarbageCollectorMetricSet;
import io.dropwizard.metrics5.jvm.MemoryUsageGaugeSet;
import io.dropwizard.metrics5.jvm.ThreadStatesGaugeSet;
import io.dropwizard.metrics5.servlets.MetricsServlet;

public class DropwizardMetricsServletContextListener extends MetricsServlet.ContextListener {

    private static final MetricRegistry METRICS_REGISTRY = new MetricRegistry();

    public DropwizardMetricsServletContextListener() {
        final MemoryUsageGaugeSet memoryUsageGaugeSet = new MemoryUsageGaugeSet();
        METRICS_REGISTRY.registerAll(memoryUsageGaugeSet);

        final GarbageCollectorMetricSet garbageCollectorMetricSet = new GarbageCollectorMetricSet();
        METRICS_REGISTRY.registerAll(garbageCollectorMetricSet);

//        final CpuTimeClock cpuTimeClock = new CpuTimeClock();
//        METRICS_REGISTRY.timer("cpu");

        final ThreadStatesGaugeSet threadStatesGaugeSet = new ThreadStatesGaugeSet();
        METRICS_REGISTRY.registerAll(threadStatesGaugeSet);

        final ClassLoadingGaugeSet classLoadingGaugeSet = new ClassLoadingGaugeSet();
        METRICS_REGISTRY.registerAll(classLoadingGaugeSet);
    }

    @Override
    protected MetricRegistry getMetricRegistry() {
        return METRICS_REGISTRY;
    }
}
