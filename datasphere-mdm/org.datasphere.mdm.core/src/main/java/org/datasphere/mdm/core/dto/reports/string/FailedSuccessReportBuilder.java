/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.dto.reports.string;

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