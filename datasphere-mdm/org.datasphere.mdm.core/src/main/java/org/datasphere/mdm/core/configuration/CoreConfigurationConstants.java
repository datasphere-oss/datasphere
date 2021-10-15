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

import org.datasphere.mdm.core.module.CoreModule;

/**
 * @author Mikhail Mikhailov on Oct 30, 2019
 */
public final class CoreConfigurationConstants {
    /**
     * This module schema name.
     */
    public static final String CORE_SCHEMA_NAME = "org_unidata_mdm_core";
    /**
     * This module log name.
     */
    public static final String CORE_LOG_NAME = "change_log";
    /**
     * Security tokens map name.
     */
    public static final String CORE_SECURITY_TOKENS_MAP_ACTION_NAME = "unidata-security-tokens-map-action";
    /**
     * Security tokens map name.
     */
    public static final String CORE_SECURITY_TOKENS_MAP_NAME = "unidata-security-tokens-map";
    /**
     * Library version JAR field.
     */
    public static final String CORE_LIBRARIES_VERSION_PROPERTY = "Unidata-Library-Version";
    // System jobs:
    /**
     * Core system jobs group name.
     */
    public static final String CORE_JOB_GROUP_NAME = "core-job-group";
    /**
     * Core system job - Clean Unused LOB binaries.
     */
    public static final String CORE_JOB_CLEAN_UNUSED_LOBS = "core-job-clean-unused-lobs";
    /**
     * Custom table prefix.
     */
    public static final String CORE_BATCH_TABLE_PREFIX = "batch_";
    // CONFIGURATION
    // Security
    /**
     * Core data source properties prefix. A non - property, not managed.
     */
    public static final String PROPERTY_CORE_DATASOURCE_PREFIX = CoreModule.MODULE_ID + ".datasource.";
    /**
     * Security group.
     */
    public static final String PROPERTY_SECURITY_GROUP = CoreModule.MODULE_ID + ".group.security";
    /**
     * Token TTL.
     */
    public static final String PROPERTY_SECURITY_TOKEN_TTL = CoreModule.MODULE_ID + ".security.token.ttl";
    /**
     * Token cleanup CRON spec.
     */
    public static final String PROPERTY_SECURITY_TOKEN_CLEANUP = CoreModule.MODULE_ID + ".security.token.cleanup";
    /**
     * Allow password change.
     * Old unidata.password.policy.user.allow.change.expired.password
     */
    public static final String PROPERTY_POLICY_ALLOW_PASSWORD_CHANGE = CoreModule.MODULE_ID + ".password.policy.allow.password.change";
    /**
     * Expiration notification period.
     * Old unidata.password.policy.expiration.email.notification.period.days
     */
    public static final String PROPERTY_POLICY_PASSWORD_EXPIRATION_NOTIFICATION_PERIOD = CoreModule.MODULE_ID + ".password.policy.expiration.notification.period";
    /**
     * Admin expiration days.
     * Old unidata.password.policy.admin.expiration.days
     */
    public static final String PROPERTY_POLICY_PASSWORD_ADMIN_EXPIRATION_DAYS = CoreModule.MODULE_ID + ".password.policy.admin.expiration.days";
    /**
     * User expiration days.
     * Old unidata.password.policy.user.expiration.days
     */
    public static final String PROPERTY_POLICY_PASSWORD_USER_EXPIRATION_DAYS = CoreModule.MODULE_ID + ".password.policy.user.expiration.days";
    /**
     * Password min length.
     * Old unidata.password.policy.min.length
     */
    public static final String PROPERTY_POLICY_PASSWORD_MIN_LENGTH = CoreModule.MODULE_ID + ".password.policy.min.length";
    /**
     * Password regexp.
     * Old unidata.password.policy.regexp
     */
    public static final String PROPERTY_POLICY_REGEXP = CoreModule.MODULE_ID + ".password.policy.regexp";
    /**
     * Password regexp example.
     * Old unidata.password.policy.regexp.example
     */
    public static final String PROPERTY_POLICY_REGEXP_EXAMPLE = CoreModule.MODULE_ID + ".password.policy.regexp.example";
    /**
     * Password repetitions check count.
     * Old unidata.password.policy.check.last.repeat
     */
    public static final String PROPERTY_POLICY_CHECK_REPETITIONS_COUNT = CoreModule.MODULE_ID + ".password.policy.check.repetitions.count";
    /**
     * PW policy group.
     */
    public static final String PROPERTY_POLICY_GROUP = CoreModule.MODULE_ID + ".group.password.policy";
    // Jobs
    /**
     * Job group.
     */
    public static final String PROPERTY_JOB_GROUP = CoreModule.MODULE_ID + ".group.job";
    /**
     * Job initial pool size.
     */
    public static final String PROPERTY_JOB_MIN_THREAD_POOL_SIZE = CoreModule.MODULE_ID + ".job.pool.min.size";
    /**
     * Job max pool size.
     */
    public static final String PROPERTY_JOB_MAX_THREAD_POOL_SIZE = CoreModule.MODULE_ID + ".job.pool.max.size";
    /**
     * Job queue capacity.
     */
    public static final String PROPERTY_JOB_QUEUE_SIZE = CoreModule.MODULE_ID + ".job.queue.size";
    /**
     * Audit group.
     */
    public static final String PROPERTY_AUDIT_GROUP = CoreModule.MODULE_ID + ".group.audit";
    /**
     * Job queue capacity.
     * Old unidata.audit.enabled
     */
    public static final String PROPERTY_AUDIT_ENABLED = CoreModule.MODULE_ID + ".audit.enabled";
    /**
     * Audit read events.
     * Old unidata.audit.read.events
     */
    public static final String PROPERTY_AUDIT_READ_EVENTS = CoreModule.MODULE_ID + ".audit.read.events";
    /**
     * Audit stack trace depth.
     * Old unidata.audit.stacktrace.depth
     */
    public static final String PROPERTY_AUDIT_STACK_TRACE_DEPTH = CoreModule.MODULE_ID + ".audit.stacktrace.depth";
    /**
     * Writer pool size.
     * Old unidata.audit.writer.pool.size
     */
    public static final String PROPERTY_AUDIT_WRITER_POOL_SIZE = CoreModule.MODULE_ID + ".audit.writer.pool.size";
    /**
     * Enabled storages.
     * Old unidata.audit.enabled.storages
     */
    public static final String PROPERTY_AUDIT_ENABLED_STORAGES = CoreModule.MODULE_ID + ".audit.enabled.storages";
    /**
     * Async rare task executor group.
     */
    public static final String PROPERTY_ASYNC_TASK_EXECUTOR_GROUP = CoreModule.MODULE_ID + ".async.task.executor.group";
    /**
     * Async rare task executor pool size..
     * Old unidata.async.rare.tasks.executor.threads.pool.size
     */
    public static final String PROPERTY_ASYNC_TASK_EXECUTOR_POOL_SIZE = CoreModule.MODULE_ID + ".async.task.executor.pool.size";
    /**
     * Indexing properties group.
     */
    public static final String PROPERTY_INDEX_GROUP = CoreModule.MODULE_ID + ".group.index";
    /**
     * Number of default shards for system index property name.
     * Fromer unidata.search.system.shards.number
     */
    public static final String PROPERTY_SYSTEM_SHARDS_NUMBER = CoreModule.MODULE_ID + ".indexing.shards.number";
    /**
     * Number of default replicas for system index property name.
     * Former unidata.search.system.replicas.number
     */
    public static final String PROPERTY_SYSTEM_REPLICAS_NUMBER = CoreModule.MODULE_ID + ".indexing.replicas.number";
    // SYSTEM JOBS
    /**
     * System jobs group.
     */
    public static final String PROPERTY_SYSTEM_JOBS_GROUP = CoreModule.MODULE_ID + ".group.system.jobs";
    /**
     * Clean unused binaries job 'disabled' state.
     * Former clean.unused.binary.job.disable
     */
    public static final String PROPERTY_JOB_CLEAN_BINARIES_DISABLED = CoreModule.MODULE_ID + ".job.clean.binaries.disabled";
    /**
     * Clean binaries lifetime inteval.
     * Former clean.unused.binary.job.lifetime.minutes
     */
    public static final String PROPERTY_JOB_CLEAN_BINARIES_LIFETIME = CoreModule.MODULE_ID + ".job.clean.binaries.lifetime";
    /**
     * Clean binaries cron expression. Read only.
     * Former clean.unused.binary.job.execution.time
     */
    public static final String PROPERTY_JOB_CLEAN_BINARIES_CRONEX = CoreModule.MODULE_ID + ".job.clean.binaries.cronex";
    /**
     * Meta model properties group.
     */
    public static final String PROPERTY_META_MODEL_GROUP = CoreModule.MODULE_ID + ".group.meta.model";
    // End of CONFIGURATION
    /**
     * Constructor.
     */
    private CoreConfigurationConstants() {
        super();
    }
}
