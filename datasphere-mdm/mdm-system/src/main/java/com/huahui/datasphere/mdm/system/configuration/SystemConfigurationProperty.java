package com.huahui.datasphere.mdm.system.configuration;

import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

import com.huahui.datasphere.mdm.system.module.SystemModule;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.format.DumpTargetFormat;
import com.huahui.datasphere.mdm.system.type.runtime.MeasurementPoint;

/**
 * @author theseusyang
 */
public final class SystemConfigurationProperty {

    private static final Locale CN = new Locale("cn");
    /**
     * No-instance constructor.
     */
    private SystemConfigurationProperty() {
        super();
    }
    /**
     * Enable/disable runtime profiler.
     * Old:
     * name: datasphere.simon.enabled
     * group: datasphere.properties.group.simon
     */
    public static final ConfigurationProperty<Boolean> SYSTEM_SIMON_ENABLED = ConfigurationProperty.bool()
        .key(SystemConfigurationConstants.PROPERTY_SIMON_ENABLED)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .setter(MeasurementPoint::setEnabled)
        .defaultValue(Boolean.FALSE)
        .readOnly(false)
        .required(false)
        .build();
    /**
     * Platform version (really needed?).
     * Old:
     * name: datasphere.platform.version
     * group: datasphere.properties.group.version
     */
    public static final ConfigurationProperty<String> SYSTEM_PLATFORM_VERSION = ConfigurationProperty.string()
        .key(SystemConfigurationConstants.PROPERTY_PLATFORM_VERSION)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(true)
        .build();
    /**
     * Node id.
     * Old:
     * name: datasphere.node.id
     * group: datasphere.properties.group.node.id
     */
    public static final ConfigurationProperty<String> SYSTEM_NODE_ID = ConfigurationProperty.string()
        .key(SystemConfigurationConstants.PROPERTY_NODE_ID)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(true)
        .build();
    /**
     * Data dump target format.
     * Old:
     * name: datasphere.dump.target.format
     * group: not grouped
     */
    public static final ConfigurationProperty<DumpTargetFormat> SYSTEM_DUMP_TARGET_FORMAT = ConfigurationProperty.custom(DumpTargetFormat.class)
        .key(SystemConfigurationConstants.PROPERTY_DUMP_TARGET_FORMAT)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .deserializer(DumpTargetFormat::fromValue)
        .serializer(DumpTargetFormat::name)
        .defaultValue(DumpTargetFormat.PROTOSTUFF)
        .readOnly(true)
        .required(true)
        .build();
    /**
     * Default system locale.
     * Old:
     * name: datasphere.default.locale
     * group: not grouped
     */
    public static final ConfigurationProperty<Locale> SYSTEM_DEFAULT_LOCALE = ConfigurationProperty.custom(Locale.class)
        .key(SystemConfigurationConstants.PROPERTY_DEFAULT_LOCALE)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .deserializer(LocaleUtils::toLocale)
        .defaultValue(CN)
        .readOnly(true)
        .required(true)
        .build();
    /**
     * Developer mode switch.
     * Old:
     * name: new
     * group: not grouped
     */
    public static final ConfigurationProperty<Boolean> SYSTEM_DEVELOPER_MODE = ConfigurationProperty.bool()
        .key(SystemConfigurationConstants.PROPERTY_DEVELOPER_MODE)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .defaultValue(Boolean.FALSE)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Developer mode switch.
     * Old:
     * name: new
     * group: not grouped
     */
    public static final ConfigurationProperty<Long> SYSTEM_EVENT_REPLAY_TIMEOUT = ConfigurationProperty.integer()
        .key(SystemConfigurationConstants.PROPERTY_EVENT_REPLAY_TIMEOUT)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
        .defaultValue(Long.valueOf(3000L))
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(false)
        .required(true)
        .build();
    /**
     * Datasphere cache password.
     * Not part of runtim econfiguration properties.
     * Old datasphere.cache.password
     */
    public static final ConfigurationProperty<String> SYSTEM_CACHE_PASSWORD = ConfigurationProperty.string()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_PASSWORD)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache port.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.port
     */
    public static final ConfigurationProperty<Long> SYSTEM_CACHE_PORT = ConfigurationProperty.integer()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_PORT)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .defaultValue(Long.valueOf(5701L))
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache port autoincrement.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.port.autoincreament
     */
    public static final ConfigurationProperty<Boolean> SYSTEM_CACHE_PORT_AUTOINCREMENT = ConfigurationProperty.bool()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_PORT_AUTOINCREMENT)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .defaultValue(Boolean.FALSE)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache port autoincrement.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.port.autoincreament
     */
    public static final ConfigurationProperty<Boolean> SYSTEM_CACHE_MULTICAST_ENABLED = ConfigurationProperty.bool()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_MULTICAST_ENABLED)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .defaultValue(Boolean.FALSE)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache multicast group.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.multicast.group
     */
    public static final ConfigurationProperty<String> SYSTEM_CACHE_MULTICAST_GROUP = ConfigurationProperty.string()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_MULTICAST_GROUP)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache multicast port.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.multicast.port
     */
    public static final ConfigurationProperty<Long> SYSTEM_CACHE_MULTICAST_PORT = ConfigurationProperty.integer()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_MULTICAST_PORT)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache multicast TTL.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.multicast.ttl
     */
    public static final ConfigurationProperty<Long> SYSTEM_CACHE_MULTICAST_TTL = ConfigurationProperty.integer()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_MULTICAST_TTL)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache multicast timeout.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.multicast.timeout
     */
    public static final ConfigurationProperty<Long> SYSTEM_CACHE_MULTICAST_TIMEOUT = ConfigurationProperty.integer()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_MULTICAST_TIMEOUT)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache TCP/IP enabled.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.tcp-ip.enabled
     */
    public static final ConfigurationProperty<Boolean> SYSTEM_CACHE_TCP_IP_ENABLED = ConfigurationProperty.bool()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_TCP_IP_ENABLED)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .defaultValue(Boolean.FALSE)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Datasphere cache TCP/IP members.
     * Not part of runtime econfiguration properties.
     * Old datasphere.cache.tcp-ip.members
     */
    public static final ConfigurationProperty<String> SYSTEM_CACHE_TCP_IP_MEMBERS = ConfigurationProperty.string()
        .key(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_TCP_IP_MEMBERS)
        .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_CACHE_GROUP)
        .moduleId(SystemModule.MODULE_ID)
        .readOnly(true)
        .required(false)
        .build();
    /**
     * Path to initial pipelines file.
     */
    public static final ConfigurationProperty<String> SYSTEM_INITIAL_PIPELINES = ConfigurationProperty.string()
            .key(SystemConfigurationConstants.PROPERTY_INITIAL_PIPELINES)
            .groupKey(SystemConfigurationConstants.PROPERTY_SYSTEM_GROUP)
            .moduleId(SystemModule.MODULE_ID)
            .required(false)
            .readOnly(true)
            .build();
    /**
     * Enable tracing of camel messaging.
     * Old camel.trace.enable
     */
    public static final ConfigurationProperty<Boolean> SYSTEM_MESSAGING_TRACE_ENABLED = ConfigurationProperty.bool()
            .key(SystemConfigurationConstants.PROPERTY_MESSAGING_TRACE_ENABLED)
            .groupKey(SystemConfigurationConstants.PROPERTY_MESSAGING_GROUP)
            .moduleId(SystemModule.MODULE_ID)
            .defaultValue(Boolean.FALSE)
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Values as array.
     */
    private static final ConfigurationProperty<?>[] VALUES = {
            SYSTEM_SIMON_ENABLED,
            SYSTEM_PLATFORM_VERSION,
            SYSTEM_NODE_ID,
            SYSTEM_DUMP_TARGET_FORMAT,
            SYSTEM_DEFAULT_LOCALE,
            SYSTEM_DEVELOPER_MODE,
            SYSTEM_EVENT_REPLAY_TIMEOUT,
            SYSTEM_CACHE_PASSWORD,
            SYSTEM_CACHE_PORT,
            SYSTEM_CACHE_PORT_AUTOINCREMENT,
            SYSTEM_CACHE_MULTICAST_ENABLED,
            SYSTEM_CACHE_MULTICAST_GROUP,
            SYSTEM_CACHE_MULTICAST_PORT,
            SYSTEM_CACHE_MULTICAST_TTL,
            SYSTEM_CACHE_MULTICAST_TIMEOUT,
            SYSTEM_CACHE_TCP_IP_ENABLED,
            SYSTEM_CACHE_TCP_IP_MEMBERS,
            SYSTEM_INITIAL_PIPELINES,
            SYSTEM_MESSAGING_TRACE_ENABLED
    };
    /**
     * Enum like array accessor.
     * @return array of values.
     */
    public static ConfigurationProperty<?>[] values() {
        return VALUES;
    }
}
