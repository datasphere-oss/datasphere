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

import javax.annotation.Nonnull;

/**
 * @param <E> type of elements in a report
 */
public interface Report<E> {

    /**
     * Start new row in a report
     * Note: A meaning of row depends on type of report.
     */
    void newRow();

    /**
     * Add element of report to row.
     *
     * @param element - element of report in row
     */
    void addElement(@Nonnull E element);

    /**
     * @return array of bytes
     */
    @Nonnull
    byte[] generate();
}
