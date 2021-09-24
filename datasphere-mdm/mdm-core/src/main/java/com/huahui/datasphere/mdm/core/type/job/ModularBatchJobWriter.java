package com.huahui.datasphere.mdm.core.type.job;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

/**
 * @author theseusyang on Feb 17, 2020
 */
public abstract class ModularBatchJobWriter<T> implements ItemWriter<T> {
    /**
     * The step name.
     */
    protected String stepName;
    /**
     * The step execution.
     */
    protected StepExecution stepExecution;

    /**
     * @return the stepName
     */
    public String getStepName() {
        return stepName;
    }
    /**
     * @param stepName the stepName to set
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    /**
     * @return the stepExecution
     */
    public StepExecution getStepExecution() {
        return stepExecution;
    }
    /**
     * Sets the step execution.
     * @param stepExecution
     */
    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
