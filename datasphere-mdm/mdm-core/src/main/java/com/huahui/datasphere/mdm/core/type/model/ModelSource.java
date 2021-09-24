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
package com.huahui.datasphere.mdm.core.type.model;

/**
 * @author theseusyang on Oct 8, 2020
 * Model source marker, the model descriptor operates on.
 */
public interface ModelSource {
    /**
     * Gets the model instance id, the source is for.
     * This is either real instance name/id (i. e. classifier name) for models that support multiple instances,
     * or just DEFAULT or null for singleton instances, such as DATA, SOURCE_SYSTEMS, ENUMERATIONS, MEASURE_UNITS etc.
     * @return model instance id
     */
    String getInstanceId();
    /**
     * Returns the model type id from configuration (the id supplied with model type descriptor for this model type).
     * @return model type id
     */
    String getTypeId();
}
