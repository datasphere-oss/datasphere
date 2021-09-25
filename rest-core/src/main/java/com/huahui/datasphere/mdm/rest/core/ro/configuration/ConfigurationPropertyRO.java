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
package com.huahui.datasphere.mdm.rest.core.ro.configuration;

public class ConfigurationPropertyRO {
    private String name;
    private String displayName;
    private String group;
    private String groupCode;
    private String type;
    private Object value;
    private ConfigurationPropertyMetaRO meta;

    public ConfigurationPropertyRO() {
    }

    public ConfigurationPropertyRO(
            final String name,
            final String displayName,
            final String groupCode,
            final String group,
            final String type,
            final Object value,
            final ConfigurationPropertyMetaRO meta
    ) {
        this.name = name;
        this.displayName = displayName;
        this.groupCode = groupCode;
        this.group = group;
        this.type = type;
        this.value = value;
        this.meta = meta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ConfigurationPropertyMetaRO getMeta() {
        return meta;
    }

    public void setMeta(ConfigurationPropertyMetaRO meta) {
        this.meta = meta;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
