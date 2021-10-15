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
package com.huahui.datasphere.mdm.core.type.model;

import java.util.Objects;

/**
 * @author theseusyang on Oct 8, 2020
 * The model descriptor.
 */
public final class ModelDescriptor<I extends ModelInstance<?>> {
    /**
     * ID/name.
     */
    private final String typeId;
    /**
     * Instance implementing class.
     */
    private final Class<I> instance;
    /**
     * Constructor.
     * @param typeId the model type name/ID
     */
    public ModelDescriptor(String typeId, Class<I> instance) {
        super();
        Objects.requireNonNull(typeId, "Model type id must not be null.");
        this.typeId = typeId;
        this.instance = instance;
    }
    /**
     * @return the name
     */
    public String getModelTypeId() {
        return typeId;
    }
    /**
     * @return the instance
     */
    public Class<I> getInstance() {
        return instance;
    }
    /**
     * Shortcut.
     * @param <I> instance type
     * @param name the model type id
     * @param klass the class
     * @return descriptor
     */
    public static<I extends ModelInstance<?>> ModelDescriptor<I> of(String name, Class<I> klass) {
        return new ModelDescriptor<>(name, klass);
    }
}
