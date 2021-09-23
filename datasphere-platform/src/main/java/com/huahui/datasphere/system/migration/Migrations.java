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
package com.huahui.datasphere.system.migration;

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
