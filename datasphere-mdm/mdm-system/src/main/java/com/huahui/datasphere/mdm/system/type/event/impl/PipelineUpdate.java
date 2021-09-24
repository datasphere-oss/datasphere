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

package com.huahui.datasphere.mdm.system.type.event.impl;

import com.huahui.datasphere.mdm.system.type.event.AbstractForeignEvent;
import com.huahui.datasphere.mdm.system.util.IdUtils;

/**
 * @author theseusyang on Nov 26, 2019
 */
public class PipelineUpdate extends AbstractForeignEvent {
    /**
     * Type of the operation.
     * @author theseusyang on Nov 27, 2019
     */
    public enum PipelineUpdateType {
        UPSERT,
        REMOVAL
    }
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 9017170992401345410L;
    /**
     * This type name.
     */
    private static final String TYPE_NAME = "PIPELINE_UPDATE_EVENT";
    /**
     * The start id.
     */
    private String startId;
    /**
     * The subject id;
     */
    private String subjectId;
    /**
     * The update type.
     */
    private PipelineUpdateType updateType;
    /**
     * Constructor.
     */
    public PipelineUpdate() {
        super(TYPE_NAME, IdUtils.v1String());
    }
    /**
     * @return the startId
     */
    public String getStartId() {
        return startId;
    }
    /**
     * @param startId the startId to set
     */
    public void setStartId(String startId) {
        this.startId = startId;
    }
    /**
     * @return the subjectId
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    /**
     * @return the updateType
     */
    public PipelineUpdateType getUpdateType() {
        return updateType;
    }
    /**
     * @param updateType the updateType to set
     */
    public void setUpdateType(PipelineUpdateType updateType) {
        this.updateType = updateType;
    }
}
