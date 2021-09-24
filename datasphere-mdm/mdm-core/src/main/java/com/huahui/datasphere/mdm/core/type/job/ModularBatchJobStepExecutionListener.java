package com.huahui.datasphere.mdm.core.type.job;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author theseusyang on Feb 17, 2020
 */
public class ModularBatchJobStepExecutionListener implements StepExecutionListener {
    /**
     * Step name.
     */
    protected String stepName;
    /**
     * Support.
     */
    @Autowired
    protected ModularBatchJobSupport support;
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
     * {@inheritDoc}
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        support.forEach(f -> f.before(stepName, stepExecution));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        support.forEach(f -> f.after(stepName, stepExecution));
        return stepExecution.getExitStatus();
    }
}
