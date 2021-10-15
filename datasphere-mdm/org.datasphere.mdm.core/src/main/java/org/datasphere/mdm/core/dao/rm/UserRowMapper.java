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

import org.datasphere.mdm.core.po.security.UserPO;
import org.springframework.jdbc.core.RowMapper;

/**
 * Row mapper for the UserPO object.
 * @author ilya.bykov
 */
public class UserRowMapper implements RowMapper<UserPO> {

    /**
     * Default row mapper instance.
     */
    public static final UserRowMapper DEFAULT_USER_ROW_MAPPER
        = new UserRowMapper();

    /**
     * Constructor.
     */
    private UserRowMapper() {
        super();
    }

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public UserPO mapRow(ResultSet rs, int rowNum) throws SQLException {

	    if(rs == null){
			return null;
		}

		UserPO result = new UserPO();
		result.setId(rs.getInt(UserPO.Fields.ID));
		result.setLogin(rs.getString(UserPO.Fields.LOGIN));
		result.setEmail(rs.getString(UserPO.Fields.EMAIL));
		result.setLocale(rs.getString(UserPO.Fields.LOCALE));
		result.setFirstName(rs.getString(UserPO.Fields.FIRST_NAME));
		result.setLastName(rs.getString(UserPO.Fields.LAST_NAME));
		result.setNotes(rs.getString(UserPO.Fields.NOTES));
		result.setCreatedAt(rs.getTimestamp(UserPO.Fields.CREATED_AT));
		result.setUpdatedAt(rs.getTimestamp(UserPO.Fields.UPDATED_AT));
		result.setCreatedBy(rs.getString(UserPO.Fields.CREATED_BY));
		result.setUpdatedBy(rs.getString(UserPO.Fields.UPDATED_BY));
		result.setActive(rs.getBoolean(UserPO.Fields.ACTIVE));
		result.setAdmin(rs.getBoolean(UserPO.Fields.ADMIN));
		result.setSource(rs.getString(UserPO.Fields.SOURCE));
        result.setExternal(rs.getBoolean(UserPO.Fields.EXTERNAL));
		result.setEmailNotification(rs.getBoolean(UserPO.Fields.EMAIL_NOTIFICATION));
		return result;
	}

}
