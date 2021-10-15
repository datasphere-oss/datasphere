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

import com.huahui.datasphere.mdm.core.po.AbstractObjectPO;

/**
 * @author theseusyang
 * Abstract PO mapper.
 */
public abstract class AbstractRowMapper<T extends AbstractObjectPO> implements RowMapper<T> {

    /**
     * Constructor.
     */
    public AbstractRowMapper() {
        super();
    }

    /**
     * Maps common rows.
     * @param t object
     * @param rs result set
     * @param rowNum row number
     * @throws SQLException if something went wrong
     */
    protected void mapRow(T t, ResultSet rs, int rowNum) throws SQLException {
        t.setCreateDate(rs.getTimestamp(AbstractObjectPO.FIELD_CREATE_DATE));
        t.setCreatedBy(rs.getString(AbstractObjectPO.FIELD_CREATED_BY));
        t.setUpdateDate(rs.getTimestamp(AbstractObjectPO.FIELD_UPDATE_DATE));
        t.setUpdatedBy(rs.getString(AbstractObjectPO.FIELD_UPDATED_BY));
    }
}
