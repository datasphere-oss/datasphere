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

import org.datasphere.mdm.core.po.security.PasswordPO;
import org.datasphere.mdm.core.po.security.UserPO;
import org.springframework.jdbc.core.RowMapper;

/**
 * Row mapper for the ResourcePO object.
 * @author ilya.bykov
 */
public class PasswordRowMapper implements RowMapper<PasswordPO>{

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public PasswordPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PasswordPO result = new PasswordPO();
        result.setId(rs.getInt(PasswordPO.Fields.ID));
        result.setActive(rs.getBoolean(PasswordPO.Fields.ACTIVE));
        result.setPasswordText(rs.getString(PasswordPO.Fields.PASSWORD_TEXT));
        result.setCreatedAt(rs.getTimestamp(PasswordPO.Fields.CREATED_AT));
        result.setUpdatedAt(rs.getTimestamp(PasswordPO.Fields.UPDATED_AT));
        result.setCreatedBy(rs.getString(PasswordPO.Fields.CREATED_BY));
        result.setUpdatedBy(rs.getString(PasswordPO.Fields.UPDATED_BY));
        return result;
    }

}
