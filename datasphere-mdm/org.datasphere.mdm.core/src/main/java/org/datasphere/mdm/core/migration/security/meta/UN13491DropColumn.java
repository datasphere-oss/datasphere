package org.datasphere.mdm.core.migration.security.meta;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Column;
import nl.myndocs.database.migrator.definition.Constraint;
import nl.myndocs.database.migrator.definition.Index;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * @author Alexey Tsarapkin
 */
public class UN13491DropColumn implements MigrationScript {

    /**
     * Constructor.
     */
    public UN13491DropColumn() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String author() {
        return "Alexey Tsarapkin";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String migrationId() {
        return "UN13491DropColumn";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void migrate(Migration migration) {
        migration.table("s_custom_storage")
                .dropColumn("s_api_id")
                .save();
    }
}