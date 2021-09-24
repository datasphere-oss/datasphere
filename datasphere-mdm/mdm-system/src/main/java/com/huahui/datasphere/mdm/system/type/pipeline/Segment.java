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

/**
 * @author theseusyang
 * Segment (integration point)
 */
public abstract class Segment implements PipelineElement {
    /**
     * Segment id.
     */
    protected final String id;
    /**
     * Segment descripton.
     */
    protected final String description;
    /**
     * Constructor.
     * @param id the ID
     * @param description the description
     */
    protected Segment(String id, String description) {
        super();
        this.id = id;
        this.description = description;
    }
    /**
     * Gets the segment ID. Must be unique accross the system.
     * @return ID
     */
    public String getId() {
        return id;
    }
    /**
     * Gets type description.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the segment type.
     * @return type
     */
    public abstract SegmentType getType();
    /**
     * Check for supporting of pipelines, initiated by this starting point.
     * @param start the starting point
     * @return true, if supports, false otherwise
     */
    public abstract boolean supports(Start<?, ?> start);
    /**
     * Returns true, if this segment is an anonymous one
     * and should not be persisted to pipeline configuration.
     * @return true, if this segment is an anonymous one
     */
    public boolean isAnonymous() {
        return false;
    }
    /**
     * Marks a segment as a participant in a batched pipeline.
     * Pipelines, started by a start segment denoted as batched, become batched pipelines.
     * @return true, for batched, false otherwise
     */
    public abstract boolean isBatched();
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSegment() {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnection() {
        return false;
    }
}
