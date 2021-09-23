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

package com.huahui.datasphere.system.dao.impl;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.huahui.datasphere.system.dao.PipelinesDAO;
import com.huahui.datasphere.system.po.PipelinePO;
/**
 * Pipelines state DAO.
 * @author Mikhail Mikhailov on Nov 26, 2019
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
