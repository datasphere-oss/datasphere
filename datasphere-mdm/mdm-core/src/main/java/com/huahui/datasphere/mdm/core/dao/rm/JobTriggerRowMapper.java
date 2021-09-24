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

package com.huahui.datasphere.mdm.core.dao.rm;

import org.springframework.jdbc.core.RowMapper;

import com.huahui.datasphere.mdm.core.po.job.JobTriggerPO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Job row mapper.
 * @author Denis Kostovarov
 */
public class JobTriggerRowMapper implements RowMapper<JobTriggerPO> {
    @Override
    public JobTriggerPO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final JobTriggerPO jobTrigger = new JobTriggerPO();
        jobTrigger.setCreatedBy(rs.getString(JobTriggerPO.FIELD_CREATED_BY));
        jobTrigger.setUpdatedBy(rs.getString(JobTriggerPO.FIELD_UPDATED_BY));
        jobTrigger.setCreateDate(rs.getDate(JobTriggerPO.FIELD_CREATE_DATE));
        jobTrigger.setUpdateDate(rs.getDate(JobTriggerPO.FIELD_UPDATE_DATE));
        jobTrigger.setId(rs.getLong(JobTriggerPO.FIELD_ID));
        jobTrigger.setFinishJobId(rs.getLong(JobTriggerPO.FIELD_FINISH_JOB_ID));
        jobTrigger.setStartJobId(rs.getLong(JobTriggerPO.FIELD_START_JOB_ID));
        jobTrigger.setSuccessRule(rs.getBoolean(JobTriggerPO.FIELD_SUCCESS_RULE));
        jobTrigger.setName(rs.getString(JobTriggerPO.FIELD_NAME));
        jobTrigger.setDescription(rs.getString(JobTriggerPO.FIELD_DESCRIPTION));

        return jobTrigger;
    }
}
