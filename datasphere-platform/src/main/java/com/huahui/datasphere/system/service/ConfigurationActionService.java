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

package com.huahui.datasphere.system.service;

import javax.annotation.Nonnull;

import com.huahui.datasphere.system.type.action.ConfigurationAction;

/**
 * @author Mikhail Mikhailov
 * Configuration action service.
 * This service runs configuration actions in 'once for the cluster' or 'strictly n times'.
 */
public interface ConfigurationActionService {
    /**
     * Executes the supplied action, blocking other actions of the same type in all other threads cluster wide.
     * Runs only if the execution counter has not reached action's nTimes.
     * @param action the action to execute
     * @return true if the action was executed, false if not, either because the nTimes counter was reached or retries numer was exceeded.
     */
    boolean execute(@Nonnull ConfigurationAction action);
}
