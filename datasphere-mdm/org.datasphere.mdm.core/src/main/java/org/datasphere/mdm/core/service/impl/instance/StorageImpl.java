package org.datasphere.mdm.core.service.impl.instance;

import java.time.Instant;

import org.datasphere.mdm.core.type.model.StorageElement;

/**
 * @author Mikhail Mikhailov on Oct 5, 2020
 */
public class StorageImpl implements StorageElement {
    /**
     * ID.
     */
    private String id;
    /**
     * Storage id.
     */
    private String description;
    /**
     * Create time stamp.
     */
    private Instant createDate;
    /**
     * Update time stamp.
     */
    private Instant updateDate;
    /**
     * Created by.
     */
    private String createdBy;
    /**
     * Updated by.
     */
    private String updatedBy;
    /**
     * Constructor.
     */
    public StorageImpl() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStorageId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setStorageId(String id) {
        this.id = id;
    }
    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getUpdateDate() {
        return updateDate;
    }
    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
