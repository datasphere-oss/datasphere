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

import com.hazelcast.core.Cluster;
import com.hazelcast.core.HazelcastInstance;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HazelcastClusterInfoExports extends Collector {

    private final HazelcastInstance hazelcastInstance;

    public HazelcastClusterInfoExports(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        final List<MetricFamilySamples> metricFamilySamples = new ArrayList<>();

        final Cluster cluster = hazelcastInstance.getCluster();

        final GaugeMetricFamily hazelcastClusterInfo = new GaugeMetricFamily(
                "hazelcast_cluster_state",
                "Hazelcast cluster state",
                Arrays.asList("state", "version", "time")
        );

        hazelcastClusterInfo.addMetric(
                Arrays.asList(
                        cluster.getClusterState().name(),
                        cluster.getClusterVersion().toString(),
                        String.valueOf(cluster.getClusterTime())
                ),
                1L
        );

        metricFamilySamples.add(hazelcastClusterInfo);


        final GaugeMetricFamily hazelcastMembersInfo = new GaugeMetricFamily(
                "hazelcast_members_info",
                "Hazelcast members info",
                Arrays.asList("uuid", "address", "version")
        );

        cluster.getMembers().forEach(m ->
                hazelcastMembersInfo.addMetric(
                        Arrays.asList(m.getUuid(), m.getAddress().toString(), m.getVersion().toString()),
                        1L
                )
        );

        metricFamilySamples.add(hazelcastMembersInfo);

        return metricFamilySamples;
    }
}
