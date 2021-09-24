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

package com.huahui.datasphere.mdm.core.util;

import com.huahui.datasphere.mdm.core.type.job.StepExecutionState;

/**
 * @author theseusyang on Dec 19, 2019
 */
public class JobUtils {
     /**
     * All.
     */
    public static final String JOB_ALL = "ALL";
    /**
     * Partition mark.
     */
    public static final String JOB_PARTITION = "partition:";
    /**
     * Member UUID.
     */
    public static final String JOB_CLUSTER_MEMBER_UUID = "memberUUID";
    /**
     * Step's static parameters.
     */
    private static ThreadLocal<StepExecutionState> stepStateStorage = new ThreadLocal<>();
    /**
     * Constructor.
     */
    private JobUtils() {
        super();
    }
    /**
     * Generates partition name. Just an int to string for now.
     * @param i partition number
     * @return name
     */
    public static String partitionName(int i) {
        return JOB_PARTITION + Integer.toString(i);
    }

    /**
     * Generates partition name. Just an int to string for now.
     * @param i partition number
     * @return name
     */
    public static String targetedPartitionName(int i, String targetUUID) {
        return JOB_PARTITION + Integer.toString(i) + ":" + targetUUID;
    }
    /**
     * Reference name constructor.
     * @param runId the run id
     * @param objectName the object name
     * @return name
     */
    public static String getObjectReferenceName(String runId, String objectName) {

        return new StringBuilder()
                .append(runId)
                .append("_")
                .append(objectName)
                .toString();
    }
    public static String getObjectReferenceName(String runId, String objectName, String objectDetails) {

        return new StringBuilder()
                .append(runId)
                .append("_")
                .append(objectName)
                .append("_")
                .append(objectDetails)
                .toString();
    }
    /**
     * Gets staep execution state object.
     * @return state object
     */
    @SuppressWarnings("unchecked")
    public static<T extends StepExecutionState> T getStepState() {
        return (T) stepStateStorage.get();
    }
    /**
     * Sets step state object.
     * @param eo the object to set
     */
    public static void setStepState(StepExecutionState state) {
        stepStateStorage.set(state);
    }
    /**
     * Removes current step state object.
     */
    @SuppressWarnings("unchecked")
    public static<T extends StepExecutionState> T removeStepState() {
        T t = (T) stepStateStorage.get();
        stepStateStorage.remove();
        return t;
    }
}
