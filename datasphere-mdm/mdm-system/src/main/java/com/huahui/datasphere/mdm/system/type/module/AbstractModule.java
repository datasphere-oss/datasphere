/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.system.type.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.mdm.system.type.pipeline.Segment;

/**
 * Abstract segments holder.
 * @author theseusyang on Nov 27, 2019
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
