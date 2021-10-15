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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.huahui.datasphere.mdm.core.po.security.LabelPO;

/**
 * Row mapper for the LabelPO object.
 * @author ilya.bykov
 */
public class LabelRowMapper implements RowMapper<LabelPO> {

    /**
     * RM singleton.
     */
    public static final LabelRowMapper DEFAULT_LABEL_ROW_MAPPER = new LabelRowMapper();

    /**
     * Default disabled constructor.
     */
    private LabelRowMapper() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public LabelPO mapRow(ResultSet rs, int rowNum) throws SQLException {

        LabelPO result = new LabelPO();
        result.setId(rs.getInt(LabelPO.Fields.ID));
        result.setName(rs.getString(LabelPO.Fields.NAME));
        result.setDisplayName(rs.getString(LabelPO.Fields.DISPLAY_NAME));
        result.setDescription(rs.getString(LabelPO.Fields.DESCRIPTION));
        result.setCreatedAt(rs.getTimestamp(LabelPO.Fields.CREATED_AT));
        result.setUpdatedAt(rs.getTimestamp(LabelPO.Fields.UPDATED_AT));
        result.setCreatedBy(rs.getString(LabelPO.Fields.CREATED_BY));
        result.setUpdatedBy(rs.getString(LabelPO.Fields.UPDATED_BY));

        return result;
    }

}
