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

/**
 * Date: 22.03.2016
 */

package com.huahui.datasphere.mdm.core.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * FIXME: Kill this class brutally!
 *
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
@Deprecated(forRemoval = true)
public class CustomJobDaoSupport extends JdbcDaoSupport {
//    private static final String[] SQL_PATTERN_SYMBOLS = {"%", "_"};

    private final AtomicLong nextId = new AtomicLong(1);

//    private String commonSequenceName;
//    private String createIdsQuery;
    private String createIdQuery;

    private String createTmpIdTableQuery = "create temporary table if not exists t_tmp_id "
            + "( list_id bigint, id bigint, some_text text, some_number bigint ) "
            + "on commit drop";

    private String insertTmpIdQuery = "insert into t_tmp_id (list_id, id) values (?, ?)";

//    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
//    public void postgresBatchUpdate(String sql, BatchPreparedStatementSetter setter) {
//        getJdbcTemplate().batchUpdate(sql, setter);
//    }

    public long listId() {
        return nextId.getAndIncrement();
    }

    // TODO Remove this crap ASAP!
//    @Transactional(propagation = Propagation.MANDATORY)
    public long insertLongsToTemp(final Collection<Long> ids) {
        final long listId = listId();

        getJdbcTemplate().update(createTmpIdTableQuery);

        final Iterator<Long> iter = ids.iterator();

        getJdbcTemplate().batchUpdate(insertTmpIdQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, listId);
                ps.setLong(2, iter.next());
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });

        return listId;
    }



//    @Transactional(propagation = Propagation.SUPPORTS)
//    public long[] createIds(int count, String sequence) {
//        if (count == 0) {
//            return new long[0];
//        }
//
//        final long[] ids = new long[count];
//
//        getJdbcTemplate().query(createIdsQuery,
//                new ResultSetExtractor<Object>() {
//                    @Override
//                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
//                        for (int i = 0; rs.next(); i++) {
//                            ids[i] = rs.getLong(1);
//                        }
//
//                        return null;
//                    }
//                }, sequence, count);
//
//        return ids;
//    }

//    @Transactional(propagation = Propagation.MANDATORY)
    public long createId(String sequence) {
        return getJdbcTemplate().queryForObject(createIdQuery, Long.class, sequence);
    }

    /**
     *
     * @return
     */
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public long[] createIds(int count) {
//        return createIds(count, commonSequenceName);
//    }

    /**
     *
     * @return
     */
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public long createId() {
//        return createId(commonSequenceName);
//    }

//    @Required
//    public void setCreateTmpIdTableQuery(String createTmpIdTableQuery) {
//        this.createTmpIdTableQuery = createTmpIdTableQuery;
//    }
//
//    @Required
//    public void setInsertTmpIdQuery(String insertTmpIdQuery) {
//        this.insertTmpIdQuery = insertTmpIdQuery;
//    }

//    @Required
//    public void setCommonSequenceName(String commonSequenceName) {
//        this.commonSequenceName = commonSequenceName;
//    }

//    public void setCreateIdsQuery(String createIdsQuery) {
//        this.createIdsQuery = createIdsQuery;
//    }
//
//    public void setCreateIdQuery(String createIdQuery) {
//        this.createIdQuery = createIdQuery;
//    }
//
//    @Autowired
//    @Qualifier("coreDataSource")
//    public void setCoreDataSource(DataSource dataSource){
//        setDataSource(dataSource);
//    }
}
