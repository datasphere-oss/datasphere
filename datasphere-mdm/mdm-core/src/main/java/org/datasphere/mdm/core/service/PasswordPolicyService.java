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
