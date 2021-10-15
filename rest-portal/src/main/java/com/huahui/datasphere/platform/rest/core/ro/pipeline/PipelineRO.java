/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.ro.pipeline;

import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Mikhailov on Nov 25, 2019
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
