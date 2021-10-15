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
/**
 * Date: 18.03.2016
 */

package com.huahui.datasphere.platform.rest.core.ro.job;

import java.util.List;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class JobMetaInfoRO {
    private String jobNameReference;
    private List<JobParameterRO> parameters;

    public String getJobNameReference() {
        return jobNameReference;
    }

    public void setJobNameReference(String jobNameReference) {
        this.jobNameReference = jobNameReference;
    }

    public List<JobParameterRO> getParameters() {
        return parameters;
    }

    public void setParameters(List<JobParameterRO> parameters) {
        this.parameters = parameters;
    }
}
