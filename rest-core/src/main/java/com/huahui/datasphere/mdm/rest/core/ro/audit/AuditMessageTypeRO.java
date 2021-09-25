package com.huahui.datasphere.mdm.rest.core.ro.audit;

import java.util.ArrayList;
import java.util.List;

public class AuditMessageTypeRO {
    /**
     * This type id.
     */
    private String id;
    /**
     * This type description.
     */
    private String description;

    private List<AuditMessageHeaderRO> headers = new ArrayList<>();

    public AuditMessageTypeRO() {
        super();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the headers
     */
    public List<AuditMessageHeaderRO> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(List<AuditMessageHeaderRO> headers) {
        this.headers = headers;
    }

}
