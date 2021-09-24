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

package com.huahui.datasphere.mdm.core.dto;

import java.io.Serializable;

import com.huahui.datasphere.mdm.core.type.security.Right;

/**
 * @author theseusyang
 * Resource specific rights (extended by merge and resore).
 */
public class ResourceSpecificRightDTO extends RightDTO implements Serializable {

    /** Virtual "restore" right */
    private boolean restore;

    /** Virtual "merge" right */
    private boolean merge;

    /**
     * SVUID.
     */
    private static final long serialVersionUID = -7386646577993018469L;

    /**
     * Constructor.
     */
    public ResourceSpecificRightDTO() {
        super();
    }

    /**
     * Constructor.
     * @param other
     */
    public ResourceSpecificRightDTO(Right other) {
        super(other);
    }

    /**
     * @return the restore
     */
    public boolean isRestore() {
        return restore;
    }

    /**
     * @param restore the restore to set
     */
    public void setRestore(boolean restore) {
        this.restore = restore;
    }

    /**
     * @return the merge
     */
    public boolean isMerge() {
        return merge;
    }

    /**
     * @param merge the merge to set
     */
    public void setMerge(boolean merge) {
        this.merge = merge;
    }
}
