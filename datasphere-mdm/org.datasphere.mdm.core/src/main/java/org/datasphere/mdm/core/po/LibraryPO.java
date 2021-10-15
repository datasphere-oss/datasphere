package org.datasphere.mdm.core.po;

import java.util.Date;

/**
 * @author Mikhail Mikhailov on Jan 25, 2021
 * CF libraries.
 */
public class LibraryPO {
    /**
     * Table name.
     */
    public static final String TABLE_NAME = "libraries";
    /**
     * Storgae ID field.
     */
    public static final String FIELD_STORAGE_ID = "storage_id";
    /**
     * Version.
     */
    public static final String FIELD_VERSION = "version";
    /**
     * Revision.
     */
    public static final String FIELD_NAME = "name";
    /**
     * General description.
     */
    public static final String FIELD_DESCRIPTION = "description";
    /**
     * MIME type.
     */
    public static final String FIELD_MIME_TYPE = "mime_type";
    /**
     * Editable flag.
     */
    public static final String FIELD_EDITABLE = "editable";
    /**
     * Data.
     */
    public static final String FIELD_CONTENT = "content";
    /**
     * Size.
     */
    public static final String FIELD_SIZE = "content_size";
    /**
     * Create date.
     */
    public static final String FIELD_CREATE_DATE = "create_date";
    /**
     * Created by.
     */
    public static final String FIELD_CREATED_BY = "created_by";
    /**
     * The storage id.
     */
    private String storageId;
    /**
     * Revision (is also the table PK).
     * Assigned manually causing CV for duplicates.
     */
    private String version;
    /**
     * Name of the library, part of the PK.
     */
    private String name;
    /**
     * General description.
     */
    private String description;
    /**
     * MIME type.
     */
    private String mimeType;
    /**
     * The editable flag.
     */
    private boolean editable;
    /**
     * Data.
     */
    private byte[] content;
    /**
     * Payload size.
     */
    private long size;
    /**
     * Creator's user name.
     */
    private String createdBy;
    /**
     * Create date.
     */
    private Date createDate;
    /**
     * Constructor.
     */
    public LibraryPO() {
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
     * @return the operationId
     */
    public String getMimeType() {
        return mimeType;
    }
    /**
     * @param operationId the operationId to set
     */
    public void setMimeType(String operationId) {
        this.mimeType = operationId;
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
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(byte[] data) {
        this.content = data;
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
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
}
