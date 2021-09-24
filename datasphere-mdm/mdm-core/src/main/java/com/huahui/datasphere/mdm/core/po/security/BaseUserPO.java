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

package com.huahui.datasphere.mdm.core.po.security;

import java.util.List;

/**
 * @author Denis Kostovarov
 */
public abstract class BaseUserPO extends BaseSecurityPO {
    /**
     * The id.
     */
    private Integer id;
    /**
     * The login.
     */
    private String login;
    /**
     * The S tokens.
     */
    private List<BaseTokenPO> tokens;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the login.
     *
     * @return the login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Sets the login.
     *
     * @param login the new login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the s tokens.
     *
     * @return the s tokens
     */
    public List<BaseTokenPO> getTokens() {
        return this.tokens;
    }

    /**
     * Sets the s tokens.
     *
     * @param tokens the new s tokens
     */
    public void setTokens(List<BaseTokenPO> tokens) {
        this.tokens = tokens;
    }

    /**
     * Adds the s token.
     *
     * @param token the token
     * @return the token po
     */
    public BaseTokenPO addToken(BaseTokenPO token) {
        getTokens().add(token);
        token.setUser(this);

        return token;
    }

    /**
     * Removes the s token.
     *
     * @param token the s token
     * @return the token po
     */
    public TokenPO removeToken(TokenPO token) {
        getTokens().remove(token);
        token.setUser(null);

        return token;
    }

    public static class Fields extends BaseSecurityPO.Fields {

        /**
         * Instantiates a new fields.
         */
        protected Fields() {
            super();
        }


        /** The Constant LOGIN. */
        public static final String LOGIN = "LOGIN";
    }
}
