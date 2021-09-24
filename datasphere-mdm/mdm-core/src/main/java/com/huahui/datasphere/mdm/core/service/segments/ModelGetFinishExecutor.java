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

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.huahui.datasphere.mdm.system.type.pipeline.Finish;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;

import com.huahui.datasphere.mdm.core.context.ModelGetContext;
import com.huahui.datasphere.mdm.core.dto.ModelGetResult;
import com.huahui.datasphere.mdm.core.module.CoreModule;
import com.huahui.datasphere.mdm.core.service.MetaModelService;
import com.huahui.datasphere.mdm.core.type.model.ModelImplementation;

/**
 * @author theseusyang on Nov 28, 2019
 * Model type agnostic get executor.
 */
@Component(ModelGetFinishExecutor.SEGMENT_ID)
public class ModelGetFinishExecutor extends Finish<ModelGetContext, ModelGetResult> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = CoreModule.MODULE_ID + "[MODEL_GET_FINISH]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = CoreModule.MODULE_ID + ".model.get.finish.description";
    /**
     * MMS. Cheap and dirty.
     */
    @Autowired
    private MetaModelService metaModelService;
    /**
     * Constructor.
     */
    public ModelGetFinishExecutor() {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, ModelGetResult.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ModelGetResult finish(ModelGetContext ctx) {

        ModelImplementation<?> mi = metaModelService.implementation(ctx.getTypeId());
        if (Objects.nonNull(mi)) {
            return mi.get(ctx);
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Start<?, ?> start) {
        return ModelGetContext.class.isAssignableFrom(start.getInputTypeClass());
    }
}
