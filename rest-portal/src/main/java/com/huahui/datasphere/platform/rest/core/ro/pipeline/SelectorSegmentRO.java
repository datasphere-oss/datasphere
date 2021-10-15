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
package com.huahui.datasphere.platform.rest.core.ro.pipeline;

import org.unidata.mdm.system.type.pipeline.Selector;
import org.unidata.mdm.system.type.pipeline.connection.OutcomesPipelineConnection;

/**
 * @author Mikhail Mikhailov on May 25, 2020
 */
public class SelectorSegmentRO extends AbstractOutcomeSegmentRO {
    /**
     * Constructor.
     */
    public SelectorSegmentRO() {
        super();
    }
    /**
     * Constructor.
     * @param connection the connection
     */
    public SelectorSegmentRO(OutcomesPipelineConnection connection) {
        super(connection);
    }
    /**
     * Constructor.
     * @param segment
     */
    public SelectorSegmentRO(Selector<?, ?> segment) {
        super(segment);
    }
}
