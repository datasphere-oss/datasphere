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

package com.huahui.datasphere.mdm.system.type.pipeline;

import java.util.Objects;

/**
 * The segment id type.
 * @author theseusyang on Nov 13, 2019
 */
public final class SegmentId<T extends Segment> {
    /**
     * This segment string id.
     */
    private final String id;
    /**
     * Segment description.
     */
    private final String description;
    /**
     * The link to segment.
     */
    private final T segment;
    /**
     * Constructor.
     */
    public SegmentId(String id, String description, T segment) {
        super();

        Objects.requireNonNull(id, "Segment id must not be null.");
        Objects.requireNonNull(description, "Segment description must not be null.");
        Objects.requireNonNull(segment, "Segment instance must not be null.");

        this.id = id;
        this.description = description;
        this.segment = segment;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return the segment
     */
    public T getSegment() {
        return segment;
    }
}
