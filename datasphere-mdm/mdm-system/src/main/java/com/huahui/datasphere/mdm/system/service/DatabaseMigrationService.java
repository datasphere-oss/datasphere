package com.huahui.datasphere.mdm.system.service;

import java.util.Collection;

import com.huahui.datasphere.mdm.system.context.DatabaseMigrationContext;

/**
 * @author theseusyang
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
