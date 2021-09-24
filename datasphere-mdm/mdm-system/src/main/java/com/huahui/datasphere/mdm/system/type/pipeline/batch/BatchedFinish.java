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

package com.huahui.datasphere.mdm.system.type.pipeline.batch;

import com.huahui.datasphere.mdm.system.type.pipeline.Finish;

/**
 * @author theseusyang on Oct 2, 2019
 */
public abstract class BatchedFinish<I extends BatchedPipelineInput, O extends BatchedPipelineOutput> extends Finish<I, O> {
    /**
     * Constructor.
     * @param id
     * @param description
     */
    public BatchedFinish(String id, String description, Class<O> outputTypeClass) {
        super(id, description, outputTypeClass);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return true;
    }
}
