/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

/**
 *
 */
package org.datasphere.mdm.core.po.lob;

import java.io.InputStream;
import java.util.UUID;

import org.datasphere.mdm.core.po.AbstractObjectPO;
import org.datasphere.mdm.core.type.lob.LargeObjectAcceptance;

/**
 * @author Mikhail Mikhailov
 * Large objects PO.
 */
public abstract class LargeObjectPO extends AbstractObjectPO {
    /**
     * ID.
     */
    public static final String FIELD_ID = "id";
    /**
     * Data.
     */
    public static final String FIELD_DATA = "data";
    /**
     * File name.
     */
    public static final String FIELD_FILE_NAME = "filename";
    /**
     * Mime type.
     */
    public static final String FIELD_MIME_TYPE = "mime_type";
    /**
     * Size.
     */
    public static final String FIELD_SIZE = "size";
    /**
     * Record origin ID, event ID, clsf origin ID or similar.
     */
    public static final String FIELD_SUBJECT = "subject";
    /**
     * Accepatnce state.
     */
    public static final String FIELD_STATE = "state";
    /**
     * Field name / path, similar.
     */
    public static final String FIELD_TAGS = "tags";
    /**
     * Record id.
     */
    private UUID id;
    /**
     * IN stream.
     */
    private InputStream data;
    /**
     * Marshaled JAXB object.
     */
    private String fileName;
    /**
     * Marshaled JAXB object.
     */
    private String mimeType;
    /**
     * Marshaled JAXB object.
     */
    private String subject;
    /**
     * Marshaled JAXB object.
     */
    private String[] tags;
    /**
     * Size of the object.
     */
    private long size;
    /**
     * LargeObjectAcceptance status.
     */
    private LargeObjectAcceptance state;
    /**
     * Binary data mark.
     * @return true, if binary
     */
    public abstract boolean isBinary();
    /**
     * Character data mark.
     * @return true, if character
     */
    public abstract boolean isCharacter();
    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subjectId) {
        this.subject = subjectId;
    }
    /**
     * @return the tags
     */
    public String[] getTags() {
        return tags;
    }
    /**
     * @param tags the tags to set
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }
    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    /**
     * @return the data
     */
    public InputStream getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(InputStream inputStream) {
        this.data = inputStream;
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
     * @return the state
     */
    public LargeObjectAcceptance getState() {
        return state;
    }
    /**
     * @param state the state to set
     */
    public void setState(LargeObjectAcceptance state) {
        this.state = state;
    }
}
