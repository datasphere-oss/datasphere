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

package com.huahui.datasphere.mdm.system.dao.impl;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.huahui.datasphere.mdm.system.dao.PipelinesDAO;
import com.huahui.datasphere.mdm.system.po.PipelinePO;
/**
 * Pipelines state DAO.
 */
@Repository
public class PipelinesDAOImpl extends BaseDAOImpl implements PipelinesDAO {
    /**
     * Loads all pipelines.
     */
    private final String loadPipelinesSQL;
    /**
     * Loads a specific pipeline.
     */
    private final String loadPipelineSQL;
    /**
     * Saves a pipeline.
     */
    private final String savePipelineSQL;
    /**
     * Deletes a pipeline.
     */
    private final String deletePipelineSQL;
    /**
     * Deletes all exisitng pipelines.
     */
    private final String deleteAllPipelinesSQL;
    /**
     * Constructor.
     * @param dataSource
     */
    @Autowired
    public PipelinesDAOImpl(
            @Qualifier("systemDataSource") final DataSource dataSource,
            @Qualifier("pipelines-sql") final Properties sql) {
        super(dataSource);
        loadPipelinesSQL = sql.getProperty("loadPipelinesSQL");
        loadPipelineSQL = sql.getProperty("loadPipelineSQL");
        savePipelineSQL = sql.getProperty("savePipelineSQL");
        deletePipelineSQL = sql.getProperty("deletePipelineSQL");
        deleteAllPipelinesSQL = sql.getProperty("deleteAllPipelinesSQL");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<PipelinePO> loadAll() {
        return jdbcTemplate.query(loadPipelinesSQL, (rs, n) -> {

            PipelinePO result = new PipelinePO();
            result.setStartId(rs.getString(PipelinePO.FIELD_START_ID));
            result.setSubject(rs.getString(PipelinePO.FIELD_SUBJECT));
            result.setContent(rs.getString(PipelinePO.FIELD_CONTENT));
            return result;
        });
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public PipelinePO load(String startId, String subjectId) {
        return jdbcTemplate.query(loadPipelineSQL, rs -> {

            if (!rs.next()) {
                return null;
            }

            PipelinePO result = new PipelinePO();
            result.setStartId(rs.getString(PipelinePO.FIELD_START_ID));
            result.setSubject(rs.getString(PipelinePO.FIELD_SUBJECT));
            result.setContent(rs.getString(PipelinePO.FIELD_CONTENT));
            return result;

        }, startId, subjectId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PipelinePO p) {
        jdbcTemplate.update(savePipelineSQL, p.getStartId(), p.getSubject(), p.getContent());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String startId, String subject) {
        jdbcTemplate.update(deletePipelineSQL, startId, subject);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        jdbcTemplate.update(deleteAllPipelinesSQL);
    }
}
