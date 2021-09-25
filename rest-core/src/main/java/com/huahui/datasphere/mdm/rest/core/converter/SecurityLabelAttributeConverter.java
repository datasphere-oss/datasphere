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
package com.huahui.datasphere.mdm.rest.core.converter;

import com.huahui.datasphere.mdm.core.type.security.SecurityLabelAttribute;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecurityLabelAttributeRO;

/**
 * replaced SecurityLabelAttribute conversion method from duplicates
 *
 * @author theseusyang
 * @since 21.05.2019
 */
class SecurityLabelAttributeConverter {

    private SecurityLabelAttributeConverter() {
    }

    /**
     * Convert security label attribute dto.
     *
     * @param source the source
     * @return the security label attribute ro
     */
    static SecurityLabelAttributeRO convertSecurityLabelAttributeDTO(SecurityLabelAttribute source) {
        if (source == null) {
            return null;
        }
        SecurityLabelAttributeRO target = new SecurityLabelAttributeRO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setValue(source.getValue());
        target.setPath(source.getPath());
        return target;
    }
}
