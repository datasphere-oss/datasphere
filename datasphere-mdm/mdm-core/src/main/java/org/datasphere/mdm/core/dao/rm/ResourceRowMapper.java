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

import com.huahui.datasphere.mdm.core.po.security.ResourcePO;

/**
 * Row mapper for the ResourcePO object.
 * @author ilya.bykov
 */
public class ResourceRowMapper implements RowMapper<ResourcePO>{

    /**
     * Default row mapper.
     */
    public static final ResourceRowMapper DEFAULT_ROW_MAPPER
        = new ResourceRowMapper();

    /**
     * Constructor.
     */
    private ResourceRowMapper() {
        super();
    }

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public ResourcePO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResourcePO result = new ResourcePO();
		result.setId(rs.getInt(ResourcePO.Fields.ID));
		result.setParentId(rs.getInt(ResourcePO.Fields.PARENT_ID) == 0 ? null : rs.getInt(ResourcePO.Fields.PARENT_ID));
		result.setName(rs.getString(ResourcePO.Fields.NAME));
		result.setRType(rs.getString(ResourcePO.Fields.R_TYPE));
		result.setCategory(rs.getString(ResourcePO.Fields.CATEGORY));
		result.setDisplayName(rs.getString(ResourcePO.Fields.DISPLAY_NAME));
		result.setCreatedAt(rs.getTimestamp(ResourcePO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(ResourcePO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(ResourcePO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(ResourcePO.Fields.UPDATED_BY));
		return result;
	}

}
