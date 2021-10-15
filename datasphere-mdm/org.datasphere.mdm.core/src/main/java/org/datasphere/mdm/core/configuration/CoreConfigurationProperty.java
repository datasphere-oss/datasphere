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

package org.datasphere.mdm.core.configuration;


import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.module.CoreModule;
import org.datasphere.mdm.core.service.AuditStorageService.AuditStorageType;
import org.datasphere.mdm.system.type.configuration.ConfigurationProperty;

public final class CoreConfigurationProperty {
    /**
     * No instance ctor.
     */
    private CoreConfigurationProperty() {
        super();
    }
    /**
     * Token TTL in seconds.
     * Old values:
     * value: 'unidata.security.token.ttl'
     * group: 'unidata.properties.group.security'
     */
    public static final ConfigurationProperty<Long> CORE_SECURITY_TOKEN_TTL = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_SECURITY_TOKEN_TTL)
            .groupKey(CoreConfigurationConstants.PROPERTY_SECURITY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(1800L))
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Token cleanup cron spec.
     * Old values:
     * value: 'unidata.security.token.cleanup'
     * group: 'unidata.properties.group.security'
     */
    public static final ConfigurationProperty<String> CORE_SECURITY_TOKEN_CLEANUP = ConfigurationProperty.string()
            .key(CoreConfigurationConstants.PROPERTY_SECURITY_TOKEN_CLEANUP)
            .groupKey(CoreConfigurationConstants.PROPERTY_SECURITY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue("0 0 * * * ?")
            .required(true)
            .readOnly(true)
            .build();
    /**
     * Job min thread pool size.
     */
    public static final ConfigurationProperty<Long> CORE_JOB_MIN_THREAD_POOL_SIZE = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_JOB_MIN_THREAD_POOL_SIZE)
            .groupKey(CoreConfigurationConstants.PROPERTY_JOB_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(4L))
            .required(true)
            .readOnly(true)
            .build();
    /**
     * Job max thread pool size.
     */
    public static final ConfigurationProperty<Long> CORE_JOB_MAX_THREAD_POOL_SIZE = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_JOB_MAX_THREAD_POOL_SIZE)
            .groupKey(CoreConfigurationConstants.PROPERTY_JOB_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(24L))
            .required(true)
            .readOnly(true)
            .build();
    /**
     * Job main queue size.
     */
    public static final ConfigurationProperty<Long> CORE_JOB_QUEUE_SIZE = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_JOB_QUEUE_SIZE)
            .groupKey(CoreConfigurationConstants.PROPERTY_JOB_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(100L))
            .required(true)
            .readOnly(true)
            .build();
    /**
     * Audit enabled.
     */
    public static final ConfigurationProperty<Boolean> CORE_AUDIT_ENABLED = ConfigurationProperty.bool()
            .key(CoreConfigurationConstants.PROPERTY_AUDIT_ENABLED)
            .groupKey(CoreConfigurationConstants.PROPERTY_AUDIT_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Boolean.TRUE)
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Audit read events.
     */
    public static final ConfigurationProperty<Boolean> CORE_AUDIT_READ_EVENTS = ConfigurationProperty.bool()
            .key(CoreConfigurationConstants.PROPERTY_AUDIT_READ_EVENTS)
            .groupKey(CoreConfigurationConstants.PROPERTY_AUDIT_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Boolean.TRUE)
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Audit stack trace depth.
     */
    public static final ConfigurationProperty<Long> CORE_AUDIT_STACK_TRACE_DEPTH = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_AUDIT_STACK_TRACE_DEPTH)
            .groupKey(CoreConfigurationConstants.PROPERTY_AUDIT_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(32L))
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Audit writer pool size.
     */
    public static final ConfigurationProperty<Long> CORE_AUDIT_WRITER_POOL_SIZE = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_AUDIT_WRITER_POOL_SIZE)
            .groupKey(CoreConfigurationConstants.PROPERTY_AUDIT_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(4L))
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Audit storage types to write to.
     */
    public static final ConfigurationProperty<AuditStorageType[]> CORE_AUDIT_ENABLED_STORAGES = ConfigurationProperty.custom(AuditStorageType[].class)
            .key(CoreConfigurationConstants.PROPERTY_AUDIT_ENABLED_STORAGES)
            .groupKey(CoreConfigurationConstants.PROPERTY_AUDIT_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .serializer(v -> StringUtils.join(AuditStorageType.toValues(v), ','))
            .deserializer(v -> AuditStorageType.fromValues(StringUtils.split(v, ',')))
            .defaultValue(AuditStorageType.fromValues(StringUtils.split("db, es", ',')))
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Async rare task executor.
     */
    public static final ConfigurationProperty<Long> CORE_ASYNC_TASK_EXECUTOR_POOL_SIZE = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_ASYNC_TASK_EXECUTOR_POOL_SIZE)
            .groupKey(CoreConfigurationConstants.PROPERTY_ASYNC_TASK_EXECUTOR_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(4L))
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Allow password change.
     * Old unidata.password.policy.user.allow.change.expired.password
     */
    public static final ConfigurationProperty<Boolean> CORE_POLICY_ALLOW_PASSWORD_CHANGE = ConfigurationProperty.bool()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_ALLOW_PASSWORD_CHANGE)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Boolean.TRUE)
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Expiration notification period.
     * Old unidata.password.policy.expiration.email.notification.period.days
     */
    public static final ConfigurationProperty<Long> CORE_POLICY_EXPIRATION_NOTIFICATION_PERIOD = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_PASSWORD_EXPIRATION_NOTIFICATION_PERIOD)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(10L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Admin expiration days.
     * Old unidata.password.policy.admin.expiration.days
     */
    public static final ConfigurationProperty<Long> CORE_POLICY_PASSWORD_ADMIN_EXPIRATION_DAYS = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_PASSWORD_ADMIN_EXPIRATION_DAYS)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(91L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * User expiration days.
     * Old unidata.password.policy.user.expiration.days
     */
    public static final ConfigurationProperty<Long> CORE_POLICY_PASSWORD_USER_EXPIRATION_DAYS = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_PASSWORD_USER_EXPIRATION_DAYS)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(181L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Password min length.
     * Old unidata.password.policy.min.length
     */
    public static final ConfigurationProperty<Long> CORE_POLICY_PASSWORD_MIN_LENGTH = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_PASSWORD_MIN_LENGTH)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(0L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Password regexp.
     * Old unidata.password.policy.regexp
     */
    public static final ConfigurationProperty<String> CORE_POLICY_REGEXP = ConfigurationProperty.string()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_REGEXP)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Password regexp example.
     * Old unidata.password.policy.regexp.example
     */
    public static final ConfigurationProperty<String> CORE_POLICY_REGEXP_EXAMPLE = ConfigurationProperty.string()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_REGEXP_EXAMPLE)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Password repetitions check count.
     * Old unidata.password.policy.check.last.repeat
     */
    public static final ConfigurationProperty<Long> CORE_POLICY_CHECK_REPETITIONS_COUNT = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_POLICY_CHECK_REPETITIONS_COUNT)
            .groupKey(CoreConfigurationConstants.PROPERTY_POLICY_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(0L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Number of default shards for system index property name.
     * Fromer unidata.search.system.shards.number
     */
    public static final ConfigurationProperty<Long> CORE_SYSTEM_SHARDS_NUMBER = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_SYSTEM_SHARDS_NUMBER)
            .groupKey(CoreConfigurationConstants.PROPERTY_INDEX_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(1L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Number of default replicas for system index property name.
     * Former unidata.search.system.replicas.number
     */
    public static final ConfigurationProperty<Long> CORE_SYSTEM_REPLICAS_NUMBER = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_SYSTEM_REPLICAS_NUMBER)
            .groupKey(CoreConfigurationConstants.PROPERTY_INDEX_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(0L))
            .required(false)
            .readOnly(false)
            .build();
    /**
     * Audit read events.
     */
    public static final ConfigurationProperty<Boolean> JOB_CLEAN_BINARIES_DISABLED = ConfigurationProperty.bool()
            .key(CoreConfigurationConstants.PROPERTY_JOB_CLEAN_BINARIES_DISABLED)
            .groupKey(CoreConfigurationConstants.PROPERTY_SYSTEM_JOBS_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Boolean.FALSE)
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Audit stack trace depth.
     */
    public static final ConfigurationProperty<Long> JOB_CLEAN_BINARIES_LIFETIME = ConfigurationProperty.integer()
            .key(CoreConfigurationConstants.PROPERTY_JOB_CLEAN_BINARIES_LIFETIME)
            .groupKey(CoreConfigurationConstants.PROPERTY_SYSTEM_JOBS_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue(Long.valueOf(10080L))
            .required(true)
            .readOnly(false)
            .build();
    /**
     * Cleanup binaries CRON expression.
     * Old clean.unused.binary.job.execution.time
     */
    public static final ConfigurationProperty<String> JOB_CLEAN_BINARIES_CRONEX = ConfigurationProperty.string()
            .key(CoreConfigurationConstants.PROPERTY_JOB_CLEAN_BINARIES_CRONEX)
            .groupKey(CoreConfigurationConstants.PROPERTY_SYSTEM_JOBS_GROUP)
            .moduleId(CoreModule.MODULE_ID)
            .defaultValue("0 0 0/1 * * ?")
            .required(true)
            .readOnly(true)
            .build();
    /**
     * Values as array.
     */
    private static final ConfigurationProperty<?>[] VALUES = {
            CORE_SECURITY_TOKEN_TTL,
            CORE_SECURITY_TOKEN_CLEANUP,
            CORE_JOB_MAX_THREAD_POOL_SIZE,
            CORE_JOB_MIN_THREAD_POOL_SIZE,
            CORE_JOB_QUEUE_SIZE,
            CORE_AUDIT_ENABLED,
            CORE_AUDIT_READ_EVENTS,
            CORE_AUDIT_STACK_TRACE_DEPTH,
            CORE_AUDIT_WRITER_POOL_SIZE,
            CORE_AUDIT_ENABLED_STORAGES,
            CORE_ASYNC_TASK_EXECUTOR_POOL_SIZE,
            CORE_POLICY_ALLOW_PASSWORD_CHANGE,
            CORE_POLICY_EXPIRATION_NOTIFICATION_PERIOD,
            CORE_POLICY_PASSWORD_ADMIN_EXPIRATION_DAYS,
            CORE_POLICY_PASSWORD_MIN_LENGTH,
            CORE_POLICY_PASSWORD_USER_EXPIRATION_DAYS,
            CORE_POLICY_REGEXP,
            CORE_POLICY_REGEXP_EXAMPLE,
            CORE_POLICY_CHECK_REPETITIONS_COUNT,
            CORE_SYSTEM_SHARDS_NUMBER,
            CORE_SYSTEM_REPLICAS_NUMBER,
            JOB_CLEAN_BINARIES_DISABLED,
            JOB_CLEAN_BINARIES_LIFETIME,
            JOB_CLEAN_BINARIES_CRONEX
    };
    /**
     * Enum like array accessor.
     * @return array of values.
     */
    public static ConfigurationProperty<?>[] values() {
        return VALUES;
    }
}
