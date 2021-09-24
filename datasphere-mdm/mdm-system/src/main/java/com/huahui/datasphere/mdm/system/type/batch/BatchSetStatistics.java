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

package com.huahui.datasphere.mdm.system.type.batch;

import java.util.List;

import com.huahui.datasphere.mdm.system.dto.ExecutionResult;

/**
 * Batch statistics base.
 * @author theseusyang on Dec 13, 2019
 */
public interface BatchSetStatistics<T extends ExecutionResult> {
    /**
     * Clear state.
     */
    void reset();
    /**
     * Returns the result collecting state.
     * @return true, if currently set to collect results
     */
    boolean collectResults();
    /**
     * Sets this statistic collector to collect (or not) execution results.
     * @param collectOutput the state to set
     */
    void collectResults(boolean collectOutput);
    /**
     * Gets the execution results, if they are set to be collected.
     * @return results or empty list
     */
    List<T> getResults();
}
