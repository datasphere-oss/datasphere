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

package com.huahui.datasphere.mdm.core.service.segments;

import org.springframework.stereotype.Component;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;
import com.huahui.datasphere.mdm.system.type.pipeline.defaults.VoidPipelineOutput;

import com.huahui.datasphere.mdm.core.context.ModelChangeContext;
import com.huahui.datasphere.mdm.core.module.CoreModule;

/**
 * @author maria.chistyakova
 * @since  06.12.2019
 */
@Component(ModelUpsertStartExecutor.SEGMENT_ID)
public class ModelUpsertStartExecutor extends Start<ModelChangeContext, VoidPipelineOutput> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = CoreModule.MODULE_ID + "[MODEL_UPSERT_START]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = CoreModule.MODULE_ID + ".model.upsert.start.description";
    /**
     * Constructor.
     */
    public ModelUpsertStartExecutor() {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, ModelChangeContext.class, VoidPipelineOutput.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(ModelChangeContext ctx) {
        // NOOP. Start does nothing here.
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String subject(ModelChangeContext ctx) {
        // No subject for this type of pipelines
        // This may be storage id in the future
        return null;
    }
}
