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

package org.datasphere.mdm.core.type.monitoring;


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
