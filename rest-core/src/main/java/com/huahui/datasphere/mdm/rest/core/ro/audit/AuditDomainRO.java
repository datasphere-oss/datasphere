package com.huahui.datasphere.mdm.rest.core.ro.audit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author theseusyang on Aug 21, 2020
 */
public class AuditDomainRO {

    private String name;

    private String description;

    private List<AuditMessageTypeRO> types = new ArrayList<>();

    /**
     * Constructor.
     */
    public AuditDomainRO() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the types
     */
    public List<AuditMessageTypeRO> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<AuditMessageTypeRO> types) {
        this.types = types;
    }

}
