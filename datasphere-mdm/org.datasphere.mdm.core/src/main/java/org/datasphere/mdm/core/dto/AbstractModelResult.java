package org.datasphere.mdm.core.dto;

import org.datasphere.mdm.system.dto.AbstractCompositeResult;

/**
 * @author Mikhail Mikhailov on Dec 18, 2020
 */
public abstract class AbstractModelResult extends AbstractCompositeResult implements ModelGetResult {
    /**
     * The name.
     */
    private String name;
    /**
     * The display name.
     */
    private String displayName;
    /**
     * The description.
     */
    private String description;
    /**
     * Read-only version.
     */
    private int version;
    /**
     * Constructor.
     */
    protected AbstractModelResult() {
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
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }
}
