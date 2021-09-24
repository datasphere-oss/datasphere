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

/**
 * Date: 05.07.2016
 */

package com.huahui.datasphere.mdm.core.po.security;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class RolePropertyValuePO extends BaseSecurityPO {
    private Long id;
    private Long roleId;
    private RolePropertyPO property;
    private String value;

    public RolePropertyValuePO() {
    }

    public RolePropertyValuePO(Long roleId, RolePropertyPO property) {
        this.roleId = roleId;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public RolePropertyPO getProperty() {
        return property;
    }

    public void setProperty(RolePropertyPO property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum FieldColumns {
        ID,
        ROLE_ID,
        PROPERTY_ID,
        VALUE
    }
}
