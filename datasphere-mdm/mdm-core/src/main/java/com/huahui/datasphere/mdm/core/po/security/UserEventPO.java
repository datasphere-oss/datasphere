/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.core.po.security;

import com.huahui.datasphere.mdm.core.po.AbstractObjectPO;


/**
 * @author theseusyang
 * User event objects.
 */
public class UserEventPO extends AbstractObjectPO {

    /**
     * Table name.
     */
    public static final String TABLE_NAME = "user_event";
    /**
     * ID.
     */
    public static final String FIELD_ID = "id";
    /**
     * User ID.
     */
    public static final String FIELD_USER_ID = "user_id";
    /**
     * Binary data id.
     */
    public static final String FIELD_BINARY_DATA_ID = "binary_data_id";
    /**
     * Character data id.
     */
    public static final String FIELD_CHARACTER_DATA_ID = "character_data_id";
    /**
     * Type.
     */
    public static final String FIELD_TYPE = "type";
    /**
     * Content.
     */
    public static final String FIELD_CONTENT = "content";
    /**
     * Details.
     */
    public static final String FIELD_DETAILS = "details";
    /**
     * Status.
     */
    // public static final String FIELD_STATE = "status";
    /**
     * This PO object's fields.
     */
    public static final String[] FIELDS = {
        FIELD_ID,
        FIELD_USER_ID,
        FIELD_BINARY_DATA_ID,
        FIELD_CHARACTER_DATA_ID,
        FIELD_TYPE,
        FIELD_CONTENT,
        FIELD_DETAILS,
        // FIELD_STATE,
        FIELD_CREATE_DATE,
        FIELD_UPDATE_DATE,
        FIELD_CREATED_BY,
        FIELD_UPDATED_BY
    };

    /**
     * Event id.
     */
    private String id;
    /**
     * User id.
     */
    private Integer userId;
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
     * Status of the record.
     */
    // private RecordStatus status;
    /**
     * Constructor.
     */
    public UserEventPO() {
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
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
