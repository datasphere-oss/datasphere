package org.datasphere.mdm.core.dto.job;

import org.datasphere.mdm.core.type.job.JobExecution;

/**
 * @author Mikhail Mikhailov on Jul 8, 2021
 * Single execution state.
 */
public class JobExecutionStatusResult {
    /**
     * The execution in current state.
     */
    private JobExecution execution;
    /**
     * Progress gauge.
     */
    private double progress;
    /**
     * Constructor.
     */
    public JobExecutionStatusResult() {
        super();
    }
    /**
     * Constructor.
     */
    public JobExecutionStatusResult(JobExecution execution, double progress) {
        super();
        this.execution = execution;
        this.progress = progress;
    }
    /**
     * @return the execution
     */
    public JobExecution getExecution() {
        return execution;
    }
    /**
     * @param execution the execution to set
     */
    public void setExecution(JobExecution execution) {
        this.execution = execution;
    }
    /**
     * @return the progress
     */
    public double getProgress() {
        return progress;
    }
    /**
     * @param progress the progress to set
     */
    public void setProgress(double progress) {
        this.progress = progress;
    }

}
