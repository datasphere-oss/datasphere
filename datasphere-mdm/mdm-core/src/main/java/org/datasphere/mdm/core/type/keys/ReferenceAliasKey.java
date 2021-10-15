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

package com.huahui.datasphere.mdm.core.type.keys;

/**
 * @author theseusyang
 * Key attribute alias key.
 */
public class ReferenceAliasKey {
    /**
     * Value.
     */
    private final String value;
    /**
     * Attribute name.
     */
    private final String entityAttributeName;
    /**
     * Constructor.
     * @param b builder
     */
    private ReferenceAliasKey(ReferenceAliasKeyBuilder b) {
        super();
        this.value = b.value;
        this.entityAttributeName = b.entityAttributeName;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the entityAttributeName
     */
    public String getEntityAttributeName() {
        return entityAttributeName;
    }

    @Override
	public String toString() {
		return new StringBuilder()
				.append("entityAttributeName = ")
				.append(entityAttributeName)
				.append("value = ")
				.append(value)
				.toString();
	}

	/**
     * @return new builder
     */
    public static ReferenceAliasKeyBuilder builder() {
        return new ReferenceAliasKeyBuilder();
    }
    /**
     * @author theseusyang
     * Builder.
     */
    public static class ReferenceAliasKeyBuilder {
        /**
         * Value.
         */
        private String value;
        /**
         * Attribute name.
         */
        private String entityAttributeName;

        /**
         * Constructor.
         */
        public ReferenceAliasKeyBuilder() {
            super();
        }

        /**
         * @param value the value to set
         */
        public ReferenceAliasKeyBuilder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * @param entityAttributeName the entityAttributeName to set
         */
        public ReferenceAliasKeyBuilder entityAttributeName(String entityAttributeName) {
            this.entityAttributeName = entityAttributeName;
            return this;
        }

        /**
         * @return new alias key
         */
        public ReferenceAliasKey build() {
            return new ReferenceAliasKey(this);
        }
    }
}
