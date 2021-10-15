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

package org.datasphere.mdm.core.migration;

import org.datasphere.mdm.core.migration.audit.UN11979InitAuditTables;
import org.datasphere.mdm.core.migration.bus.InitBusConfigurationTables;
import org.datasphere.mdm.core.migration.event.meta.UN12296InitializationEventCoreSchema;
import org.datasphere.mdm.core.migration.job.meta.InitializationQuartzJobSchema;
import org.datasphere.mdm.core.migration.job.meta.UN12296InitializationJobCoreSchema;
import org.datasphere.mdm.core.migration.security.data.UN12296InsertSecurityDefaultData;
import org.datasphere.mdm.core.migration.security.meta.UN12296InitializationSecuritySchema;
import org.datasphere.mdm.core.migration.security.meta.UN13491DropColumn;
import org.datasphere.mdm.system.migration.Migrations;
import org.datasphere.mdm.system.util.ResourceUtils;

import nl.myndocs.database.migrator.MigrationScript;

/**
 * storage migrations to install security meta + admin login + resource
 *
 *
 * @author maria.chistyakova
 */
public final class CoreSchemaMigrations {

    private static final MigrationScript[] MIGRATIONS = {
            new UN12296InitializationSecuritySchema(),
            new UN12296InsertSecurityDefaultData(),
            new UN12296InitializationJobCoreSchema(),
            new UN12296InitializationEventCoreSchema(),
            new UN11979InitAuditTables(),
            new InitializationQuartzJobSchema(),
            new InitBusConfigurationTables(),
            new UN13491DropColumn(),
            Migrations.of("UN-15627__MoveStorageToCore", "mikhail.mikhailov", ResourceUtils.asStrings("classpath:/migration/UN-15627-move-storage-to-core.sql")),
            Migrations.of("UN-15627__UserLibraries", "mikhail.mikhailov", ResourceUtils.asStrings("classpath:/migration/UN-15627-user-libraries.sql")),
            Migrations.of("UN-15627__UserLibraries_EditableField", "mikhail.mikhailov", "alter table libraries add column if not exists editable boolean default true"),
            Migrations.of("UN-15755__BinaryCharacterData", "mikhail.mikhailov", ResourceUtils.asStrings("classpath:/migration/UN-15755-binary-character-data.sql")),
            Migrations.of("UN-16796__CannotSaveUserProperty", "mikhail.mikhailov",
                    // user property values
                    "alter table s_user_property_value drop constraint if exists idx_s_user_property_value_property_id",
                    "alter table s_user_property_value drop constraint if exists idx_s_user_property_value_user_id",
                    "alter table s_user_property_value add constraint uq_s_user_property_value_user_id_property_id unique (user_id, property_id)",
                    "create index ix_s_user_property_value_property_id on s_user_property_value using btree (property_id)",
                    "create index ix_s_user_property_value_user_id on s_user_property_value using btree (user_id)",
                    // role property values
                    "alter table s_role_property_value drop constraint if exists idx_s_role_property_value_property_id",
                    "alter table s_role_property_value drop constraint if exists idx_s_role_property_value_user_id",
                    "alter table s_role_property_value add constraint uq_s_role_property_value_role_id_property_id unique (role_id, property_id)",
                    "create index ix_s_role_property_value_property_id on s_role_property_value using btree (property_id)",
                    "create index ix_s_role_property_value_role_id on s_role_property_value using btree (role_id)"),
            Migrations.of("UN-14474__JobAPIRefactoring", "mikhail.mikhailov",
                    // Table 'job'
                    "alter table job add column parameters text",
                    "alter table job rename column job_name_ref to job_name",
                    "alter table job rename column cron_expr to cron_expression",
                    "alter table job rename column descr to description",
                    // Table 'job_parameter'
                    "drop table if exists job_parameter",
                    // Triggers
                    "drop table if exists job_trigger",
                    // Table 'job_batch_job_instance'
                    "alter table job_batch_job_instance rename column job_id to job_definition_id",
                    "alter table job_batch_job_instance drop column update_date",
                    "alter table job_batch_job_instance drop column updated_by",
                    "alter table job_batch_job_instance drop constraint fk_job_id",
                    "alter table job_batch_job_instance add constraint pk_job_batch_job_instance_job_id_job_instance_id primary key (job_definition_id, job_instance_id)",
                    "alter table job_batch_job_instance add constraint fk_job_batch_job_instance_job_id foreign key (job_definition_id) "
                        + "references job(id) match full on delete cascade on update cascade",
                    "create index ix_job_batch_job_instance_create_date on job_batch_job_instance using btree (job_definition_id, create_date)",
                    // Table 'job_sys_job_instance'
                    "create table job_sys_job_instance ("
                        + "job_name text not null, "
                        + "job_instance_id int8 not null, "
                        + "create_date timestamptz not null default now(),"
                        + "created_by varchar(256) not null, "
                        + "constraint pk_job_sys_job_instance_job_name_job_instance_id primary key (job_name, job_instance_id))",
                    "create index ix_job_sys_job_instance_create_date on job_sys_job_instance using btree (job_name, create_date)",
                    // Table 'batch_job_execution'
                    "alter table batch_job_execution drop constraint job_inst_exec_fk",
                    "alter table batch_job_execution add constraint job_inst_exec_fk foreign key (job_instance_id) "
                        + "references batch_job_instance(job_instance_id) match full on delete cascade on update cascade",
                    "create index batch_job_execution_job_instance_id on batch_job_execution using btree (job_instance_id)",
                    "create index batch_job_execution_create_time on batch_job_execution using btree (create_time)",
                    // Table 'batch_step_execution'
                    "alter table batch_step_execution drop constraint job_exec_step_fk",
                    "alter table batch_step_execution add constraint job_exec_step_fk foreign key (job_execution_id) "
                        + "references batch_job_execution(job_execution_id) match full on delete cascade on update cascade",
                    "create index batch_step_execution_job_execution_id on batch_step_execution using btree (job_execution_id)",
                    // Table 'batch_job_execution_context'
                    "alter table batch_job_execution_context drop constraint job_exec_ctx_fk",
                    "alter table batch_job_execution_context add constraint job_exec_ctx_fk foreign key (job_execution_id) "
                        + "references batch_job_execution(job_execution_id) match full on delete cascade on update cascade",
                    // Table 'batch_step_execution'
                    "alter table batch_step_execution_context drop constraint step_exec_ctx_fk",
                    "alter table batch_step_execution_context add constraint step_exec_ctx_fk foreign key (step_execution_id) "
                        + "references batch_step_execution(step_execution_id) match full on delete cascade on update cascade",
                    // Table 'batch_job_execution_params'
                    "alter table batch_job_execution_params drop constraint job_exec_params_fk",
                    "alter table batch_job_execution_params add constraint job_exec_params_fk foreign key (job_execution_id) "
                        + "references batch_job_execution(job_execution_id) match full on delete cascade on update cascade",
                    "create index batch_job_execution_params_job_execution_id on batch_job_execution_params using btree (job_execution_id)")
    };
    /**
     * Constructor.
     */
    private CoreSchemaMigrations() {
        super();
    }

    /**
     * Makes SONAR happy.
     *
     * @return migrations
     */
    public static MigrationScript[] migrations() {
        return MIGRATIONS;
    }

}
