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

/**
 *
 */
package com.huahui.datasphere.mdm.core.context;

import com.huahui.datasphere.mdm.system.context.BooleanFlagsContext;
import com.huahui.datasphere.mdm.system.context.StorageCapableContext;
import com.huahui.datasphere.mdm.system.context.StorageId;

/**
 * @author theseusyang
 * Is this context capable for notifications?
 * TODO: Check that this interface is still needed. Appears to be pretty useless and even harmful.
 */
public interface NotificationSendableContext extends BooleanFlagsContext, StorageCapableContext {
    /**
     * The notification id SID.
     */
    StorageId SID_NOTIFICATION_ID = new StorageId("NOTIFICATION_ID");
    /**
     * The notification destination SID.
     */
    StorageId SID_NOTIFICATION_DEST = new StorageId("NOTIFICATION_DEST");
    /**
     * Whether notification should be send or not.
     * @return true, if the context should be sent, false otherwise
     */
    default boolean sendNotification() {
        return getFlag(CoreContextFlags.FLAG_SEND_NOTIFICATION);
    }
    /**
     * Send notification using the infos below.
     * @param notificationDestination the destination
     * @param notificationId the id
     */
    default void sendNotification(String notificationDestination, String notificationId) {
        setFlag(CoreContextFlags.FLAG_SEND_NOTIFICATION);
        notificationDestination(notificationDestination);
        notificationId(notificationId);
    }
    /**
     * Tells that a notification should be skipped for this context.
     */
    default void skipNotification() {
        clearFlag(CoreContextFlags.FLAG_SEND_NOTIFICATION);
    }
    /**
     * Gets the destination. This may be JMS replyTo destination queue JNDI name.
     * @return destination name
     */
    default String notificationDestination() {
        return getFromStorage(SID_NOTIFICATION_DEST);
    }
    /**
     * Sets the notification destination.
     * @param destination the destination
     */
    default void notificationDestination(String destination) {
        putToStorage(SID_NOTIFICATION_DEST, destination);
    }
    /**
     * Gets the replay ID. This may be JMS correlationId.
     * @return id
     */
    default String notificationId() {
        return getFromStorage(SID_NOTIFICATION_ID);
    }
    /**
     * Sets the notification id.
     * @param id the notification id
     */
    default void notificationId(String id) {
        putToStorage(SID_NOTIFICATION_ID, id);
    }
    /**
     * Repeat the behaviour.
     * @param notificationSendableContext the behaviour to repeat
     */
    default void repeatNotificationBehavior(NotificationSendableContext notificationSendableContext) {
        notificationDestination(notificationSendableContext.notificationDestination());
        notificationId(notificationSendableContext.notificationId());
        if (notificationSendableContext.sendNotification()) {
            setFlag(CoreContextFlags.FLAG_SEND_NOTIFICATION);
        } else {
            clearFlag(CoreContextFlags.FLAG_SEND_NOTIFICATION);
        }
    }
}
