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

package com.huahui.datasphere.mdm.system.service;

import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;

/**
 * @author theseusyang
 * Pipeline execution service.
 */
public interface ExecutionService {
    /**
     * Calls {@link Start#subject(PipelineInput)}
     * to select a pre-configured pipeline for the context and than
     * calls the variant below.
     *
     * @param <C> the type of the request
     * @param <R> the type of the result
     * @param ctx the request context
     * @return result
     */
    <C extends PipelineInput, R extends PipelineOutput> R execute(C ctx);
    /**
     * Calls the pipeline 'p' for the supplied context 'c'.
     *
     * @param <C> the type of the request
     * @param <R> the type of the result
     * @param p a ready to execute pipeline instance
     * @param ctx the context
     * @return result
     */
    <C extends PipelineInput, R extends PipelineOutput> R execute(Pipeline p, C ctx);
}