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
package com.huahui.datasphere.mdm.rest.core.ro.pipeline;

import java.util.Collections;
import java.util.List;

/**
 * @author theseusyang on Nov 25, 2019
 */
public class PipelineRO {

    private String startId;

    private String subjectId;

    private String description;

    private List<SegmentRO> segments;

    public String getStartId() {
        return startId;
    }

    public void setStartId(String type) {
        this.startId = type;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subject) {
        this.subjectId = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SegmentRO> getSegments() {
        return segments == null ? Collections.emptyList() : segments;
    }

    public void setSegments(List<SegmentRO> segments) {
        this.segments = segments;
    }
}
