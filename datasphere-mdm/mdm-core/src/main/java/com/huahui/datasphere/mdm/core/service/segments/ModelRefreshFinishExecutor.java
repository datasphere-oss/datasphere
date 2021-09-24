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
import com.huahui.datasphere.mdm.system.type.pipeline.Finish;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;
import com.huahui.datasphere.mdm.system.type.pipeline.defaults.VoidPipelineOutput;

import com.huahui.datasphere.mdm.core.context.ModelRefreshContext;
import com.huahui.datasphere.mdm.core.module.CoreModule;

/**
 * @author maria.chistyakova
 * @since  06.12.2019
 */
@Component(ModelRefreshFinishExecutor.SEGMENT_ID)
public class ModelRefreshFinishExecutor extends Finish<ModelRefreshContext, VoidPipelineOutput> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = CoreModule.MODULE_ID + "[MODEL_REFRESH_FINISH]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = CoreModule.MODULE_ID + ".model.refresh.finish.description";
    /**
     * Constructor.
     */
    public ModelRefreshFinishExecutor() {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, VoidPipelineOutput.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public VoidPipelineOutput finish(ModelRefreshContext ctx) {
        return VoidPipelineOutput.INSTANCE;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Start<?, ?> start) {
        return ModelRefreshContext.class.isAssignableFrom(start.getInputTypeClass());
    }
}
