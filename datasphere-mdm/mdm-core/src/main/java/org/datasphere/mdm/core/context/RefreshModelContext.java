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

package com.huahui.datasphere.mdm.core.context;

import com.huahui.datasphere.mdm.core.service.segments.ModelRefreshStartExecutor;

/**
 * @author maria.chistyakova
 * @since  18.12.2019
 * Refresh model context - reload a model by type/instance IDs.
 */
public class RefreshModelContext extends AbstractModelRefreshContext {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 2456877118482094107L;
    /**
     * Constructor.
     *
     * @param b
     */
    public RefreshModelContext(RefreshModelContextBuilder b) {
        super(b);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStartTypeId() {
        return ModelRefreshStartExecutor.SEGMENT_ID;
    }
    /**
     * @return builder
     */
    public static RefreshModelContextBuilder builder() {
        return new RefreshModelContextBuilder();
    }
    /**
     * @author theseusyang on Oct 26, 2020
     * Publish model context -  - reload a model by type/instance IDs builder.
     */
    public static class RefreshModelContextBuilder extends AbstractModelRefreshContextBuilder<RefreshModelContextBuilder> {
        /**
         * {@inheritDoc}
         */
        @Override
        public RefreshModelContext build() {
            return new RefreshModelContext(this);
        }
    }
}
