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

import org.datasphere.mdm.core.po.security.RolePO;
import org.springframework.jdbc.core.RowMapper;

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
