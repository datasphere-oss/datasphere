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
package org.datasphere.mdm.core.service.job.step;

import org.springframework.batch.core.ItemReadListener;

/**
 * @author Mikhail Mikhailov on Jul 13, 2021
 * Read item adapter.
 */
public class ItemReadListenerAdapter<T> implements ItemReadListener<T> {
    /**
     * Constructor.
     */
    public ItemReadListenerAdapter() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeRead() {
        // Override
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterRead(T item) {
        // Override
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onReadError(Exception ex) {
        // Override
    }
}
