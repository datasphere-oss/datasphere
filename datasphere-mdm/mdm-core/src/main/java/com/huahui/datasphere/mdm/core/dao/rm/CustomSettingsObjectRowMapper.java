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

import com.huahui.datasphere.mdm.core.po.CustomStorageRecordPO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Dmitry Kopin
 * Mapper for custom settings object
 */
public class CustomSettingsObjectRowMapper implements RowMapper<CustomStorageRecordPO> {



    /**
     * Default reusable row mapper.
     */
    public static final CustomSettingsObjectRowMapper DEFAULT_ROW_MAPPER = new CustomSettingsObjectRowMapper();

    /**
     * Constructor.
     */
    public CustomSettingsObjectRowMapper() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomStorageRecordPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomStorageRecordPO result = new CustomStorageRecordPO();
        result.setKey(rs.getString(CustomStorageRecordPO.FIELD_KEY));
        if(Objects.equals(result.getKey(), "")){
            result.setKey(null);
        }
        result.setUser(rs.getString(CustomStorageRecordPO.FIELD_USER_NAME));
        if(Objects.equals(result.getUser(), "")){
            result.setUser(null);
        }
        result.setValue(rs.getString(CustomStorageRecordPO.FIELD_VALUE));
        result.setUpdateDate(rs.getTimestamp(CustomStorageRecordPO.FIELD_UPDATE_DATE));
        return result;
    }

}
