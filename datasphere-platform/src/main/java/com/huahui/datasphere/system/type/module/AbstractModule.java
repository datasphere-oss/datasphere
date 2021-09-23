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

package com.huahui.datasphere.system.type.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.system.type.pipeline.Segment;

/**
 * Abstract segments holder.
 * @author Mikhail Mikhailov on Nov 27, 2019
 */
public abstract class AbstractModule implements Module {
    /**
     * This module segments.
     */
    protected Collection<Segment> segments;
    /**
     * Adds collection of segments to this module.
     * @param segments the collectin to add
     */
    protected void addSegments(Collection<Segment> segments) {

        if (CollectionUtils.isEmpty(segments)) {
            return;
        }

        if (Objects.isNull(this.segments)) {
            this.segments = new ArrayList<>(segments);
        } else {
            this.segments.addAll(segments);
        }
    }
    /**
     * Adds collection of segments to this module.
     * @param segments the collectin to add
     */
    protected void addSegments(Segment... segments) {

        if (ArrayUtils.isEmpty(segments)) {
            return;
        }

        if (Objects.isNull(this.segments)) {
            this.segments = new ArrayList<>(Arrays.asList(segments));
        } else {
            this.segments.addAll(Arrays.asList(segments));
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getSegments() {
        return segments == null ? Collections.emptyList() : segments;
    }
}
