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

package org.datasphere.mdm.core.context;

import org.datasphere.mdm.core.service.segments.ModelRefreshStartExecutor;

/**
 * @author maria.chistyakova
 * @since  18.12.2019
 * Refresh model context - reload a model by type/instance IDs.
 */
public class RefreshModelContext extends AbstractModelRefreshContext {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 2456877118482094107L;
    /**
     * Constructor.
     *
     * @param b
     */
    public RefreshModelContext(RefreshModelContextBuilder b) {
        super(b);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStartTypeId() {
        return ModelRefreshStartExecutor.SEGMENT_ID;
    }
    /**
     * @return builder
     */
    public static RefreshModelContextBuilder builder() {
        return new RefreshModelContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Oct 26, 2020
     * Publish model context -  - reload a model by type/instance IDs builder.
     */
    public static class RefreshModelContextBuilder extends AbstractModelRefreshContextBuilder<RefreshModelContextBuilder> {
        /**
         * {@inheritDoc}
         */
        @Override
        public RefreshModelContext build() {
            return new RefreshModelContext(this);
        }
    }
}
