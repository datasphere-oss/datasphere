package com.huahui.datasphere.rest.system.ro.details;

import java.util.List;

/**
 * Error details
 *
 * @author Alexandr Serov
 * @since 27.10.2020
 **/
public class ErrorDetailsRO extends DetailsValueRO {

    private String severity;
    private String externalMessage;
    private String internalMessage;
    private String details;
    private List<String> params;

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getExternalMessage() {
        return externalMessage;
    }

    public void setExternalMessage(String externalMessage) {
        this.externalMessage = externalMessage;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
