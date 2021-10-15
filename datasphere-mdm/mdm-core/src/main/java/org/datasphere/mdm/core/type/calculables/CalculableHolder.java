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

package com.huahui.datasphere.mdm.core.type.calculables;

import java.util.Date;

import com.huahui.datasphere.mdm.core.type.data.RecordStatus;
import com.huahui.datasphere.mdm.core.type.keys.OriginKey;

/**
 * @author theseusyang
 * Holder for calculation objects.
 */
public interface CalculableHolder<T> extends ModificationBoxKey {
    /**
     * @return the relation
     */
    T getValue();
    /**
     * @return the name
     */
    String getTypeName();
    /**
     * @return the sourceSystem
     */
    String getSourceSystem();
    /**
     * @return the external id (if present)
     */
    String getExternalId();
    /**
     * @return the status
     */
    RecordStatus getStatus();

    /**
     * @return the last update date
     */
    Date getLastUpdate();
    /**
     * Gets the revision of the object hold, if applicable.
     * @return revision (&gt; 0), -1 if not applicable or 0 for new objects
     */
    int getRevision();
    /**
     * Validity period from.
     * @return from
     */
    Date getValidFrom();
    /**
     * Validity period to.
     * @return to
     */
    Date getValidTo();
    /**
     * Tells whether this calculable is an enrichment.
     * @return true, if so, false otherwise
     */
    boolean isEnrichment();
    /**
     * Returns true, if this calculable as a modification to timeline (has revision 0).
     * @return true, if this calculable as a modification to timeline (has revision 0), false otherwise
     */
    default boolean isModification() {
        return getRevision() == 0;
    }
    /**
     * Gets the origin key of the value.
     * This might be useful in situation with lowered types visibility.
     * Returns null for attributes.
     * @return origin key
     */
    OriginKey getOriginKey();
}
