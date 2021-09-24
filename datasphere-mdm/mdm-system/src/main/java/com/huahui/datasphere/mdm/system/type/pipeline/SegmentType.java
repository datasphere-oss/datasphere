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
 * The pipeline segment type.
 */
public enum SegmentType {
    /**
     * Pipeline's starting point, defining pipeline's input type.
     */
    START,
    /**
     * Pipeline's execution point. Contains code, processing input type.
     */
    POINT,
    /**
     * Connector type segment,
     * connecting another pipeline to this pipeline and returning intermediate result.
     */
    CONNECTOR,
    /**
     * Transforms the input for parallel execution and the joins its results as a single result.
     */
    SPLITTER,
    /**
     * Selects an execution path and runs that path, returning the result.
     */
    SELECTOR,
    /**
     * The finalizer type, preparing the pipeline's result.
     */
    FINISH,
    /**
     * Marks an action, executed only if the pipeline fails.
     * Fallbacks are executed on the order of submission to pipeline.
     */
    FALLBACK;
}
