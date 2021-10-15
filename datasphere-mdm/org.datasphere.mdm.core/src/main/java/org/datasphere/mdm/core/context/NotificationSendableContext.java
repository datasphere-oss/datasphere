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

/**
 *
 */
package org.datasphere.mdm.core.context;

import org.datasphere.mdm.system.context.BooleanFlagsContext;
import org.datasphere.mdm.system.context.StorageCapableContext;
import org.datasphere.mdm.system.context.StorageId;

/**
 * @author Mikhail Mikhailov
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
