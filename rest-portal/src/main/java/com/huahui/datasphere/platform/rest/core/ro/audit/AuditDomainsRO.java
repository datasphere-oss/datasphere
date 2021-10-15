package com.huahui.datasphere.platform.rest.core.ro.audit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Mikhailov on Aug 21, 2020
 */
public class AuditDomainsRO {

    private List<AuditDomainRO> domains = new ArrayList<>();

    private List<AuditMessageHeaderRO> systemHeaders = new ArrayList<>();

    /**
     * Constructor.
     */
    public AuditDomainsRO() {
        super();
    }

    /**
     * @return the domains
     */
    public List<AuditDomainRO> getDomains() {
        return domains;
    }

    /**
     * @param domains the domains to set
     */
    public void setDomains(List<AuditDomainRO> domains) {
        this.domains = domains;
    }

    /**
     * @return the systemHeaders
     */
    public List<AuditMessageHeaderRO> getSystemHeaders() {
        return systemHeaders;
    }

    /**
     * @param systemHeaders the systemHeaders to set
     */
    public void setSystemHeaders(List<AuditMessageHeaderRO> systemHeaders) {
        this.systemHeaders = systemHeaders;
    }

}
