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
 *
 */
package com.huahui.datasphere.mdm.rest.core.converter;

import java.util.List;

import com.huahui.datasphere.mdm.core.dto.UserEventDTO;

import com.huahui.datasphere.mdm.rest.core.ro.UserEventRO;

/**
 * @author theseusyang
 */
public class UserEventConverter {

    /**
     * Constructor.
     */
    private UserEventConverter() {
        super();
    }

    /**
     * To REST from system.
     *
     * @param source system
     * @return REST
     */
    public static UserEventRO to(UserEventDTO source) {
        if (source == null) {
            return null;
        }

        UserEventRO target = new UserEventRO();

        target.setId(source.getId());
        target.setBinaryDataId(source.getBinaryDataId());
        target.setCharacterDataId(source.getCharacterDataId());
        target.setCreateDate(source.getCreateDate());
        target.setCreatedBy(source.getCreatedBy());
        target.setType(source.getType());
        target.setContent(source.getContent());
        target.setDetails(source.getDetails());

        return target;
    }

    /**
     * To REST from system.
     *
     * @param source system
     * @param target REST
     */
    public static void to(List<UserEventDTO> source, List<UserEventRO> target) {
        if (source == null || source.isEmpty()) {
            return;
        }

        for (UserEventDTO d : source) {
            target.add(to(d));
        }
    }
}
