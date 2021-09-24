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

package com.huahui.datasphere.mdm.core.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.huahui.datasphere.mdm.system.context.StorageCapableContext;
import com.huahui.datasphere.mdm.system.context.StorageId;
/**
 * @author theseusyang
 * The _former_ finalize participant context, extracted to interface.
 */
public interface TransactionalRequestContext extends StorageCapableContext {
    /**
     * The transaction finalizers SID.
     */
    StorageId SID_TRANSACTION_FINALIZERS = new StorageId("TRANSACTION_FINALIZERS");
    /**
     * Executes upon transaction commit.
     * @param executor the executor to run
     */
    default void addFinalizeExecutor(Consumer<TransactionalRequestContext> executor) {
        List<Consumer<TransactionalRequestContext>> finalizeExecutors = getFromStorage(SID_TRANSACTION_FINALIZERS);
        if (finalizeExecutors == null) {
            finalizeExecutors = new ArrayList<>();
        }

        finalizeExecutors.add(executor);
    }
    /**
     * Gets the list of collected executors
     * @return list of executors
     */
    default List<Consumer<TransactionalRequestContext>> getFinalizeExecutors() {
        List<Consumer<TransactionalRequestContext>> finalizeExecutors = getFromStorage(SID_TRANSACTION_FINALIZERS);
        return Objects.isNull(finalizeExecutors) ? Collections.emptyList() : finalizeExecutors;
    }
}