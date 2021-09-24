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

package com.huahui.datasphere.mdm.core.type.monitoring.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceImpl;
import com.hazelcast.instance.HazelcastInstanceProxy;
import com.hazelcast.internal.metrics.LongGauge;
import com.hazelcast.internal.metrics.MetricsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class HazelcastInternalsExporters {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastInternalsExporters.class);

    public HazelcastInternalsExporters(final HazelcastInstance hazelcastInstance) {
        final HazelcastInstanceProxy hazelcastInstanceProxy = (HazelcastInstanceProxy) hazelcastInstance;
        try {
            final Field original = HazelcastInstanceProxy.class.getDeclaredField("original");
            original.setAccessible(true);
            final HazelcastInstanceImpl hazelcastInstanceImpl = (HazelcastInstanceImpl) original.get(hazelcastInstance);
            final MetricsRegistry metricsRegistry = hazelcastInstanceImpl.node.nodeEngine.getMetricsRegistry();
            initGauges(metricsRegistry);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Error getting hazelcast internals", e);
        }
    }

    private void initGauges(final MetricsRegistry metricsRegistry) {
        metricsRegistry.getNames().forEach(m -> {
            final LongGauge longGauge = metricsRegistry.newLongGauge(m);
            System.out.println(m + " = " + longGauge.read());
        });
//        final LongGauge longGauge = metricsRegistry.newLongGauge("os.totalSwapSpaceSize");
//        System.out.println();longGauge.read()
    }
}
