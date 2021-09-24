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
 * Point segment.
 */
public abstract class Point<C extends PipelineInput> extends Segment {
    /**
     * Constructor.
     * @param id the id
     * @param description the description
     */
    protected Point(String id, String description) {
        super(id, description);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.POINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Performs actual execution of the segment's code.
     * @param ctx the context to execute the segment on
     */
    public abstract void point(C ctx);
}
