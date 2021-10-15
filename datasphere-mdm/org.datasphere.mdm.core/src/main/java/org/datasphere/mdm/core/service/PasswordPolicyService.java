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

package org.datasphere.mdm.core.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author Alexey Tsarapkin
 */
public interface PasswordPolicyService {
    /**
     *  show user regexp example
     * @return regexpExample
     */
    String regexpExample();

    /**
     * Notification period
     * @return NotificationPeriodDays
     */
    Integer getNotificationPeriodDays();

    /**
     * check password length
     * @param password
     * @return boolean
     */
    boolean isLengthEnough(String password);

    /**
     * Check expired password change allowed
     * @param admin
     * @return boolean
     */
    boolean isAllowChangeExpiredPassword(boolean admin);

    /**
     * Check password expired
     * @param from - date of last created or updated password
     * @param admin
     * @return boolean
     */
    boolean isExpired(Instant from, boolean admin);

    /**
     * Remaining expiration period
     * @param from - date of last created or updated password
     * @param admin
     * @return
     */
    Duration getRemainingExpiration(LocalDateTime from, boolean admin);

    /**
     * Apply regexp
     * @param password
     * @return
     */
    boolean regexpMatching(String password);

    /**
     * Count of last passwords for check repeating
     * @return
     */
    Integer getLastRepeatCount();

    /**
     * check nlimitedExpiration
     * @return
     */
    boolean isUnlimitedExpiration(boolean admin);
}
