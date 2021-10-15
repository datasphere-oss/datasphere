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

import com.huahui.datasphere.mdm.core.context.DeleteLargeObjectContext;
import com.huahui.datasphere.mdm.core.context.FetchLargeObjectContext;
import com.huahui.datasphere.mdm.core.context.UpsertLargeObjectContext;
import com.huahui.datasphere.mdm.core.dto.LargeObjectResult;

/**
 * @author Dmitry Kopin on 26.12.2017.
 */
public interface LargeObjectsService {
    /**
     * Gets large object input stream according to context specification.
     * @param ctx the context
     * @return {@link LargeObjectResult} instance
     */
    LargeObjectResult fetchLargeObject(FetchLargeObjectContext ctx);
    /**
     * Saves large object data.
     * @param ctx the context
     * @return true if successful, false otherwise
     */
    LargeObjectResult saveLargeObject(UpsertLargeObjectContext ctx);
    /**
     * Deletes large object data.
     * @param ctx the context
     * @return true if successful, false otherwise
     */
    boolean deleteLargeObject(DeleteLargeObjectContext ctx);
    /**
     * Check large object exist
     * @param ctx context
     * @return return true if exist, else false
     */
    boolean checkExistLargeObject(FetchLargeObjectContext ctx);
}
