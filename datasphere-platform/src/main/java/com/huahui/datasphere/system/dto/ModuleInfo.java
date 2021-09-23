/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.huahui.datasphere.system.dto;

import com.huahui.datasphere.system.type.module.Module;

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
