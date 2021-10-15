/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.dao.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.datasphere.mdm.core.po.security.ApiPO;
import org.springframework.jdbc.core.RowMapper;

/**
 * The Class APIRowMapper.
 */
public class APIRowMapper implements RowMapper<ApiPO> {

	/** The Constant DEFAULT_API_ROW_MAPPER. */
	public static final APIRowMapper DEFAULT_API_ROW_MAPPER = new APIRowMapper();

	/**
	 * Instantiates a new API row mapper.
	 */
	private APIRowMapper() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public ApiPO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ApiPO result = new ApiPO();
		result.setId(rs.getInt(ApiPO.Fields.ID));
		result.setName(rs.getString(ApiPO.Fields.NAME));
		result.setDisplayName(rs.getString(ApiPO.Fields.DISPLAY_NAME));
		result.setDescription(rs.getString(ApiPO.Fields.DESCRIPTION));
		result.setCreatedAt(rs.getTimestamp(ApiPO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(ApiPO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(ApiPO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(ApiPO.Fields.UPDATED_BY));
		return result;
	}

}
