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
 * Date: 06.07.2016
 */

package com.huahui.datasphere.mdm.core.dao.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.huahui.datasphere.mdm.core.po.security.RolePropertyPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyValuePO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyValuePO.FieldColumns;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class UserPropertyValueRowMapper implements RowMapper<UserPropertyValuePO> {

    /**
     * Dfeault instance singleton.
     */
    public static final UserPropertyValueRowMapper DEFAULT_ROW_MAPPER = new UserPropertyValueRowMapper();
    /**
     * Constructor.
     */
    private UserPropertyValueRowMapper() {
        super();
    }

    @Override
    public UserPropertyValuePO mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserPropertyValuePO result = new UserPropertyValuePO();

        long id = rs.getLong(FieldColumns.ID.name());
        result.setId(rs.wasNull() ? null : id);

        result.setUserId(rs.getLong(FieldColumns.USER_ID.name()));
        result.setValue(rs.getString(FieldColumns.VALUE.name()));

        long propertyId = rs.getLong(FieldColumns.PROPERTY_ID.name());

        UserPropertyPO property = new UserPropertyPO();
        property.setId(propertyId);
        property.setName(rs.getString(RolePropertyPO.FieldColumns.NAME.name()));
        property.setDisplayName(rs.getString(RolePropertyPO.FieldColumns.DISPLAY_NAME.name()));

        result.setProperty(property);

        return result;
    }
}