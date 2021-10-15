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

import com.huahui.datasphere.mdm.core.po.job.JobParameterPO;

import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Denis Kostovarov
 */
public class JobParameterRowMapper implements RowMapper<JobParameterPO> {
    @Override
    public JobParameterPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String name = rs.getString(JobParameterPO.FIELD_NAME);
        final String valStr = rs.getString(JobParameterPO.FIELD_VAL_STRING);
        Long valLong = rs.getLong(JobParameterPO.FIELD_VAL_LONG);
        if (rs.wasNull()) {
            valLong = null;
        }
        Double valDouble = rs.getDouble(JobParameterPO.FIELD_VAL_DOUBLE);
        if (rs.wasNull()) {
            valDouble = null;
        }
        final Timestamp sqlTimestamp = rs.getTimestamp(JobParameterPO.FIELD_VAL_DATE);
        Boolean valBoolean = rs.getBoolean(JobParameterPO.FIELD_VAL_BOOLEAN);
        if (rs.wasNull()) {
            valBoolean = null;
        }

        String[] valStringArr = null;
        Array stringArr = rs.getArray(JobParameterPO.FIELD_VAL_ARR_STRING);
        if (stringArr != null) {
            valStringArr = (String[])stringArr.getArray();
        }

        Timestamp[] valDateArr = null;
        Array dateArr = rs.getArray(JobParameterPO.FIELD_VAL_ARR_DATE);
        if (dateArr != null) {
            valDateArr = (Timestamp[])dateArr.getArray();
        }

        Long[] valLongArr = null;
        Array longArr = rs.getArray(JobParameterPO.FIELD_VAL_ARR_LONG);
        if (longArr != null) {
            valLongArr = (Long[])longArr.getArray();
        }

        Double[] valDoubleArr = null;
        Array doubleArr = rs.getArray(JobParameterPO.FIELD_VAL_ARR_DOUBLE);
        if (doubleArr != null) {
            valDoubleArr = (Double[])doubleArr.getArray();
        }

        Boolean[] valBooleanArr = null;
        Array booleanArr = rs.getArray(JobParameterPO.FIELD_VAL_ARR_BOOLEAN);
        if (booleanArr != null) {
            valBooleanArr = (Boolean[])booleanArr.getArray();
        }

        final JobParameterPO param = validateAndCreateJobParameter(name,
                valStr, sqlTimestamp, valLong, valDouble, valBoolean,
                valStringArr, valDateArr, valLongArr, valDoubleArr, valBooleanArr);

        if (param != null) {
            final long id = rs.getLong(JobParameterPO.FIELD_ID);
            final long jobId = rs.getLong(JobParameterPO.FIELD_JOB_ID);
            final String createdBy = rs.getString(JobParameterPO.FIELD_CREATED_BY);
            final String updatedBy = rs.getString(JobParameterPO.FIELD_UPDATED_BY);
            final Date createDate = rs.getDate(JobParameterPO.FIELD_CREATE_DATE);
            final Date updateDate = rs.getDate(JobParameterPO.FIELD_UPDATE_DATE);

            param.setCreatedBy(createdBy);
            param.setUpdatedBy(updatedBy);
            param.setCreateDate(createDate);
            param.setUpdateDate(updateDate);
            param.setId(id);
            param.setJobId(jobId);
        }

        return param;
    }

    public static JobParameterPO validateAndCreateJobParameter(final String name, final String valStr,
                                                               final Timestamp sqlTimestamp, final Long valLong,
                                                               final Double valDouble, final Boolean valBoolean,
                                                               final String[] valStringArr,
                                                               final Timestamp[] valDateArr,
                                                               final Long[] valLongArr,
                                                               final Double[] valDoubleArr,
                                                               final Boolean[] valBooleanArr) {
        JobParameterPO param = null;
        int identified = 0;
        if (valStr != null) {
            param = new JobParameterPO(name, valStr);
            ++identified;
        }
        if (valLong != null && ++identified == 1) {
            param = new JobParameterPO(name, valLong);
        }
        if (valDouble != null && ++identified == 1) {
            param = new JobParameterPO(name, valDouble);
        }
        if (sqlTimestamp != null && ++identified == 1) {
            final ZonedDateTime zonedTime = ZonedDateTime.ofInstant(sqlTimestamp.toInstant(), ZoneId.systemDefault());
            param = new JobParameterPO(name, zonedTime);
        }
        if (valBoolean != null && ++identified == 1) {
            param = new JobParameterPO(name, valBoolean);
        }

        // Arrays.
        if (valStringArr != null && ++identified == 1) {
            param = new JobParameterPO(name, valStringArr);
        }
        if (valDateArr != null && ++identified == 1) {
            ZonedDateTime[] zonedDateTimeArr = convertTimestamp(valDateArr);
            param = new JobParameterPO(name, zonedDateTimeArr);
        }
        if (valLongArr != null && ++identified == 1) {
            param = new JobParameterPO(name, valLongArr);
        }
        if (valDoubleArr != null && ++identified == 1) {
            param = new JobParameterPO(name, valDoubleArr);
        }
        if (valBooleanArr != null && ++identified == 1) {
            param = new JobParameterPO(name, valBooleanArr);
        }

        if (identified > 1) {
            param = null;
        }

        return param;
    }

    private static ZonedDateTime[] convertTimestamp(Timestamp[] timestamps) {
        ZonedDateTime[] result = new ZonedDateTime[timestamps.length];

        for (int i = 0; i < timestamps.length; i++) {
            result[i] = ZonedDateTime.ofInstant(timestamps[i].toInstant(), ZoneId.systemDefault());
        }

        return result;
    }
}
