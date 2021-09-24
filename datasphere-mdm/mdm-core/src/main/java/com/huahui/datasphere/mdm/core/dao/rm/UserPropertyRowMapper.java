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
 * Date: 05.07.2016
 */

package com.huahui.datasphere.mdm.core.dao.rm;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.huahui.datasphere.mdm.core.po.security.UserPropertyPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyPO.FieldColumns;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class UserPropertyRowMapper implements RowMapper<UserPropertyPO> {
    @Override
    public UserPropertyPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserPropertyPO result = new UserPropertyPO();

        result.setId(rs.getLong(FieldColumns.ID.name()));
        result.setName(rs.getString(FieldColumns.NAME.name()));
        result.setDisplayName(rs.getString(FieldColumns.DISPLAY_NAME.name()));
        result.setRequired(rs.getBoolean(FieldColumns.REQUIRED.name()));
        result.setReadOnly(rs.getBoolean(FieldColumns.READ_ONLY.name()));
        result.setFieldType(rs.getString(FieldColumns.FIELD_TYPE.name()));

        return result;
    }
}
