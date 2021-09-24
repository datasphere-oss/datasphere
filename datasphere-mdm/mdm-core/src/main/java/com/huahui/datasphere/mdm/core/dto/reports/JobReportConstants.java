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

package com.huahui.datasphere.mdm.core.dto.reports;

/**
 * @author theseusyang
 * Various job report constants.
 */
public class JobReportConstants {
    public static final String JOB_NAMED_SUFFIX = ".named";
    /**
     * Started.
     */
    public static final String JOB_STATUS_STARTED = "app.job.status.started";
    /**
     * Starting.
     */
    public static final String JOB_STATUS_STARTING = "app.job.status.starting";
    /**
     * Completed.
     */
    public static final String JOB_STATUS_COMPLETED = "app.job.status.completed";
    /**
     * Stopping.
     */
    public static final String JOB_STATUS_STOPPING = "app.job.status.stopping";
    /**
     * Stopped.
     */
    public static final String JOB_STATUS_STOPPED = "app.job.status.stopped";
    /**
     * Failed.
     */
    public static final String JOB_STATUS_FAILED = "app.job.status.failed";
    /**
     * Unknown.
     */
    public static final String JOB_STATUS_UNKNOWN = "app.job.status.unknown";
    /**
     * Records (first plural).
     */
    public static final String JOB_REPORT_RECORDS_1 = "app.job.report.term.records.1";
    /**
     * Records (second plural).
     */
    public static final String JOB_REPORT_RECORDS_2 = "app.job.report.term.records.2";
    /**
     * Records (singular).
     */
    public static final String JOB_REPORT_RECORD = "app.job.report.term.record";

    public static final String USER_NAME_PARAM = "userName";

    public static final String JOB_REPORT_TYPE = "app.job.report.reportType";

    /**
     * Constructor.
     */
    private JobReportConstants() {
        super();
    }

}
