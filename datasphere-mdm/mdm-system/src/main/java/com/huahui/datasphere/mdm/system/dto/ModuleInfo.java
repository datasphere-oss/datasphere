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

package com.huahui.datasphere.mdm.system.dto;

import com.huahui.datasphere.mdm.system.type.module.Module;

public class ModuleInfo {
    private final Module module;

    private ModuleStatus moduleStatus = ModuleStatus.NOT_LOADED;

    private String error;

    public ModuleInfo(
            final Module module
    ) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public ModuleStatus getModuleStatus() {
        return moduleStatus;
    }

    public void setModuleStatus(ModuleStatus moduleStatus) {
        this.moduleStatus = moduleStatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public enum ModuleStatus {
        NOT_LOADED,
        LOADING,
        LOADED,
        FAILED,
        INSTALLATION_FAILED,
        START_FAILED
    }
}
