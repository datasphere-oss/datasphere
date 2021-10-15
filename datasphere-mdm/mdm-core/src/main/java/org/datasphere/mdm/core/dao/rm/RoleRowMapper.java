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

import com.huahui.datasphere.mdm.core.po.security.RolePO;

/**
 *  Row mapper for the RolePO object.
 *  @author ilya.bykov
 */
public class RoleRowMapper implements RowMapper<RolePO> {

    /**
     * Default row mapper.
     */
    public static final RoleRowMapper DEFAULT_ROLE_ROW_MAPPER = new RoleRowMapper();

    /**
     * Constructor.
     */
    private RoleRowMapper() {
        super();
    }
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public RolePO mapRow(ResultSet rs, int rowNum) throws SQLException {
		RolePO result = new RolePO();
		result.setId(rs.getInt(RolePO.Fields.ID));
		result.setName(rs.getString(RolePO.Fields.NAME));
		result.setRType(rs.getString(RolePO.Fields.R_TYPE));
		result.setDisplayName(rs.getString(RolePO.Fields.DISPLAY_NAME));
		result.setDescription(rs.getString(RolePO.Fields.DESCRIPTION));
		result.setCreatedAt(rs.getTimestamp(RolePO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(RolePO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(RolePO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(RolePO.Fields.UPDATED_BY));
		return result;
	}

}
