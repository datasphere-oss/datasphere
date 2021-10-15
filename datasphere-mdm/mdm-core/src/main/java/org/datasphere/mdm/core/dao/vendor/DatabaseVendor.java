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

package com.huahui.datasphere.mdm.core.dao.vendor;

import org.apache.commons.lang3.StringUtils;

/**
 * @author theseusyang
 * Database vendor type.
 */
public enum DatabaseVendor {
    /**
     * PgSQL.
     */
    POSTGRESQL("jdbc:postgresql:", "org.postgresql.Driver"),
    /**
     * MSSQL.
     */
    MSSQL("jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    /**
     * Oracle.
     */
    ORACLE("jdbc:oracle:", "oracle.jdbc.OracleDriver"),
    /**
     * IBM DB2.
     */
    DB2_JCC("jdbc:db2:", "com.ibm.db2.jcc.DB2Driver"),
    /**
     * IBM DB2 JDBC.
     */
    DB2_JDBC("jdbc:db2:", "COM.ibm.db2.jdbc.app.DB2Driver");
    /**
     * URL prefix.
     */
    private final String urlPrefix;
    /**
     * Class name of the driver.
     */
    private final String driverClassName;
    /**
     * Constructor.
     * @param urlPrefix
     * @param driverClassName
     */
    private DatabaseVendor(String urlPrefix, String driverClassName) {
        this.urlPrefix = urlPrefix;
        this.driverClassName = driverClassName;
    }
    /**
     * Guesses the vendor from DB URL.
     * @param url the URL
     * @return vendor or null, if not supported
     */
    public static DatabaseVendor fromUrl(String url) {

        if (StringUtils.isNotBlank(url)) {
            for (DatabaseVendor current : DatabaseVendor.values()) {
                if (url.startsWith(current.urlPrefix)) {
                    return current;
                }
            }
        }

        return null;
    }
    /**
     * Guesses the vendor from driver class name.
     * @param driverClassName the name
     * @return vendor or null, if not supported
     */
    public static DatabaseVendor fromDriverClassName(String driverClassName) {

        if (StringUtils.isNotBlank(driverClassName)) {
            for (DatabaseVendor current : DatabaseVendor.values()) {
                if (driverClassName.equals(current.driverClassName)) {
                    return current;
                }
            }
        }

        return null;
    }

    public static void registerSupportedDrivers() {

        for (DatabaseVendor current : DatabaseVendor.values()) {
            try {
                Class.forName(current.driverClassName);
            } catch (Exception e) { }
        }
    }
}
