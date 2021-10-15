package org.datasphere.mdm.core.po.job;

/**
 * @author Mikhail Mikhailov on Jul 6, 2021
 */
public class ExecutionStatePO {
    /**
     * Last execution status. Used _only_ in queries.
     */
    public static final String FIELD_STATUS = "status";
    /**
     * Last execution status. Used _only_ in queries.
     */
    public static final String FIELD_EXIT_CODE = "exit_code";
    /**
     * Last execution status. Used _only_ in queries.
     */
    public static final String FIELD_EXIT_MESSAGE = "exit_message";
    /**
     * The satus
     */
    private String status;
    /**
     * The code.
     */
    private String exitCode;
    /**
     * The message.
     */
    private String exitMessage;
    /**
     * Constructor.
     */
    public ExecutionStatePO() {
        super();
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @return the exitCode
     */
    public String getExitCode() {
        return exitCode;
    }
    /**
     * @param exitCode the exitCode to set
     */
    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }
    /**
     * @return the exitMessage
     */
    public String getExitMessage() {
        return exitMessage;
    }
    /**
     * @param exitMessage the exitMessage to set
     */
    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }

}
