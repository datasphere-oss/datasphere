package com.huahui.datasphere.rest.system.ro;

import com.huahui.datasphere.rest.system.ro.details.ResultDetailsRO;

/**
 * The object describing the exception.
 * Must be passed as the body of the error response
 *
 * @author Alexandr Serov
 * @since 02.11.2020
 **/
public class DetailedErrorResponseRO extends DetailedOutputRO {

    /**
     * Error code
     */
    private String errorMessage;

    public DetailedErrorResponseRO() {
        // default
    }

    public DetailedErrorResponseRO(String errorMessage, ResultDetailsRO details) {
        this.errorMessage = errorMessage;
        this.details = details;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
