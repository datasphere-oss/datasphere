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

package com.huahui.datasphere.mdm.core.dao.template;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;

import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;

/**
 * @author theseusyang
 * The template descriptor.
 */
public interface QueryTemplateDescriptor {
    /**
     * The template's code.
     * @return code
     */
    String getCode();
    /**
     * Tells the caller, whether this decriptor denotes a distributed query or not
     * @return true for distributed queries, false otherwise
     */
    boolean isDistributed();

    static<T extends QueryTemplateDescriptor> QueryTemplates toTemplates(T[] values, Map<T, QueryTemplate> map, Properties p) {

        for (int i = 0; i < values.length; i++) {

            T drq = values[i];
            String source = p.getProperty(drq.getCode());
            if (Objects.isNull(source)) {
                throw new PlatformFailureException(
                        "No record query template found for given descriptor [{}]",
                        CoreExceptionIds.EX_DATA_STORAGE_NO_QUERY_TEMPLATE_FOR_DECSRIPTOR,
                        drq.getCode());
            }

            map.put(drq, new QueryTemplate(source));
        }

        return new QueryTemplates(map);
    }
}
