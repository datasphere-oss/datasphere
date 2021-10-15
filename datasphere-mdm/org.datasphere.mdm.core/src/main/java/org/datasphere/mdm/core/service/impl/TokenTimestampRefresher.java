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

package org.datasphere.mdm.core.service.impl;

import java.util.Map.Entry;

import org.datasphere.mdm.core.type.security.SecurityToken;

import com.hazelcast.core.ReadOnly;
import com.hazelcast.map.EntryProcessor;

/**
 * @author Mikhail Mikhailov
 * Bogus entry processor, dedicated to timestamp renewal after near cache reads.
 */
public class TokenTimestampRefresher implements EntryProcessor<String, SecurityToken, String>, ReadOnly {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 4779553343762697067L;
    /**
     * {@inheritDoc}
     */
    @Override
    public String process(Entry<String, SecurityToken> entry) {
        // We're done with this
        return entry.getKey();
    }
}
