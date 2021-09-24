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
package com.huahui.datasphere.mdm.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huahui.datasphere.mdm.system.type.pipeline.fragment.InputFragmentContainer;

import com.huahui.datasphere.mdm.core.context.ModelIdentityContext;
import com.huahui.datasphere.mdm.core.type.event.RefreshModelEvent.RefreshModelTuple;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * @author theseusyang on Oct 27, 2020
 * Ids collector.
 */
public interface ModelIdentitySupport {
    /**
     * Collects model ids recursively.
     * @param ctx the input
     * @return map
     */
    default Map<String, Map<String, Set<String>>> collectAsMap(ModelIdentityContext ctx) {

        Map<String, Map<String, Set<String>>> result = new LinkedHashMap<>();
        result
            .computeIfAbsent(ctx.getTypeId(), k -> new HashMap<String, Set<String>>())
            .computeIfAbsent(SecurityUtils.getStorageId(ctx), k -> new HashSet<String>())
            .add(ctx.getInstanceId());

        if (ctx instanceof InputFragmentContainer) {
            ((InputFragmentContainer) ctx).recursive(f -> {

                if (f instanceof ModelIdentityContext) {

                    ModelIdentityContext mcc = (ModelIdentityContext) f;
                    result
                        .computeIfAbsent(mcc.getTypeId(), k -> new HashMap<String, Set<String>>())
                        .computeIfAbsent(SecurityUtils.getStorageId(mcc), k -> new HashSet<String>())
                        .add(mcc.getInstanceId());
                }
            });
        }

        return result;
    }

    default Collection<RefreshModelTuple> collectAsTuples(ModelIdentityContext ctx) {

        List<RefreshModelTuple> result = new ArrayList<>();

        result.add(new RefreshModelTuple(ctx.getTypeId(), SecurityUtils.getStorageId(ctx), ctx.getInstanceId()));
        if (ctx instanceof InputFragmentContainer) {
            ((InputFragmentContainer) ctx).recursive(f -> {

                if (f instanceof ModelIdentityContext) {
                    ModelIdentityContext mcc = (ModelIdentityContext) f;
                    result.add(new RefreshModelTuple(mcc.getTypeId(), SecurityUtils.getStorageId(mcc), mcc.getInstanceId()));
                }
            });
        }

        return result;
    }
}
