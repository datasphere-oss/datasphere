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

package org.datasphere.mdm.core.po.security;

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
