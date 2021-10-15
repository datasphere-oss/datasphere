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

import javax.annotation.Nonnull;

import com.huahui.datasphere.mdm.core.type.data.Attribute;
import com.huahui.datasphere.mdm.core.type.data.DataRecord;
import com.huahui.datasphere.mdm.core.type.upath.UPath;
import com.huahui.datasphere.mdm.core.type.upath.UPathApplicationMode;
import com.huahui.datasphere.mdm.core.type.upath.UPathExecutionContext;
import com.huahui.datasphere.mdm.core.type.upath.UPathResult;

/**
 * @author theseusyang
 * UPath service.
 */
public interface UPathService {
    /**
     * Creates UPath chain.
     * @param path UPath expression, must not be null
     * @return UPath instance
     * @throws UPathException
     */
    UPath upathCreate(@Nonnull String path);
    /**
     * Evaluates the given UPath against the {@linkplain DataRecord}
     * using {@link UPathExecutionContext#FULL_TREE} context and {@link UPathApplicationMode#MODE_ALL}.
     * @param upath the UPath instance
     * @param record the record
     * @return result
     */
    UPathResult upathGet(UPath upath, DataRecord record);
    /**
     * Evaluates the given UPath against the {@linkplain DataRecord}
     * using {@link UPathExecutionContext#FULL_TREE} context and specified application mode.
     * @param upath the UPath instance, must not be null
     * @param record the record, must not be null
     * @param mode the application mode
     * @return result
     * @throws UPathException
     */
    UPathResult upathGet(@Nonnull UPath upath, @Nonnull DataRecord record, UPathApplicationMode mode);
    /**
     * Evaluates the given UPath against the {@linkplain DataRecord}.
     * @param upath the UPath instance, must not be null
     * @param record the record, must not be null
     * @param context the context to apply
     * @param mode the application mode
     * @return result
     * @throws UPathException
     */
    UPathResult upathGet(@Nonnull UPath upath, @Nonnull DataRecord record, UPathExecutionContext context, UPathApplicationMode mode);
    /**
     * Evaluates the given UPath against the {@linkplain DataRecord} and applies target.
     * @param upath the UPath instance, must not be null
     * @param record the record, must not be null
     * @param target target attribute
     * @param context the context
     * @param mode application mode
     * @return true, if target attribute was applied successfullly at least once
     * @throws UPathException
     */
    boolean upathSet(@Nonnull UPath upath, @Nonnull DataRecord record, Attribute target, UPathExecutionContext context, UPathApplicationMode mode);
    /**
     * Evaluates the given UPath against the {@linkplain DataRecord} and applies target.
     * @param upath the UPath instance, must not be null
     * @param record the record, must not be null
     * @param target target attribute
     * @param mode application mode
     * @return true, if target attribute was applied successfullly at least once
     * @throws UPathException
     */
    boolean upathSet(@Nonnull UPath upath, @Nonnull DataRecord record, Attribute target, UPathApplicationMode mode);
    /**
     * Evaluates the given UPath against the {@linkplain DataRecord} and applies target.
     * @param upath the UPath instance, must not be null
     * @param record the record, must not be null
     * @param target target attribute
     * @return true, if target attribute was applied successfullly at least once
     * @throws UPathException
     */
    boolean upathSet(@Nonnull UPath upath, @Nonnull DataRecord record, Attribute target);
    /**
     * Combines previous two.
     * @param path UPath expression, must not be null
     * @param record the record, must not be null
     * @return result
     * @throws UPathException
     */
    UPathResult upathResult(@Nonnull String path, @Nonnull DataRecord record);
}
