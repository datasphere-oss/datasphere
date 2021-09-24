

package com.huahui.datasphere.mdm.system.dao;

import java.util.List;

import com.huahui.datasphere.mdm.system.po.PipelinePO;

/**
 * @author theseusyang on Nov 26, 2019
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
