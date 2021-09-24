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

package com.huahui.datasphere.mdm.core.dto.reports.string;

import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import com.huahui.datasphere.mdm.system.util.TextUtils;

import com.huahui.datasphere.mdm.core.dto.reports.JobReportConstants;

/**
 * Create report which look like ''
 */
//todo think about generalization (in case when more when two sentence will be present)
public class FailedSuccessReport {

    /**
     * Space
     */
    public static final char SPACE = ' ';

    /**
     * Dot
     */
    public static final char DOT = '.';

    /**
     * Colon
     */
    public static final char COLON = ':';

    /**
     * Semi - Colon
     */
    public static final char SEMI_COLON = ';';

    /**
     * Dash
     */
    public static final char DASH = '-';

    /**
     * Convert to
     *
     * @param recordCount
     * @return
     */
    @Nonnull
    public static String mapToRecords(long recordCount) {
        if (recordCount == 0) {
            return TextUtils.getText(JobReportConstants.JOB_REPORT_RECORDS_1);
        } else if (recordCount == 1) {
            return TextUtils.getText(JobReportConstants.JOB_REPORT_RECORD);
        } else if (recordCount < 5) {
            return TextUtils.getText(JobReportConstants.JOB_REPORT_RECORDS_2);
        } else {
            return TextUtils.getText(JobReportConstants.JOB_REPORT_RECORDS_1);
        }
    }

    private final int successCount;

    private final int failedCount;

    private final String emptyMessage;

    private final String successMessage;

    private final String failedMessage;

    private boolean noTrailingSpace;

    private boolean noTrailingDot;

    private boolean noLineSeparator;

    private String valuesSeparator;

    private final Function<Integer, String> mapper;

    FailedSuccessReport(int success, int failed, String empty, String successMessage, String failedMessage,
                        Function<Integer, String> mapper, boolean noTrailingSpace, boolean noTrailingDot, boolean noLineSeparator,
                        String valuesSeparator) {
        this.successCount = success;
        this.failedCount = failed;
        this.emptyMessage = empty;
        this.successMessage = successMessage;
        this.failedMessage = failedMessage;
        this.mapper = mapper;
        this.noTrailingSpace = noTrailingSpace;
        this.noTrailingDot = noTrailingDot;
        this.noLineSeparator = noLineSeparator;
        this.valuesSeparator = valuesSeparator;
    }

    public String generateReport() {

        if ((successCount + failedCount) == 0) {
            return SPACE + emptyMessage;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPACE);
        stringBuilder.append(successMessage);
        stringBuilder.append(COLON);
        stringBuilder.append(SPACE);
        stringBuilder.append(successCount);
        stringBuilder.append(noTrailingSpace ? StringUtils.EMPTY : SPACE);
        if (mapper != null) {
            stringBuilder.append(mapper.apply(successCount));
        }

        stringBuilder.append(noTrailingDot ?  StringUtils.EMPTY : DOT);
        if (failedCount > 0) {
            if (StringUtils.isNotEmpty(valuesSeparator)) {
                stringBuilder.append(valuesSeparator);
            }
            if (!noLineSeparator) {
                stringBuilder.append(StringUtils.LF);
            }
            stringBuilder.append(SPACE);
            stringBuilder.append(failedMessage);
            stringBuilder.append(COLON);
            stringBuilder.append(SPACE);
            stringBuilder.append(failedCount);
            stringBuilder.append(noTrailingSpace ? StringUtils.EMPTY : SPACE);
            if (mapper != null) {
                stringBuilder.append(mapper.apply(failedCount));
            }

            stringBuilder.append(noTrailingDot ?  StringUtils.EMPTY : DOT);
        }

        return stringBuilder.toString();
    }

    public static FailedSuccessReportBuilder builder(){
        return new FailedSuccessReportBuilder();
    }
}
