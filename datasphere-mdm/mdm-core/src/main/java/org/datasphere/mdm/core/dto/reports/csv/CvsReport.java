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

package com.huahui.datasphere.mdm.core.dto.reports.csv;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.huahui.datasphere.mdm.core.dto.reports.Report;

/**
 * Stateful class responsible for creating csv report.
 */
public class CvsReport implements Report<String> {

    /**
     * New row
     */
    private static final String NEW_ROW = "\n";

    /**
     * Separator
     */
    private final char separator;

    /**
     * CharSet
     */
    @Nonnull
    private final Charset charSet;

    /**
     * Current state of csv
     */
    private final StringBuffer result = new StringBuffer();

    /**
     * Constructor
     *
     * @param separator - csv separator
     * @param charSet   - returned charSet
     */
    public CvsReport(char separator, @Nullable String charSet) {
        this.separator = separator;
        this.charSet = charSet == null ? Charset.forName(StandardCharsets.UTF_8.name()) : Charset.forName(charSet);
    }

    /**
     * Constructor where will be default charset.
     *
     * @param separator - csv separator
     */
    public CvsReport(char separator) {
        this.separator = separator;
        this.charSet = Charset.forName(StandardCharsets.UTF_8.name());
    }

    @Override
    public void newRow() {
        result.append(NEW_ROW);
    }

    @Override
    public void addElement(@Nonnull String element) {
        String finalElement = element.replace(separator, ' ').replace(NEW_ROW, "");
        result.append(finalElement);
        result.append(separator);
    }

    @Nonnull
    @Override
    public byte[] generate() {
        return result.toString().getBytes(charSet);
    }
}
