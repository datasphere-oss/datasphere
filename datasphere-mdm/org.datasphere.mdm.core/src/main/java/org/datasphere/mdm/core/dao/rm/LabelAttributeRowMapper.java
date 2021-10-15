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

import org.datasphere.mdm.core.po.security.LabelAttributePO;
import org.datasphere.mdm.core.po.security.LabelPO;
import org.springframework.jdbc.core.RowMapper;

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
