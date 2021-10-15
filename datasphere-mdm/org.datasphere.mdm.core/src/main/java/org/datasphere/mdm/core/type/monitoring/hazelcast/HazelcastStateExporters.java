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

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.LocalQueueStats;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.LocalMapStats;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.LocalTopicStats;

import io.prometheus.client.Gauge;

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
