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
package org.datasphere.mdm.core.migration.security.meta;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Column;
import nl.myndocs.database.migrator.definition.Constraint;
import nl.myndocs.database.migrator.definition.ForeignKey;
import nl.myndocs.database.migrator.definition.Index;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * migration for create tables
 *
 * @author maria.chistyakova
 * @since 11.10.2019
 */
public class UN12296InitializationSecuritySchema implements MigrationScript {

    private String CREATED_AT = "created_at";
    private String UPDATED_AT = "updated_at";
    private String UPDATED_BY = "updated_by";
    private String CREATED_BY = "created_by";
    private String ID = "id";
    private String NAME = "name";
    private String DISPLAY_NAME = "display_name";
    private String PARENT_ID = "parent_id";
    private String DB_DEFAULT_CURRENT_TIME = "NOW()";
    private String DESCRIPTION = "description";

    /**
     * Constructor.
     */
    public UN12296InitializationSecuritySchema() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String author() {
        return "maria.chistyakova";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String migrationId() {
        return "UN-12296__InitializationSecuritySchema";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void migrate(Migration migration) {

        migration.table("s_resource")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE))
                .addColumn(DISPLAY_NAME, Column.TYPE.VARCHAR, cb -> cb.size(1024).notNull(Boolean.TRUE))
                .addColumn("r_type", Column.TYPE.VARCHAR, cb -> cb.size(15))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(PARENT_ID, Column.TYPE.INTEGER, Column.Builder::build)
                .addColumn("category",
                        Column.TYPE.VARCHAR,
                        cb -> cb
                                .size(128)
                                .defaultValue("META_MODEL")
                                .notNull(true))
                .addConstraint("pk_s_secured_resource", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addIndex("uq_s_resource_name", Index.TYPE.UNIQUE, cb -> cb.columns(ID))
                .addIndex("ix_s_resource_parent_id", Index.TYPE.DEFAULT, cb -> cb.columns(PARENT_ID))
                .save();

        migration.table("s_resource")
                .addConstraint("fk_s_resource_parent_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns(PARENT_ID)
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_resource")
                                                .foreignKeys(ID)
                                                .build()
                                )
                )
                .save();

        migration.table("s_right")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(DESCRIPTION, Column.TYPE.TEXT, Column.Builder::build)
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_role_right", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .save();

        migration.table("s_role")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE).size(255))
                .addColumn(DISPLAY_NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn("r_type", Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(DESCRIPTION, Column.TYPE.TEXT, Column.Builder::build)
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_roles", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addConstraint("idx_s_role", Constraint.TYPE.UNIQUE, cb -> cb.columns(NAME))
                .save();

        migration.table("s_user")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("login", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE).size(255))
                .addColumn("email", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE).size(255))
                .addColumn("first_name", Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn("last_name", Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn("notes", Column.TYPE.VARCHAR, cb -> cb.size(600))
                .addColumn("active", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.TRUE.toString()))
                .addColumn("admin", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("source", Column.TYPE.VARCHAR, cb -> cb.defaultValue("UNIDATA"))
                .addColumn("external", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("locale", Column.TYPE.VARCHAR, cb -> cb.size(60))
                .addColumn("email_notification", Column.TYPE.BOOLEAN,
                        cb -> cb
                                .defaultValue(Boolean.FALSE.toString())
                                .notNull(true))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_user", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addConstraint("unique_login_source", Constraint.TYPE.UNIQUE, cb -> cb.columns("login"))
                .save();

        migration.table("s_label")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(DISPLAY_NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(DESCRIPTION, Column.TYPE.TEXT, Column.Builder::build)
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_label", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .save();


        migration.table("s_role_property")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.size(2044).notNull(Boolean.TRUE))
                .addColumn(DISPLAY_NAME, Column.TYPE.VARCHAR, cb -> cb.size(2044).notNull(Boolean.TRUE))
                .addColumn("required", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("read_only", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("field_type", Column.TYPE.VARCHAR, cb -> cb.defaultValue(Boolean.FALSE.toString()))
//                .addColumn("s_roles_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
//                .addColumn("s_label_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))

                .addConstraint("pk_s_role_property", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addIndex("ix_s_role_property_unique_name", Index.TYPE.UNIQUE,cb -> cb.columns(NAME))

                .save();


        migration.table("s_user_property")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.size(2044).notNull(Boolean.TRUE))
                .addColumn(DISPLAY_NAME, Column.TYPE.VARCHAR, cb -> cb.size(2044).notNull(Boolean.TRUE))
                .addColumn("required", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("read_only", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("field_type", Column.TYPE.VARCHAR, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))

                .addConstraint("pk_s_user_property", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addIndex("idx_unique_name", Index.TYPE.UNIQUE,cb -> cb.columns(NAME))

                .save();

        migration.table("s_apis")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(DISPLAY_NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(DESCRIPTION, Column.TYPE.TEXT, Column.Builder::build)

                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))

                .addConstraint("pk_s_apis", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))

                .save();


        /**********************************************RELATION TABLE************************************************************/
        migration.table("s_user_s_role")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("s_users_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_roles_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_users_s_roles", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addIndex("idx_s_users_s_roles", Index.TYPE.UNIQUE, cb -> cb.columns("s_users_id", "s_roles_id"))
                .addIndex("idx_s_users_s_roles_s_role_id", Index.TYPE.DEFAULT, cb -> cb.columns("s_roles_id"))
                .addIndex("idx_s_users_s_roles_s_user_id", Index.TYPE.DEFAULT, cb -> cb.columns("s_users_id"))
                .addConstraint("fk_s_users_s_roles_s_user",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_users_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_user")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_users_s_roles_s_roles",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_roles_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_role")
                                                .foreignKeys(ID)
                                )
                )
                .save();

        migration.table("s_password")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("s_user_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("password_text", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("active", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn("activation_code", Column.TYPE.TEXT, cb -> cb.isNull(true))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_password", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addConstraint("fk_s_password",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_user_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_user")
                                                .foreignKeys(ID)
                                )
                )
                .save();

        migration.table("s_token")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("s_user_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("token", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("active", Column.TYPE.BOOLEAN, cb -> cb.defaultValue(Boolean.FALSE.toString()))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_token", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addIndex("idx_s_token", Index.TYPE.DEFAULT, cb -> cb.columns(ID))
                .addConstraint("fk_s_token",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_user_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_user")
                                                .foreignKeys(ID)
                                )
                )
                .save();

        migration.table("s_right_s_resource")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("s_resource_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_right_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_role_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addConstraint("pk_s_right_s_resource", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))

                .addIndex("uq_s_right_s_resource_s_role_id", Index.TYPE.UNIQUE, cb -> cb.columns("s_right_id", "s_resource_id", "s_role_id"))

                .addIndex("idx_s_right_s_resource_s_role_id", Index.TYPE.DEFAULT, cb -> cb.columns("s_role_id"))
                .addIndex("idx_s_right_s_resource_s_right_id", Index.TYPE.DEFAULT, cb -> cb.columns("s_right_id"))
                .addIndex("idx_s_right_s_resource_s_resource_id", Index.TYPE.DEFAULT, cb -> cb.columns("s_resource_id"))

                .addConstraint("fk_s_right_s_resource_s_resource",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_resource_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_resource")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_right_s_resource_s_right",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_right_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_right")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_right_s_resource_s_role",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_role_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_role")
                                                .foreignKeys(ID)
                                )
                )
                .save();

        migration.table("s_label_attribute")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn(NAME, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn("value", Column.TYPE.VARCHAR, cb -> cb.size(1500))
                .addColumn(DESCRIPTION, Column.TYPE.TEXT, Column.Builder::build)
                .addColumn("path", Column.TYPE.VARCHAR, cb -> cb.size(2044))
                .addColumn("s_label_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))

                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))

                .addConstraint("pk_s_label_attribute", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))


                .addConstraint("fk_s_label_attribute",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_label_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_label")
                                                .foreignKeys(ID)
                                )
                )
                .save();

        migration.table("s_label_attribute_value")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("s_label_attribute_id", Column.TYPE.INTEGER, Column.Builder::build)
                .addColumn("value", Column.TYPE.VARCHAR, cb -> cb.size(1500))
                .addColumn("s_label_group", Column.TYPE.INTEGER, Column.Builder::build)

                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))

                .addConstraint("pk_s_label_attribute_value", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))


                .addConstraint("fk_s_label_attribute_value",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_label_attribute_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_label_attribute")
                                                .foreignKeys(ID)
                                )
                )
                .save();



        migration.table("s_role_s_label")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("s_role_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_label_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(255))

                .addConstraint("pk_s_role_s_label", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))

                .addConstraint("fk_s_role_s_label_s_role",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_role_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_role")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_users_s_roles_s_label",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_label_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_label")
                                                .foreignKeys(ID)
                                )
                )
                .save();



        migration.table("s_user_property_value")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))
                .addColumn("user_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("property_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("value", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE).size(2044))
                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(2044))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(2044))

                .addConstraint("pk_s_user_property_value", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addConstraint("idx_s_user_property_value_user_id", Constraint.TYPE.UNIQUE, cb -> cb.columns("user_id"))
                .addConstraint("idx_s_user_property_value_property_id", Constraint.TYPE.UNIQUE, cb -> cb.columns("property_id"))

                .addConstraint("fk_s_user_property_value_user_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("user_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_user")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_user_property_value_property_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("property_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_user_property")
                                                .foreignKeys(ID)
                                )
                )
                .save();



        migration.table("s_role_property_value")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))

                .addColumn("role_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("property_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))

                .addColumn("value", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE).size(2044))

                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(2044))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(2044))

                .addConstraint("pk_s_role_property_value", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))
                .addConstraint("idx_s_role_property_value_user_id", Constraint.TYPE.UNIQUE, cb -> cb.columns("role_id"))
                .addConstraint("idx_s_role_property_value_property_id", Constraint.TYPE.UNIQUE, cb -> cb.columns("property_id"))

                .addConstraint("fk_s_role_property_value_role_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("role_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_role")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_role_property_value_property_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("property_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_role_property")
                                                .foreignKeys(ID)
                                )
                )
                .save();





        migration.table("s_user_s_apis")
                .addColumn(ID, Column.TYPE.INTEGER, cb -> cb.notNull(true).autoIncrement(Boolean.TRUE))

                .addColumn("s_user_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_api_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))

//                .addColumn("value", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE)) // What is it?

                .addColumn(CREATED_AT, Column.TYPE.TIMESTAMPTZ, cb -> cb.defaultValue(DB_DEFAULT_CURRENT_TIME))
                .addColumn(UPDATED_AT, Column.TYPE.TIMESTAMPTZ, Column.Builder::build)
                .addColumn(CREATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(2044))
                .addColumn(UPDATED_BY, Column.TYPE.VARCHAR, cb -> cb.size(2044))

                .addConstraint("pk_s_user_s_apis", Constraint.TYPE.PRIMARY_KEY, cb -> cb.columns(ID))

                .addIndex("idx_s_user_s_apis_s_apis", Index.TYPE.DEFAULT, cb -> cb.columns("s_api_id"))
                .addIndex("idx_s_user_s_apis_s_user", Index.TYPE.DEFAULT, cb -> cb.columns("s_user_id"))

                .addConstraint("idx_s_user_s_apis", Constraint.TYPE.UNIQUE, cb -> cb.columns("s_api_id", "s_user_id"))

                .addConstraint("fk_s_user_s_apis_s_user",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_user_id")
                                .foreignKey(
                                        r -> r
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                                .foreignTable("s_user")
                                                .foreignKeys(ID)
                                )
                )
                .addConstraint("fk_s_user_s_apis",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_api_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_apis")
                                                .foreignKeys(ID)
                                )
                )
                .save();


        migration.table("s_custom_storage")
                .addColumn("key", Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn("user_name", Column.TYPE.VARCHAR, cb -> cb.size(255))
                .addColumn("value", Column.TYPE.VARCHAR, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_api_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))

                .addColumn("update_date", Column.TYPE.TIMESTAMPTZ, cb -> cb.notNull(true))

                .addIndex("idx_s_custom_storage_key", Index.TYPE.DEFAULT, cb -> cb.columns("key"))
                .addIndex("idx_s_custom_storage_user_name", Index.TYPE.DEFAULT, cb -> cb.columns("user_name"))

                .addConstraint("idx_uq_key_user_name", Constraint.TYPE.UNIQUE, cb -> cb.columns("key", "user_name"))

                .save();



        migration.table("s_user_s_label_attribute_value")
                .addColumn("s_object_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_label_attribute_value_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))

                .addIndex("s_user_s_label_attribute_value_s_object_id_s_label_attribute_key",
                        Index.TYPE.UNIQUE, cb -> cb.columns("s_object_id","s_label_attribute_value_id"))

                .addConstraint("fk_s_user_s_label_attribute_value_s_user",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_object_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_user")
                                                .foreignKeys(ID)
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                )
                )

                .addConstraint("fk_s_label_attribute_value_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_label_attribute_value_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_label_attribute_value")
                                                .foreignKeys(ID)
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                )
                )
                .save();




        migration.table("s_role_s_label_attribute_value")
                .addColumn("s_object_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))
                .addColumn("s_label_attribute_value_id", Column.TYPE.INTEGER, cb -> cb.notNull(Boolean.TRUE))

                .addIndex("s_role_s_label_attribute_value_s_object_id_s_label_attribute_key",
                        Index.TYPE.UNIQUE, cb -> cb.columns("s_object_id","s_label_attribute_value_id"))

                .addConstraint("fk_s_role_s_label_attribute_value_s_role_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_object_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_role")
                                                .foreignKeys(ID)
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                )
                )

                .addConstraint("s_label_attribute_value_id",
                        Constraint.TYPE.FOREIGN_KEY,
                        cb -> cb.columns("s_label_attribute_value_id")
                                .foreignKey(
                                        r -> r
                                                .foreignTable("s_label_attribute_value")
                                                .foreignKeys(ID)
                                                .cascadeDelete(ForeignKey.CASCADE.CASCADE)
                                                .cascadeUpdate(ForeignKey.CASCADE.CASCADE)
                                )
                )
                .save();

    }
}
