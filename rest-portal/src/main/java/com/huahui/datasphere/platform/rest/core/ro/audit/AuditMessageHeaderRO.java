package com.huahui.datasphere.platform.rest.core.ro.audit;

public class AuditMessageHeaderRO {
    /**
     * The header name.
     */
    private String name;
    /**
     * The header type.
     */
    private String type;

    public AuditMessageHeaderRO() {
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
