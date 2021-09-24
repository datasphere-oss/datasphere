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

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.LocalQueueStats;
import com.hazelcast.monitor.LocalTopicStats;
import io.prometheus.client.Gauge;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HazelcastStateExporters {

    private final HazelcastInstance hazelcastInstance;

    /* --- MAPS --- */
    private final Gauge mapsEntriesCountGauge;

    private final Gauge localMapsOwnedEntriesCountGauge;

    private final Gauge localMapsBackupEntriesCountGauge;

    /* --- QUEUES --- */
    private final Gauge queuesEntriesCountGauge;

    private final Gauge localQueuesOwnedEntriesCountGauge;

    private final Gauge localQueuesBackupEntriesCountGauge;

    /* --- TOPICS --- */
    private final Gauge localTopicsPublishOperationsCount;

    private final Gauge localTopicsReceiveOperationCount;

    private final ScheduledExecutorService scheduledExecutorService;

    public HazelcastStateExporters(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;


        /* --- MAPS --- */
        mapsEntriesCountGauge = Gauge.build("hazelcast_map_entries_count", "Hazelcast map entries count")
                .labelNames("name")
                .register();

        localMapsOwnedEntriesCountGauge = Gauge.build(
                "hazelcast_map_local_owned_entries_count",
                "Number of entries owned by this member in hazelcast map"
        )
                .labelNames("name")
                .register();

        localMapsBackupEntriesCountGauge = Gauge.build(
                "hazelcast_map_local_backup_entries_count",
                "Number of backup entries hold by this member in hazelcast map"
        )
                .labelNames("name")
                .register();

        /* --- QUEUES --- */
        queuesEntriesCountGauge = Gauge.build(
                "hazelcast_queue_entries_count",
                "Hazelcast queue entries count"
        )
                .labelNames("name")
                .register();

        localQueuesOwnedEntriesCountGauge = Gauge.build(
                "hazelcast_local_queue_owned_entries_count",
                "Number of owned items in this member in hazelcast queue"
        )
                .labelNames("name")
                .register();

        localQueuesBackupEntriesCountGauge = Gauge.build(
                "hazelcast_local_queue_backup_entries_count",
                "Number of backup items in this member in hazelcast queue"
        )
                .labelNames("name")
                .register();

        /* --- TOPICS --- */
        localTopicsPublishOperationsCount = Gauge.build(
                "hazelcast_topic_local_publish_operations_count",
                "Total number of published messages of this topic on this hazelcast member"
        )
                .labelNames("name")
                .register();

        localTopicsReceiveOperationCount = Gauge.build(
                "hazelcast_topic_local_receive_operations_count",
                "Total number of received messages of this topic on this hazelcast member"
        )
                .labelNames("name")
                .register();


        scheduledExecutorService = Executors.newScheduledThreadPool(
                1,
                new CustomizableThreadFactory("hazelcast-metrics-collector")
        );
        scheduledExecutorService.scheduleAtFixedRate(this::collect, 0L, 1L, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduledExecutorService.shutdown();
    }

    private void collect() {
        final Config config = hazelcastInstance.getConfig();

        final Map<String, MapConfig> mapsConfigs = config.getMapConfigs();
        mapsConfigs.keySet().forEach(this::fillMapMetrics);

        final Map<String, QueueConfig> queuesConfigs = config.getQueueConfigs();
        queuesConfigs.keySet().forEach(this::fillQueueMetrics);

        final Map<String, TopicConfig> topicsConfigs = config.getTopicConfigs();
        topicsConfigs.keySet().forEach(this::fillTopicMetrics);
    }

    private void fillMapMetrics(final String mapName) {
        final IMap<Object, Object> map = hazelcastInstance.getMap(mapName);
        final LocalMapStats localMapStats = map.getLocalMapStats();

        mapsEntriesCountGauge.labels(mapName).set(map.size());

        localMapsOwnedEntriesCountGauge.labels(mapName).set(localMapStats.getOwnedEntryCount());
        localMapsBackupEntriesCountGauge.labels(mapName).set(localMapStats.getBackupEntryCount());
    }

    private void fillQueueMetrics(final String queueName) {
        final IQueue<Object> queue = hazelcastInstance.getQueue(queueName);
        final LocalQueueStats localQueueStats = queue.getLocalQueueStats();

        queuesEntriesCountGauge.labels(queueName).set(queue.size());

        localQueuesOwnedEntriesCountGauge.labels(queueName).set(localQueueStats.getOwnedItemCount());
        localQueuesBackupEntriesCountGauge.labels(queueName).set(localQueueStats.getBackupItemCount());
    }

    private void fillTopicMetrics(final String topicName) {
        final ITopic<Object> topic = hazelcastInstance.getTopic(topicName);
        final LocalTopicStats localTopicStats = topic.getLocalTopicStats();

        localTopicsPublishOperationsCount.labels(topicName).set(localTopicStats.getPublishOperationCount());
        localTopicsReceiveOperationCount.labels(topicName).set(localTopicStats.getReceiveOperationCount());
    }
}
