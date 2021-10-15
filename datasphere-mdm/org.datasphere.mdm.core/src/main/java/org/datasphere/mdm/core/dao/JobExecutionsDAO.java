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
package org.datasphere.mdm.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.datasphere.mdm.core.po.job.ExecutionStatePO;
import org.datasphere.mdm.core.po.job.JobBatchJobInstancePO;
import org.datasphere.mdm.core.po.job.JobSysJobInstancePO;
import org.datasphere.mdm.system.dao.BaseDAO;

/**
 * @author Mikhail Mikhailov on Jul 5, 2021
 * Stuff, related to executions.
 */
public interface JobExecutionsDAO extends BaseDAO {
    /**
     * Finds last execution IDs for the given definition IDs.
     * @param jobDefinitionIds the definition IDs
     * @return map
     */
    Map<Long, Long> findLastInstanceIds(List<Long> jobDefinitionIds);
    /**
     * Finds last execution IDs for the given job name (system job).
     * @param jobName the job name
     * @return id or null
     */
    Long findLastInstanceId(String jobName);
    /**
     * Finds all execution IDs for the given definition IDs
     * (really in doubts, whether such a bad query can continue to exist).
     * @param jobDefinitionIds the definition ids
     * @return map
     */
    Map<Long, List<Long>> findAllInstanceIds(List<Long> jobDefinitionIds);
    /**
     * Finds all execution IDs for the given job name.
     * (really in doubts, whether such a bad query can continue to exist).
     * @param jobName the job name (system job)
     * @return list
     */
    List<Long> findAllInstanceIds(String jobName);
    /**
     * Loads state of the last finished execution of a definition with the given id.
     * @param jobDefibitionId the definition id
     * @return state
     */
    @Nullable
    ExecutionStatePO findLastFinishedState(long jobDefibitionId);
    /**
     * Saves instance to execution log.
     * @param po the record to save
     */
    void save(JobBatchJobInstancePO po);
    /**
     * Saves instance to execution log.
     * @param po the record to save
     */
    void save(JobSysJobInstancePO po);
}
