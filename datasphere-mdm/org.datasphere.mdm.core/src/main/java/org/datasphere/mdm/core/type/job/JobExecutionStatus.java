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

package org.datasphere.mdm.core.type.job;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikhail Mikhailov on Jun 25, 2021
 * Same as SB BatchStatus.
 */
public enum JobExecutionStatus {
    /**
     * Finished/complete.
     */
    COMPLETED,
    /**
     * Currently starting.
     */
    STARTING,
    /**
     * Started and running.
     */
    STARTED,
    /**
     * Stopping upon request.
     */
    STOPPING,
    /**
     * Stopped upon request.
     */
    STOPPED,
    /**
     * Failed badly.
     */
    FAILED,
    /**
     * Abandoned.
     */
    ABANDONED,
    /**
     * Unknown.
     */
    UNKNOWN;
    /**
     * Non-throwing, case insensetive valueOf(String).
     * @param v the string
     * @return {@linkplain JobExecutionStatus} or null
     */
    @Nullable
    public static JobExecutionStatus fromValue(final String v) {

        for (int i = 0; i < JobExecutionStatus.values().length; i++) {

            final JobExecutionStatus c = JobExecutionStatus.values()[i];
            if (StringUtils.equalsIgnoreCase(v, c.name())) {
                return c;
            }

        }

        return null;
    }
    /**
     * Tells, whether this job is in active state.
     * @return true, if job execution is active
     */
    public boolean isActive() {
        return this == STARTING || this == STARTED || this == STOPPING;
    }
}
