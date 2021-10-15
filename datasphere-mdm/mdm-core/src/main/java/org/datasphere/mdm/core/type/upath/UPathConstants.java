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

package com.huahui.datasphere.mdm.core.type.upath;

/**
 * @author theseusyang
 * Some UPath constants.
 */
public final class UPathConstants {
    /**
     * Constructor.
     */
    private UPathConstants() {
        super();
    }
    /**
     * Internal name of the root element
     */
    public static final String UPATH_ROOT_NAME = "{}";
    /**
     * The 'record' constant, used in expression evaluations.
     */
    public static final String UPATH_RECORD_NAME = "record";
    /**
     * Path separator char.
     */
    public static final char UPATH_SEPARATOR_CHAR = '.';
    /**
     * Start of the UPath filtering expression.
     */
    public static final char UPATH_EXPRESSION_START = '{';
    /**
     * End of the UPath filtering expression.
     */
    public static final char UPATH_EXPRESSION_END = '}';
    /**
     * Mid of the UPath filtering expression.
     */
    public static final char UPATH_EXPRESSION_MID = ':';
    /**
     * Start of the UPath subscript expression.
     */
    public static final char UPATH_SUBSCRIPT_START = '[';
    /**
     * End of the UPath subscript expression.
     */
    public static final char UPATH_SUBSCRIPT_END = ']';
    /**
     * UPath escape symbol.
     */
    public static final char UPATH_ESCAPE_CHAR = '\\';
}
