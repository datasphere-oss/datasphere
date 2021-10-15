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

package org.datasphere.mdm.core.service.impl;

import java.util.Objects;

import org.datasphere.mdm.core.dao.UserDao;
import org.datasphere.mdm.core.type.messaging.CoreHeaders;
import org.datasphere.mdm.core.type.messaging.CoreTypes;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.system.type.messaging.DomainInstance;
import org.datasphere.mdm.system.type.messaging.Message;
import org.datasphere.mdm.system.type.messaging.SystemHeaders;
import org.datasphere.mdm.system.util.TextUtils;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryExpiredListener;
import com.hazelcast.map.listener.EntryRemovedListener;

/**
 * The listener interface for receiving token events. The class that is
 * interested in processing a token event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addTokenListener<code> method. When the token event occurs,
 * that object's appropriate method is invoked.
 *
 * @author ilya.bykov
 */
public class TokenListener implements
    EntryRemovedListener<String, SecurityToken>,
    EntryExpiredListener<String, SecurityToken>,
    EntryEvictedListener<String, SecurityToken> {

    private static final String LOGOUT_BY_TIMEOUT="app.audit.record.operation.logout.byTimeout";

    private final DomainInstance coreMessagingDomain;

    /** The user dao. */
    private final UserDao userDao;

    /**
     * Instantiates a new token listener.
     *
     * @param coreMessagingDomain the core notification sender
     * @param userDao           the user dao
     */
    public TokenListener(final DomainInstance coreMessagingDomain, final UserDao userDao) {
        this.coreMessagingDomain = coreMessagingDomain;
        this.userDao = userDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hazelcast.map.listener.EntryRemovedListener#entryRemoved(com.
     * hazelcast.core.EntryEvent)
     */
    @Override
    public void entryRemoved(EntryEvent<String, SecurityToken> event) {
        userDao.deleteToken(event.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void entryEvicted(EntryEvent<String, SecurityToken> event) {
        userDao.deleteToken(event.getKey());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hazelcast.map.listener.EntryEvictedListener#entryEvicted(com.
     * hazelcast.core.EntryEvent)
     */
    @Override
    public void entryExpired(EntryEvent<String, SecurityToken> event) {

        // No need to delete token. It is already done above in entryEvicted()
        final SecurityToken token = event.getOldValue();
        coreMessagingDomain.send(new Message(CoreTypes.LOGOUT)
                .withHeader(SystemHeaders.LOGIN, token.getUser().getLogin())
                .withHeader(SystemHeaders.ENDPOINT, Objects.nonNull(token.getEndpoint()) ? token.getEndpoint().name() : "<unknown>")
                .withHeader(SystemHeaders.CLIENT_IP, token.getUserIp())
                .withHeader(SystemHeaders.SERVER_IP, token.getServerIp())
                .withHeader(CoreHeaders.ACTION_REASON, TextUtils.getText(LOGOUT_BY_TIMEOUT)));
    }
}
