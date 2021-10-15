package org.datasphere.mdm.core.dto;

import java.time.OffsetDateTime;

import org.datasphere.mdm.core.type.libraries.LibraryMimeType;

/**
 * @author Mikhail Mikhailov on Jan 29, 2021
 * User library short info.
 */
public class UserLibraryResult {
    /**
     * The storage id.
     */
    private String storageId;
    /**
     * File name.
     */
    private String filename;
    /**
     * Library's version
     */
    private String version;
    /**
     * The description.
     */
    private String description;
    /**
     * MIME type.
     */
    private LibraryMimeType mimeType;
    /**
     * The 'editable' flag.
     */
    private boolean editable;
    /**
     * Size in bytes.
     */
    private long size;
    /**
     * The payload.
     */
    private byte[] payload;
    /**
     * Originator of the record.
     */
    private String createdBy;
    /**
     * Create date.
     */
    private OffsetDateTime createDate;
    /**
     * Constructor.
     */
    public UserLibraryResult() {
        super();
    }
    /**
     * @return the storageId
     */
    public String getStorageId() {
        return storageId;
    }
    /**
     * @param storageId the storageId to set
     */
    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    /**
     * @return the mimeType
     */
    public LibraryMimeType getMimeType() {
        return mimeType;
    }
    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(LibraryMimeType mimeType) {
        this.mimeType = mimeType;
    }
    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }
    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
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
     * @return the size
     */
    public long getSize() {
        return size;
    }
    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }
    /**
     * @return the payload
     */
    public byte[] getPayload() {
        return payload;
    }
    /**
     * @param payload the payload to set
     */
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
    /**
     * @return the createdBy
     */
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
     * @return the createDate
     */
    public OffsetDateTime getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }
}
