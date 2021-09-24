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

package com.huahui.datasphere.mdm.core.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.mdm.core.context.AuditEventWriteContext;

/**
 * @author theseusyang
 */
public interface AuditStorageService {
    /**
     * Known audit storage types.
     * @author theseusyang on Apr 25, 2020
     */
    enum AuditStorageType {
        /**
         * Database.
         */
        DATABASE("db"),
        /**
         * Indexing.
         */
        INDEX("es");
        /**
         * Constructor.
         * @param value the value
         */
        private AuditStorageType(String value) {
            this.value = value;
        }
        /**
         * The value.
         */
        private final String value;
        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        public static AuditStorageType[] fromValues(String... vals) {

            final AuditStorageType[] result = new AuditStorageType[AuditStorageType.values().length];
            for (int i = 0; ArrayUtils.isNotEmpty(vals) && i < vals.length; i++) {

                AuditStorageType hit = null;
                for (int j = 0; Objects.nonNull(vals[i]) && j < AuditStorageType.values().length; j++) {

                    if (AuditStorageType.values()[j].getValue().equals(vals[i])) {
                        hit = AuditStorageType.values()[j];
                        break;
                    }
                }

                if (Objects.nonNull(hit)) {
                    result[hit.ordinal()] = hit;
                }
            }

            return result;
        }

        public static String[] toValues(AuditStorageType[] types) {

            if (ArrayUtils.isEmpty(types)) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }

            return Arrays.stream(types)
                    .filter(Objects::nonNull)
                    .map(AuditStorageType::getValue)
                    .toArray(String[]::new);
        }
    }
    /**
     * Write event to the index.
     * @param auditEventWriteContext the event
     */
    void write(Collection<AuditEventWriteContext> auditEventWriteContext);
    /**
     * Prepares storage for usage.
     */
    void prepare();
}
