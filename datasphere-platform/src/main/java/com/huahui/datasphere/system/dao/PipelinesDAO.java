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

package com.huahui.datasphere.system.dao;

import java.util.List;

import com.huahui.datasphere.system.po.PipelinePO;

/**
 * @author Mikhail Mikhailov on Nov 26, 2019
 */
public interface PipelinesDAO {
    /**
     * Loads all pipelines, known to the system.
     * @return collection of pipelines
     */
    List<PipelinePO> loadAll();
    /**
     * Loads specific pipeline by start id and subject id.
     * @param startId the start id
     * @param subjectId the subject id
     * @return pipeline or null if not found
     */
    PipelinePO load(String startId, String subjectId);
    /**
     * Saves this one pipeline 'p'.
     * @param p the pipeline to save
     */
    void save(PipelinePO p);
    /**
     * Deletes a pipeline by requisites.
     * @param startId the start type id
     * @param subject the subject
     */
    void delete(String startId, String subject);
    /**
     * Deletes all pipelines.
     */
    void deleteAll();
}
