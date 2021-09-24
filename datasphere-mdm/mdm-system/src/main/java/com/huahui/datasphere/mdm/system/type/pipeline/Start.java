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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author theseusyang
 * The pipeline starting segment.
 */
public abstract class Start<C extends PipelineInput, O extends PipelineOutput> extends Segment {
    /**
     * The input type class.
     */
    private final Class<C> inputTypeClass;
    /**
     * The output type class.
     */
    private final Class<O> outputTypeClass;
    /**
     * Constructor.
     * @param id the id
     * @param description the description
     * @param inputTypeClass the input type class
     * @param outputTypeClass the output type class
     */
    protected Start(String id, String description, Class<C> inputTypeClass, Class<O> outputTypeClass) {
        super(id, description);
        this.inputTypeClass = inputTypeClass;
        this.outputTypeClass = outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.START;
    }
    /**
     * @return the inputTypeClass
     */
    public Class<C> getInputTypeClass() {
        return inputTypeClass;
    }
    /**
     * @return the outputTypeClass
     */
    public Class<O> getOutputTypeClass() {
        return outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Start<?, ?> start) {
        return start == this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Performs the step.
     * @param ctx the context
     */
    public abstract void start(@Nonnull C ctx);
    /**
     * Selects execution subject for the supplied context, if possible.
     * @param ctx the context to use
     * @return execution subject or null
     */
    @Nullable
    public abstract String subject(C ctx);
}
