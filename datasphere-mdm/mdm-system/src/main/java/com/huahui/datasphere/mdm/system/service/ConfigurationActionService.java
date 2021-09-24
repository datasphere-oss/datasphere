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

package com.huahui.datasphere.mdm.system.service;

import javax.annotation.Nonnull;

import com.huahui.datasphere.mdm.system.type.action.ConfigurationAction;

/**
 * @author theseusyang
 * Configuration action service.
 * This service runs configuration actions in 'once for the cluster' or 'strictly n times'.
 */
public interface ConfigurationActionService {
    /**
     * Executes the supplied action, blocking other actions of the same type in all other threads cluster wide.
     * Runs only if the execution counter has not reached action's nTimes.
     * @param action the action to execute
     * @return true if the action was executed, false if not, either because the nTimes counter was reached or retries numer was exceeded.
     */
    boolean execute(@Nonnull ConfigurationAction action);
}
