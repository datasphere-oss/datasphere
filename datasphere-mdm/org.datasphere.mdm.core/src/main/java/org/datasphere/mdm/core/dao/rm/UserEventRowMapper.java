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

/**
 *
 */
package org.datasphere.mdm.core.dao.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.datasphere.mdm.core.po.security.UserEventPO;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;


/**
 * @author Mikhail Mikhailov
 *
 */
public class UserEventRowMapper implements RowMapper<UserEventPO> {

    /**
     * Default row mapper.
     */
    public static final RowMapper<UserEventPO> DEFAULT_ROW_MAPPER = new UserEventRowMapper();
    /**
     * Default extractor.
     */
    public static final ResultSetExtractor<UserEventPO> DEFAULT_RESULT_SET_EXTRACTOR
        = new ResultSetExtractor<UserEventPO>() {

        @Override
        public UserEventPO extractData(ResultSet rs) throws SQLException {
            return rs != null && rs.next() ? DEFAULT_ROW_MAPPER.mapRow(rs, rs.getRow()) : null;
        }
    };
    /**
     * Constructor.
     */
    private UserEventRowMapper() {
        super();
    }

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public UserEventPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEventPO po = new UserEventPO();

        po.setId(rs.getString(UserEventPO.FIELD_ID));
        po.setUserId(rs.getInt(UserEventPO.FIELD_USER_ID));
        po.setBinaryDataId(rs.getString(UserEventPO.FIELD_BINARY_DATA_ID));
        po.setCharacterDataId(rs.getString(UserEventPO.FIELD_CHARACTER_DATA_ID));
        po.setContent(rs.getString(UserEventPO.FIELD_CONTENT));
        po.setDetails(rs.getString(UserEventPO.FIELD_DETAILS));
        po.setType(rs.getString(UserEventPO.FIELD_TYPE));
        po.setCreateDate(rs.getTimestamp(UserEventPO.FIELD_CREATE_DATE));
        po.setCreatedBy(rs.getString(UserEventPO.FIELD_CREATED_BY));
        po.setUpdateDate(rs.getTimestamp(UserEventPO.FIELD_UPDATE_DATE));
        po.setUpdatedBy(rs.getString(UserEventPO.FIELD_UPDATED_BY));

        return po;
    }

}
