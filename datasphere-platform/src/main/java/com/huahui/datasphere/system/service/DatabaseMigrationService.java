package com.huahui.datasphere.system.service;

import java.util.Collection;

import com.huahui.datasphere.system.context.DatabaseMigrationContext;

/**
 * @author Mikhail Mikhailov on Apr 9, 2020
 */
public interface DatabaseMigrationService {
    /**
     * Migrates a single context.
     * @param ctx the contexts
     */
    void migrate(DatabaseMigrationContext ctx);
    /**
     * Migrates a serie of contexts.
     * @param ctxs the contexts
     */
    void migrate(Collection<DatabaseMigrationContext> ctxs);
}
