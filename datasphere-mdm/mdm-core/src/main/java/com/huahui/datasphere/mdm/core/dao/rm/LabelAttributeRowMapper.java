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

import com.huahui.datasphere.mdm.core.po.security.LabelAttributePO;
import com.huahui.datasphere.mdm.core.po.security.LabelPO;

/**
 * Row mapper for the LabelPO object.
 * @author ilya.bykov
 */
public class LabelAttributeRowMapper implements RowMapper<LabelAttributePO> {

    /**
     * Mapper singleton.
     */
    public static final LabelAttributeRowMapper DEFAULT_ROW_MAPPER = new LabelAttributeRowMapper();

    /**
     * Constructor.
     */
    private LabelAttributeRowMapper() {
        super();
    }
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public LabelAttributePO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LabelAttributePO result = new LabelAttributePO();
		result.setId(rs.getInt(LabelAttributePO.Fields.ID));
		result.setName(rs.getString(LabelAttributePO.Fields.NAME));
		result.setValue(rs.getString(LabelAttributePO.Fields.VALUE));
		result.setPath(rs.getString(LabelAttributePO.Fields.PATH));
		result.setDescription(rs.getString(LabelAttributePO.Fields.DESCRIPTION));
		result.setCreatedAt(rs.getTimestamp(LabelAttributePO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(LabelAttributePO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(LabelAttributePO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(LabelAttributePO.Fields.UPDATED_BY));
		result.setLabel(new LabelPO(rs.getInt(LabelAttributePO.Fields.S_LABEL_ID)));
		return result;
	}

}
