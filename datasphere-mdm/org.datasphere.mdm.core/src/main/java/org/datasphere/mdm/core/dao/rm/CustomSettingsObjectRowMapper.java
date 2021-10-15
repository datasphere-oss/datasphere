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

import org.datasphere.mdm.core.po.CustomStorageRecordPO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Dmitry Kopin
 * Mapper for custom settings object
 */
public class CustomSettingsObjectRowMapper implements RowMapper<CustomStorageRecordPO> {



    /**
     * Default reusable row mapper.
     */
    public static final CustomSettingsObjectRowMapper DEFAULT_ROW_MAPPER = new CustomSettingsObjectRowMapper();

    /**
     * Constructor.
     */
    public CustomSettingsObjectRowMapper() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomStorageRecordPO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomStorageRecordPO result = new CustomStorageRecordPO();
        result.setKey(rs.getString(CustomStorageRecordPO.FIELD_KEY));
        if(Objects.equals(result.getKey(), "")){
            result.setKey(null);
        }
        result.setUser(rs.getString(CustomStorageRecordPO.FIELD_USER_NAME));
        if(Objects.equals(result.getUser(), "")){
            result.setUser(null);
        }
        result.setValue(rs.getString(CustomStorageRecordPO.FIELD_VALUE));
        result.setUpdateDate(rs.getTimestamp(CustomStorageRecordPO.FIELD_UPDATE_DATE));
        return result;
    }

}
