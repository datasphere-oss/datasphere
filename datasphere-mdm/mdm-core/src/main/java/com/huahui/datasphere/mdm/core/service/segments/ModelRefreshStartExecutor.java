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

import com.huahui.datasphere.mdm.core.context.ModelRefreshContext;
import com.huahui.datasphere.mdm.core.module.CoreModule;

/**
 * @author maria.chistyakova
 * @since  06.12.2019
 */
@Component(ModelRefreshStartExecutor.SEGMENT_ID)
public class ModelRefreshStartExecutor extends Start<ModelRefreshContext, VoidPipelineOutput> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = CoreModule.MODULE_ID + "[MODEL_REFRESH_START]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = CoreModule.MODULE_ID + ".model.refresh.start.description";
    /**
     * Constructor.
     */
    public ModelRefreshStartExecutor() {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, ModelRefreshContext.class, VoidPipelineOutput.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(ModelRefreshContext ctx) {
        // NOOP. Start does nothing here.
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String subject(ModelRefreshContext ctx) {
        // No subject for this type of pipelines
        // This may be storage id in the future
        return null;
    }
}
