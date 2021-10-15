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
package org.datasphere.mdm.core.dao.impl;

import javax.sql.DataSource;

import org.datasphere.mdm.core.dao.BoundedDAO;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

/**
 * @author Mikhail Mikhailov on Sep 9, 2021
 */
public abstract class BoundedDAOImpl extends BaseDAOImpl implements BoundedDAO {
    /**
     * The object's name this DAO is bound to.
     */
    private final String name;

    /**
     * Constructor.
     * @param dataSource
     */
    protected BoundedDAOImpl(DataSource dataSource, String name) {
        super(dataSource);
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void lock() {
        super.lock(getName());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
}
