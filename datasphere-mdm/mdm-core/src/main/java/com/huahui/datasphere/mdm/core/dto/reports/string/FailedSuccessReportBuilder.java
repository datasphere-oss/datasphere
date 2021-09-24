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

import org.apache.commons.lang3.StringUtils;

public class FailedSuccessReportBuilder {
    private int success;
    private int failed;
    private String empty;
    private String successMessage;
    private String failedMessage;
    private boolean noTrailingSpace;
    private boolean noTrailingDot;
    private boolean noLineSeparator;
    private String valuesSeparator;
    private Function<Integer, String> mapper;

    FailedSuccessReportBuilder() {
    }

    public FailedSuccessReportBuilder setSuccessCount(int success) {
        this.success = success;
        return this;
    }

    public FailedSuccessReportBuilder setFailedCount(int failed) {
        this.failed = failed;
        return this;
    }

    public FailedSuccessReportBuilder setEmptyMessage(String empty) {
        this.empty = empty;
        return this;
    }

    public FailedSuccessReportBuilder setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        return this;
    }

    public FailedSuccessReportBuilder setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
        return this;
    }

    public FailedSuccessReportBuilder setMapper(Function<Integer, String> mapper) {
        this.mapper = mapper;
        return this;
    }

    public FailedSuccessReportBuilder noTrailingSpace(boolean noTrailingSpace) {
        this.noTrailingSpace = noTrailingSpace;
        return this;
    }

    public FailedSuccessReportBuilder noTrailingDot(boolean noTrailingDot) {
        this.noTrailingDot = noTrailingDot;
        return this;
    }

    public FailedSuccessReportBuilder noLineSeparator(boolean noLineSeparator) {
        this.noLineSeparator = noLineSeparator;
        return this;
    }

    public FailedSuccessReportBuilder valuesSeparator(String valuesSeparator) {
        this.valuesSeparator = valuesSeparator;
        return this;
    }

    public FailedSuccessReport createFailedSuccessReport() {
        assert !StringUtils.isBlank(successMessage);
        assert !StringUtils.isBlank(empty);
        assert !StringUtils.isBlank(failedMessage);
        assert mapper != null;
        return new FailedSuccessReport(success, failed, empty, successMessage, failedMessage, mapper, noTrailingSpace,
                noTrailingDot, noLineSeparator, valuesSeparator);
    }
}