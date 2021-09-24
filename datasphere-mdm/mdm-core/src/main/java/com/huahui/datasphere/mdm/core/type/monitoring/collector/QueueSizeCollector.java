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

package com.huahui.datasphere.mdm.core.type.monitoring.collector;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

public class QueueSizeCollector extends Collector {

    private final Queue<?> queue;

    private final String metricName;
    private final String metricHelpText;

    public static void createAndRegister(final Queue<?> queue, final String metricName, final String metricHelpText) {
        new QueueSizeCollector(queue, metricName, metricHelpText).register();
    }

    public QueueSizeCollector(final Queue<?> queue, final String metricName, final String metricHelpText) {
        this.queue = queue;
        this.metricName = metricName;
        this.metricHelpText = metricHelpText;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        return Collections.singletonList(new GaugeMetricFamily(metricName, metricHelpText, queue.size()));
    }
}
