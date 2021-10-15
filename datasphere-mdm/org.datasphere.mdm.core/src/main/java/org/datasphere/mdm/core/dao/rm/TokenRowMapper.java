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

import org.datasphere.mdm.core.po.security.BaseTokenPO;
import org.datasphere.mdm.core.po.security.TokenPO;
import org.springframework.jdbc.core.RowMapper;

public class TokenRowMapper implements RowMapper<BaseTokenPO> {

	@Override
	public BaseTokenPO mapRow(ResultSet rs, int rowNum) throws SQLException {
		TokenPO result = new TokenPO();
		result.setId(rs.getInt(TokenPO.Fields.ID));
		result.setToken(rs.getString(TokenPO.Fields.TOKEN));
		result.setCreatedAt(rs.getTimestamp(TokenPO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(TokenPO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(TokenPO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(TokenPO.Fields.UPDATED_BY));
		return result;
	}

}
