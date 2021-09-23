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

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * @author Mikhail Mikhailov on Oct 22, 2019
 */
public class UN12296InitSystemSchema implements MigrationScript {
    /**
     * Constructor.
     */
    public UN12296InitSystemSchema() {
        super();
    }

    @Override
    public String author() {
        return "mikhail.mikhailov";
    }

    @Override
    public void migrate(Migration m) {
        m.raw()
            .sql("create table modules_info ("
                + "module_id text primary key, "
                + "version text not null, "
                + "tag text, "
                + "status text" // Better use pg enum.
                + ")")
            .save();
    }

    @Override
    public String migrationId() {
        return "UN-12296__InitSystemSchema";
    }
}
