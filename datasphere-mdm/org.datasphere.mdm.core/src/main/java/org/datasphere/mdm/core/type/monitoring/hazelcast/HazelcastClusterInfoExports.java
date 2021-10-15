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

package org.datasphere.mdm.core.type.monitoring.hazelcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hazelcast.cluster.Cluster;
import com.hazelcast.core.HazelcastInstance;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

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
                        Arrays.asList(m.getUuid().toString(), m.getAddress().toString(), m.getVersion().toString()),
                        1L
                )
        );

        metricFamilySamples.add(hazelcastMembersInfo);

        return metricFamilySamples;
    }
}
