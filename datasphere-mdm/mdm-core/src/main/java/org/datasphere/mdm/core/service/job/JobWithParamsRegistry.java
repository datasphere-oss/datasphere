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

/**
 * Date: 16.03.2016
 */

package com.huahui.datasphere.mdm.core.service.job;

import java.util.Set;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public interface JobWithParamsRegistry extends JobRegistry {
    void register(JobTemplateParameters parameters) throws DuplicateJobException;

   	void unregister(String jobName);

    Set<String> getJobParameterNames();

    JobTemplateParameters getJobTemplateParameters(String name);

    void registerTriggerListener(Job job);
}
