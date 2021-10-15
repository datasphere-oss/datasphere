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

package org.datasphere.mdm.core.migration.audit;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Column;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * @author Alexander Malyshev
 */
public class UN11979InitAuditTables implements MigrationScript {

    @Override
    public String migrationId() {
        return "UN-11979__InitAuditTables";
    }

    @Override
    public String author() {
        return "Alexander Malyshev";
    }

    @Override
    public void migrate(Migration migration) {
        migration.table("audit_event")
                .addColumn("type", Column.TYPE.VARCHAR, cb -> cb.notNull(true).size(255))
                .addColumn("parameters", Column.TYPE.TEXT)
                .addColumn("success", Column.TYPE.BOOLEAN, cb -> cb.notNull(true))
                .addColumn("login", Column.TYPE.VARCHAR, cb -> cb.notNull(true).size(255))
                .addColumn("client_ip", Column.TYPE.VARCHAR, cb -> cb.notNull(true).size(255))
                .addColumn("server_ip", Column.TYPE.VARCHAR, cb -> cb.notNull(true).size(255))
                .addColumn("endpoint", Column.TYPE.VARCHAR, cb -> cb.notNull(true).size(32))
                .addColumn("when_happened", Column.TYPE.TIMESTAMP, cb -> cb.notNull(true))
                .save();
    }
}
