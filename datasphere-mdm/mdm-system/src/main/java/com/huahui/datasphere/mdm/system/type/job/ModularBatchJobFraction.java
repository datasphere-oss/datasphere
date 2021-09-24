package com.huahui.datasphere.mdm.system.type.job;

import javax.annotation.Nullable;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import com.huahui.datasphere.mdm.system.type.batch.BatchSetAccumulator;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.PipelineConnection;

/**
 * Basic interface, which allowes optional modules participate on optional batch jobs.
 * Concrete support instances must be marked with annotation {@literal @}JobRef annotation, i. e.
 * {@literal @}JobRef("reindexDataJob").
 * @author theseusyang on Feb 17, 2020
 */
public interface ModularBatchJobFraction {
    /**
     * Run by the job before the step actually runs.
     * @param stepName the step name
     * @param step the step execution
     */
    default void before(String stepName, StepExecution step) {
        // Do nothing by default.
        // Free people from overriding lots of stuff.
    }
    /**
     * Run by the job after the step has run.
     * @param stepName the step name
     * @param step the step execution
     */
    default void after(String stepName, StepExecution step) {
        // Do nothing by default.
        // Free people from overriding lots of stuff.
    }
    /**
     * Returns a connector, which can be attached to batch pipeline.
     * Called by ItemWriter in scope of {@link ItemWriter#write(java.util.List)}
     * once upon bean creation (from {@linkplain InitializingBean#afterPropertiesSet()} in particular).
     * This is done that way, because pipelines are cached for the step lifetime.
     * @param stepName the step name
     * @param step the step execution
     * @return connector or null
     */
    @Nullable
    default PipelineConnection connect(String stepName, StepExecution step) {
        // Do nothing by default.
        // Free people from overriding lots of stuff.
        return null;
    }
    /**
     * Called by ItemWriter in scope of {@link ItemWriter#write(java.util.List)}
     * each time after 'bsa' has been charged with new portion of data.
     * @param stepName the step name
     * @param step the step execution
     * @param bsa the batch set
     */
    default void accumulate(String stepName, StepExecution step, BatchSetAccumulator<?, ?> bsa) {
        // Do nothing by default.
        // Free people from overriding lots of stuff.
    }
    /**
     * If the are several post processors for the same job, the order of execution will be ascending order according to the value,
     * returned from this method.
     * @return
     */
    default int order() {
        return 0;
    }
}
