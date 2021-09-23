

package com.huahui.datasphere.system.configuration;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.system.module.SystemModule;

/**
 * @author theseusyang
 * This module contsants.
 */
public final class SystemConfigurationConstants {
    /**
     * The non-subject mark.
     */
    public static final String NON_SUBJECT = StringUtils.EMPTY;
    /**
     * Unidata system schema name.
     */
    public static final String SYSTEM_SCHEMA_NAME = "datasphere_data_system";
    /**
     * Change log table name.
     */
    public static final String SYSTEM_MIGRATION_LOG_NAME = "system_change_log";
    /**
     * Change log table name.
     */
    public static final String SYSTEM_ACTIONS_MAP_NAME = "system-actions-map";
    /**
     * System executor service name.
     */
    public static final String SYSTEM_EXECUTOR_SERVICE_NAME = "system-executor-service";
    /**
     * DataSphere default camel context name.
     */
    public static final String SYSTEM_CAMEL_CONTEXT = "datasphere-camel-context";
    /**
     * Base configuration path.
     */
    public static final String PROPERTY_CONFIGURATION_PATH = "datasphere.conf";
    // PROPERTIES start
    /**
     * BTM configuration properties prefix.
     * Not a part of properties framework.
     */
    public static final String PROPERTY_BITRONIX_PREFIX = SystemModule.MODULE_ID + ".bitronix.tm.";
    /**
     * System group.
     */
    public static final String PROPERTY_SYSTEM_GROUP = SystemModule.MODULE_ID + ".system.group";
    /**
     * Runtime profiler.
     */
    public static final String PROPERTY_SIMON_ENABLED = SystemModule.MODULE_ID + ".simon.enabled";
    /**
     * Current main platform version.
     */
    public static final String PROPERTY_PLATFORM_VERSION = SystemModule.MODULE_ID + ".platform.version";
    /**
     * Unidata node id property.
     */
    public static final String PROPERTY_NODE_ID = SystemModule.MODULE_ID + ".node.id";
    /**
     * Unidata dump target format property.
     */
    public static final String PROPERTY_DUMP_TARGET_FORMAT = SystemModule.MODULE_ID + ".dump.target.format";
    /**
     * Unidata system schema URL property name.
     */
    public static final String PROPERTY_DEFAULT_LOCALE = SystemModule.MODULE_ID + ".default.locale";
    /**
     * Unidata system dev mode indicator.
     */
    public static final String PROPERTY_DEVELOPER_MODE = SystemModule.MODULE_ID + ".developer.mode";
    /**
     * Default replay timeout (in millis).
     */
    public static final String PROPERTY_EVENT_REPLAY_TIMEOUT = SystemModule.MODULE_ID + ".event.replay.timeout";
    /**
     * Unidata system schema URL property name.
     * Not part of configuration properties.
     */
    public static final String PROPERTY_SYSTEM_DATASOURCE_URL = SystemModule.MODULE_ID + ".datasource.url";
    /**
     * Unidata system schema user property name.
     * Not part of configuration properties.
     */
    public static final String PROPERTY_SYSTEM_DATASOURCE_USER = SystemModule.MODULE_ID + ".datasource.username";
    /**
     * Unidata system schema password property name.
     * Not part of configuration properties.
     */
    public static final String PROPERTY_SYSTEM_DATASOURCE_PASSWORD = SystemModule.MODULE_ID + ".datasource.password";
    // Distributed cache (Hazelcast)
    /**
     * Unidata cache group.
     * Not part of runtim econfiguration properties.
     * Old unidata.cache.group
     */
    public static final String PROPERTY_SYSTEM_CACHE_GROUP = SystemModule.MODULE_ID + ".cache.group";
    /**
     * Unidata cache password.
     * Not part of runtim econfiguration properties.
     * Old unidata.cache.password
     */
    public static final String PROPERTY_SYSTEM_CACHE_PASSWORD = SystemModule.MODULE_ID + ".cache.password";
    /**
     * Unidata cache port.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.port
     */
    public static final String PROPERTY_SYSTEM_CACHE_PORT = SystemModule.MODULE_ID + ".cache.port";
    /**
     * Unidata cache port autoincrement.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.port.autoincreament
     */
    public static final String PROPERTY_SYSTEM_CACHE_PORT_AUTOINCREMENT = SystemModule.MODULE_ID + ".cache.port.autoincreament";
    /**
     * Unidata cache multicast enabled.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.multicast.enabled
     */
    public static final String PROPERTY_SYSTEM_CACHE_MULTICAST_ENABLED = SystemModule.MODULE_ID + ".cache.multicast.enabled";
    /**
     * Unidata cache multicast group.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.multicast.group
     */
    public static final String PROPERTY_SYSTEM_CACHE_MULTICAST_GROUP = SystemModule.MODULE_ID + ".cache.multicast.group";
    /**
     * Unidata cache multicast port.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.multicast.port
     */
    public static final String PROPERTY_SYSTEM_CACHE_MULTICAST_PORT = SystemModule.MODULE_ID + ".cache.multicast.port";
    /**
     * Unidata cache multicast TTL.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.multicast.ttl
     */
    public static final String PROPERTY_SYSTEM_CACHE_MULTICAST_TTL = SystemModule.MODULE_ID + ".cache.multicast.ttl";
    /**
     * Unidata cache multicast timeout.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.multicast.timeout
     */
    public static final String PROPERTY_SYSTEM_CACHE_MULTICAST_TIMEOUT = SystemModule.MODULE_ID + ".cache.multicast.timeout";
    /**
     * Unidata cache TCP/IP enabled.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.tcp-ip.enabled
     */
    public static final String PROPERTY_SYSTEM_CACHE_TCP_IP_ENABLED = SystemModule.MODULE_ID + ".cache.tcp-ip.enabled";
    /**
     * Unidata cache TCP/IP members.
     * Not part of runtime econfiguration properties.
     * Old unidata.cache.tcp-ip.members
     */
    public static final String PROPERTY_SYSTEM_CACHE_TCP_IP_MEMBERS = SystemModule.MODULE_ID + ".cache.tcp-ip.members";
    /**
     * Enable tracing of camel messaging.
     * Old camel.trace.enable
     */
    public static final String PROPERTY_MESSAGING_TRACE_ENABLED = SystemModule.MODULE_ID + ".messaging.trace.enabled";
    /**
     * Camel bus group.
     */
    public static final String PROPERTY_MESSAGING_GROUP = SystemModule.MODULE_ID + ".messaging.group";
    /**
     * Initial pipelines.
     */
    public static final String PROPERTY_INITIAL_PIPELINES = SystemModule.MODULE_ID + ".initial.pipelines";
    /**
     * Constructor.
     */
    private SystemConfigurationConstants() {
        super();
    }

}
