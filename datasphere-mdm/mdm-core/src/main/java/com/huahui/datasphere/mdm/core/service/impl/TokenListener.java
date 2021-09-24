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

package com.huahui.datasphere.mdm.core.service.impl;

import com.huahui.datasphere.mdm.system.type.messaging.DomainInstance;
import com.huahui.datasphere.mdm.system.type.messaging.Message;
import com.huahui.datasphere.mdm.system.type.messaging.SystemHeaders;
import com.huahui.datasphere.mdm.system.util.TextUtils;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryExpiredListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.huahui.datasphere.mdm.core.dao.UserDao;
import com.huahui.datasphere.mdm.core.type.messaging.CoreHeaders;
import com.huahui.datasphere.mdm.core.type.messaging.CoreTypes;
import com.huahui.datasphere.mdm.core.type.security.SecurityToken;

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
        final String userName = event.getOldValue().getUser().getLogin();

        coreMessagingDomain.send(new Message(CoreTypes.LOGOUT)
                .withHeader(SystemHeaders.LOGIN, userName)
                .withHeader(CoreHeaders.ACTION_REASON, TextUtils.getText(LOGOUT_BY_TIMEOUT)));
    }
}
