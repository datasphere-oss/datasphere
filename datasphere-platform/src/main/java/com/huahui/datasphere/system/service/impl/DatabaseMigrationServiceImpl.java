package com.huahui.datasphere.system.service.impl;

import java.sql.Connection;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.system.context.DatabaseMigrationContext;
import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.service.DatabaseMigrationService;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.database.Selector;
import nl.myndocs.database.migrator.database.query.Database;
import nl.myndocs.database.migrator.processor.Migrator;

/**
 * @author Mikhail Mikhailov on Apr 9, 2020
 */
@Service
public class DatabaseMigrationServiceImpl implements DatabaseMigrationService {
    /**
     * {@inheritDoc}
     */
    @Override
    public void migrate(DatabaseMigrationContext ctx) {

        try (Connection connection = ctx.getDataSource().getConnection()) {

            Database database = new Selector().loadFromConnection(connection, ctx.getSchemaName());
            Migrator migrator = new Migrator(database, ctx.getLogName());

            migrator.migrate(ctx, ctx.getMigrations().toArray(MigrationScript[]::new));

        } catch (Exception exc) {
            throw new PlatformFailureException("Migration failed!", exc,
                    SystemExceptionIds.EX_SYSTEM_MIGRATE_SCHEMA_FAILED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void migrate(Collection<DatabaseMigrationContext> ctxs) {
        if (CollectionUtils.isNotEmpty(ctxs)) {
            ctxs.forEach(this::migrate);
        }
    }
}
