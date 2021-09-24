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

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.Map.Entry;

import com.hazelcast.core.ReadOnly;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.huahui.datasphere.mdm.core.type.security.SecurityToken;

/**
 * @author theseusyang
 * Bogus entry processor, dedicated to timestamp renewal after near cache reads.
 */
public class TokenTimestampRefresher implements EntryProcessor<String, SecurityToken>, ReadOnly {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 4779553343762697067L;
    /**
     * Bogus backup processor, to keep backups in sync with master partition.
     */
    private final EntryBackupProcessor<String, SecurityToken> backupProcessor =
            new EntryBackupProcessor<String, SecurityToken>() {
                /**
                 * SVUID.
                 */
                private static final long serialVersionUID = 1081818211998591708L;
                /**
                 * {@inheritDoc}
                 */
                @Override
                public void processBackup(Entry<String, SecurityToken> entry) {
                    // NOP
                }
            };
    /**
     * {@inheritDoc}
     */
    @Override
    public Object process(Entry<String, SecurityToken> entry) {
        // We're done with this
        return entry.getKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntryBackupProcessor<String, SecurityToken> getBackupProcessor() {
        return backupProcessor;
    }
}
