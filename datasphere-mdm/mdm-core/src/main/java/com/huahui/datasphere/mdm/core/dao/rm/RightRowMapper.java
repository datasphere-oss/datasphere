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

import com.huahui.datasphere.mdm.core.po.security.RightPO;

/**
 * Row mapper for the RightPO class.
 * @author ilya.bykov
 */
public class RightRowMapper implements RowMapper<RightPO> {

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public RightPO mapRow(ResultSet rs, int rowNum) throws SQLException {
		RightPO result = new RightPO();
		result.setId(rs.getInt(RightPO.Fields.ID));
		result.setName(rs.getString(RightPO.Fields.NAME));
		result.setDescription(rs.getString(RightPO.Fields.DESCRIPTION));
		result.setCreatedAt(rs.getTimestamp(RightPO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(RightPO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(RightPO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(RightPO.Fields.UPDATED_BY));
		return result;
	}

}
