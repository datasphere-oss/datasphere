/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
/**
 *
 */
package com.huahui.datasphere.platform.rest.core.ro;

import java.util.Date;

/**
 * @author Mikhail Mikhailov
 *
 */
public class UserEventRO {

    /**
     * Event id.
     */
    private String id;
    /**
     * Create date.
     */
    private Date createDate;
    /**
     * Created by.
     */
    private String createdBy;
    /**
     * Binary data ID. Read only.
     */
    private String binaryDataId;
    /**
     * Binary data ID. Read only.
     */
    private String characterDataId;
    /**
     * Type.
     */
    private String type;
    /**
     * Content.
     */
    private String content;
    /**
     * Details.
     */
    private String details;
    /**
     * Constructor.
     */
    public UserEventRO() {
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


    /**
     * @return the binaryDataId
     */
    public String getBinaryDataId() {
        return binaryDataId;
    }


    /**
     * @param binaryDataId the binaryDataId to set
     */
    public void setBinaryDataId(String binaryDataId) {
        this.binaryDataId = binaryDataId;
    }


    /**
     * @return the characterDataId
     */
    public String getCharacterDataId() {
        return characterDataId;
    }


    /**
     * @param characterDataId the characterDataId to set
     */
    public void setCharacterDataId(String characterDataId) {
        this.characterDataId = characterDataId;
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


    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }


    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }


    /**
     * @param details of content
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
