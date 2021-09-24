package com.huahui.datasphere.mdm.system.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.processor.MigrationContext;

/**
 * @author theseusyang
 */
public class DatabaseMigrationContext extends MigrationContext {
    /**
     * Data source to use.
     */
    protected final DataSource dataSource;
    /**
     * Schema name to switch.
     */
    protected final String schemaName;
    /**
     * Log table name.
     */
    protected final String logName;
    /**
     * Migration scripts to run.
     */
    protected final List<MigrationScript> migrations;
    /**
     * Constructor.
     * @param b
     */
    protected DatabaseMigrationContext(AbstractDatabaseMigrationContextBuilder<?> b) {
        super();
        this.dataSource = b.dataSource;
        this.migrations = b.migrations;
        this.schemaName = b.schemaName;
        this.logName = b.logName;
    }
    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    /**
     * @return the schemaName
     */
    public String getSchemaName() {
        return schemaName;
    }
    /**
     * @return the logName
     */
    public String getLogName() {
        return logName;
    }
    /**
     * @return the migrations
     */
    public List<MigrationScript> getMigrations() {
        return Objects.isNull(migrations) ? Collections.emptyList() : migrations;
    }
    /**
     * The builder.
     * @return builder
     */
    public static DatabaseMigrationContextBuilder builder() {
        return new DatabaseMigrationContextBuilder();
    }
    /**
     * The builder class, we actually use.
     */
    public static class DatabaseMigrationContextBuilder extends AbstractDatabaseMigrationContextBuilder<DatabaseMigrationContextBuilder> {
        /**
         * {@inheritDoc}
         */
        @Override
        public DatabaseMigrationContext build() {
            Objects.requireNonNull(this.dataSource, "Datasource must not be null!");
            Objects.requireNonNull(this.logName, "Log name must not be null!");
            return new DatabaseMigrationContext(this);
        }

    }
    /**
     * The usual builder class.
     */
    protected abstract static class AbstractDatabaseMigrationContextBuilder<X extends AbstractDatabaseMigrationContextBuilder<X>> {
        /**
         * Data source to use.
         */
        protected DataSource dataSource;
        /**
         * Schema name to switch.
         */
        protected String schemaName;
        /**
         * Log table name.
         */
        protected String logName;
        /**
         * Migration scripts to run.
         */
        protected List<MigrationScript> migrations;
        /**
         * Constructor.
         */
        protected AbstractDatabaseMigrationContextBuilder() {
            super();
        }
        /**
         * Data source to use.
         */
        public X dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return self();
        }
        /**
         * Schema name to switch and operate on.
         */
        public X schemaName(String schemaName) {
            this.schemaName = schemaName;
            return self();
        }
        /**
         * Log table name to use.
         */
        public X logName(String logName) {
            this.logName = logName;
            return self();
        }
        /**
         * Migration scripts to run.
         */
        public X migrations(Collection<MigrationScript> migrations) {
            if (CollectionUtils.isNotEmpty(migrations)) {
                for (MigrationScript ms : migrations) {
                    migration(ms);
                }
            }
            return self();
        }
        /**
         * Migration scripts to run.
         */
        public X migrations(MigrationScript... migrations) {
            for (int i = 0; ArrayUtils.isNotEmpty(migrations) && i < migrations.length; i++) {
                migration(migrations[i]);
            }
            return self();
        }
        /**
         * Migration scripts to run.
         */
        public X migration(MigrationScript migration) {

            if (Objects.nonNull(migration)) {

                if (Objects.isNull(migrations)) {
                    migrations = new ArrayList<>();
                }

                migrations.add(migration);
            }

            return self();
        }
        /**
         * Self cast.
         * @return this
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
        /**
         * Build method.
         * @return context
         */
        public abstract DatabaseMigrationContext build();
    }
}
