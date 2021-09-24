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
import com.huahui.datasphere.mdm.core.po.security.LabelAttributeValuePO;

/**
 * Row mapper for the LabelPO object.
 * @author ilya.bykov
 */
public class LabelAttributeValueRowMapper implements RowMapper<LabelAttributeValuePO> {

    /**
     * Instance.
     */
    public static final LabelAttributeValueRowMapper DEFAULT_ROW_MAPPER
        = new LabelAttributeValueRowMapper();

    /**
     * Constructor.
     */
    private LabelAttributeValueRowMapper() {
        super();
    }
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public LabelAttributeValuePO mapRow(ResultSet rs, int rowNum) throws SQLException {
		LabelAttributeValuePO result = new LabelAttributeValuePO();
		result.setId(rs.getInt(LabelAttributeValuePO.Fields.ID));
		result.setValue(rs.getString(LabelAttributeValuePO.Fields.VALUE));
		result.setCreatedAt(rs.getTimestamp(LabelAttributeValuePO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(LabelAttributeValuePO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(LabelAttributeValuePO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(LabelAttributeValuePO.Fields.UPDATED_BY));
		result.setGroup(rs.getInt(LabelAttributeValuePO.Fields.S_LABEL_GROUP));
		result.setLabelAttribute(new LabelAttributePO(rs.getInt(LabelAttributeValuePO.Fields.S_LABEL_ATTRIBUTE_ID)));
		return result;
	}

}
