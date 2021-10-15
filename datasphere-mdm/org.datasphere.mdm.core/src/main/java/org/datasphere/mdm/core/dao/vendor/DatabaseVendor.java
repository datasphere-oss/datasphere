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

package org.datasphere.mdm.core.dao.vendor;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikhail Mikhailov
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
