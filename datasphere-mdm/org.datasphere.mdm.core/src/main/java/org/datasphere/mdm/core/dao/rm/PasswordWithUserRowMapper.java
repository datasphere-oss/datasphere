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

public class PasswordWithUserRowMapper extends PasswordRowMapper {
    @Override
    public PasswordPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final PasswordPO passwordPO = super.mapRow(rs, rowNum);
        final UserPO user = new UserPO();
        user.setId(rs.getInt("userId"));
        user.setLogin(rs.getString(UserPO.Fields.LOGIN));
        user.setEmail(rs.getString(UserPO.Fields.EMAIL));
        user.setEmailNotification(rs.getBoolean(UserPO.Fields.EMAIL_NOTIFICATION));
        user.setLocale(rs.getString(UserPO.Fields.LOCALE));
        user.setAdmin(rs.getBoolean(UserPO.Fields.ADMIN));

        passwordPO.setUser(user);
        return passwordPO;
    }
}
