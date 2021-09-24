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
package com.huahui.datasphere.mdm.system.migration;

import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Migration;
/**
 * Helps to keep migration definitions short.
 */
public class Migrations {
    /**
     * Constructor.
     */
    private Migrations() {
        super();
    }
    /**
     * Construct a migration of plain SQL.
     * @param id migration id
     * @param author migration author
     * @param sql content
     * @return migration script
     */
    public static MigrationScript of(@Nonnull String id, @Nonnull String author, String... sql) {

        Objects.requireNonNull(id, "Migration id must not be null.");
        Objects.requireNonNull(author, "Migration author must not be null.");

        return new MigrationScriptImpl(id, author, sql);
    }
    /**
     * Construct a migration of plain SQL.
     * @param id migration id
     * @param author migration author
     * @param consumer executor
     * @return migration script
     */
    public static MigrationScript of(@Nonnull String id, @Nonnull String author, Consumer<Migration> consumer) {

        Objects.requireNonNull(id, "Migration id must not be null.");
        Objects.requireNonNull(author, "Migration author must not be null.");

        return new MigrationScriptImpl(id, author, consumer);
    }
    /**
     * Simple universal migration shortcut.
     */
    private static class MigrationScriptImpl implements MigrationScript {

        private final String id;

        private final String author;

        private final String[] raw;

        private final Consumer<Migration> consumer;
        /**
         * Constructor.
         * @param id migration id
         * @param author migration author
         * @param raw content
         */
        public MigrationScriptImpl(String id, String author, String... raw) {
            super();
            this.id = id;
            this.author = author;
            this.raw = raw;
            this.consumer = null;
        }
        /**
         * Constructor.
         * @param id migration id
         * @param author migration author
         * @param consumer the consumer to execute
         */
        public MigrationScriptImpl(String id, String author, Consumer<Migration> consumer) {
            super();
            this.id = id;
            this.author = author;
            this.raw = null;
            this.consumer = consumer;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String author() {
            return author;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void migrate(Migration m) {

            if (ArrayUtils.isNotEmpty(this.raw)) {
                m.raw()
                    .sql(this.raw)
                    .save();
            }

            if (Objects.nonNull(this.consumer)) {
                this.consumer.accept(m);
            }
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String migrationId() {
            return id;
        }
    }
}
