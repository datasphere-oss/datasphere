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

package com.huahui.datasphere.mdm.core.type.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.event.AbstractMulticastEvent;

/**
 * This event is sent when other nodes have to notified that they have to reload model.
 * @author theseusyang on Oct 28, 2019
 */
public class RefreshModelEvent extends AbstractMulticastEvent {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 807674465478762451L;
    /**
     * This type name.
     */
    private static final String TYPE_NAME = "MODEL_REFRESH_EVENT";
    /**
     * The tuples to refresh.
     */
    private final ArrayList<RefreshModelTuple> tuples;
    /**
     * Constructor.
     * @param typeName
     * @param id
     */
    public RefreshModelEvent(Collection<RefreshModelTuple> payload) {
        super(TYPE_NAME);
        tuples = new ArrayList<>(payload);
    }
    /**
     * Gets the payload.
     * @return payload
     */
    public List<RefreshModelTuple> getPayload() {
        return Objects.nonNull(tuples) ? tuples : Collections.emptyList();
    }
    /**
     * @author theseusyang on Oct 29, 2020
     */
    public static final class RefreshModelTuple implements Serializable {
        /**
         * GSVUID.
         */
        private static final long serialVersionUID = 418430367514674068L;
        /**
         * The model type id.
         */
        private final String typeId;
        /**
         * The storage id.
         */
        private final String storageId;
        /**
         * The model instance id.
         */
        private final String instanceId;
        /**
         * Constructor.
         * @param typeId the model type id
         * @param storageId the storage id
         * @param instanceId the model instance id
         */
        public RefreshModelTuple(String typeId, String storageId, String instanceId) {
            super();
            this.typeId = typeId;
            this.storageId = storageId;
            this.instanceId = instanceId;
        }
        /**
         * @return the typeId
         */
        public String getTypeId() {
            return typeId;
        }
        /**
         * @return the storageId
         */
        public String getStorageId() {
            return storageId;
        }
        /**
         * @return the instanceId
         */
        public String getInstanceId() {
            return instanceId;
        }
    }
}
