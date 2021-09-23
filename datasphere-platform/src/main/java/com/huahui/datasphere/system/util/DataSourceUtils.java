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

package com.huahui.datasphere.system.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;

/**
 * @author mikhail
 * Data source factory utils.
 */
public final class DataSourceUtils {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceUtils.class);
    /**
     * Tomcat DSF class name.
     */
    private static final String TOMCAT_FACTORY_CLASS_NAME = "org.apache.tomcat.jdbc.pool.DataSourceFactory";
    /**
     * Tomcat pool class name.
     */
    private static final String TOMCAT_DATA_SOURCE_CLASS_NAME = "org.apache.tomcat.jdbc.pool.DataSource";
    /**
     * Apache DS factory instance.
     */
    private static final Object TOMCAT_DATA_SOURCE_FACTORY;
    /**
     * 'createDataSource' method.
     */
    private static final Method TOMCAT_CREATE_DATA_SOURCE_METHOD;
    /**
     * Tomcat pool class name.
     */
    private static final String BITRONIX_DATA_SOURCE_CLASS_NAME = "bitronix.tm.resource.jdbc.PoolingDataSource";
    /**
     * Static initialization.
     */
    static {

        // Bad things happen here
        Object instance = null;
        Method method = null;
        try {
            Class<?> klass = Class.forName(DataSourceUtils.TOMCAT_FACTORY_CLASS_NAME);
            instance = klass.getDeclaredConstructor().newInstance();
            method = klass.getMethod("createDataSource", Properties.class);
        } catch (ClassNotFoundException | InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            final String message = "Failed to instantiate '{}' NON-XA data source factory class.";
            LOGGER.error(message, TOMCAT_FACTORY_CLASS_NAME, e);
            throw new PlatformFailureException(message,
                    SystemExceptionIds.EX_SYSTEM_CANNOT_INITIALIZE_NON_XA_FACTORY,
                    TOMCAT_FACTORY_CLASS_NAME);
        } finally {
            TOMCAT_DATA_SOURCE_FACTORY = instance;
            TOMCAT_CREATE_DATA_SOURCE_METHOD = method;
        }
    }
    /**
     * Constructor.
     */
    private DataSourceUtils() {
        super();
    }
    /**
     * Creates a data source.
     * @param properties the properties
     * @return data source or null
     */
    public static DataSource newPoolingNonXADataSource(Properties properties) {

        if (Objects.isNull(TOMCAT_DATA_SOURCE_FACTORY)
         || Objects.isNull(TOMCAT_CREATE_DATA_SOURCE_METHOD)) {
            return null;
        }

        try {
            return (DataSource) TOMCAT_CREATE_DATA_SOURCE_METHOD.invoke(TOMCAT_DATA_SOURCE_FACTORY, properties);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            final String message = "Failed to create NON-XA data source.";
            throw new PlatformFailureException(message, e, SystemExceptionIds.EX_SYSTEM_CANNOT_CREATE_NON_XA_DATASOURCE);
        }
    }
    /**
     * Creates a data source.
     * @param properties the properties
     * @return data source or null
     */
    public static DataSource newPoolingXADataSource(Properties properties) {

        // Bad things happen here
        Class<?> klass = null;
        Method method = null;
        Object instance = null;
        try {
            klass = Class.forName(DataSourceUtils.BITRONIX_DATA_SOURCE_CLASS_NAME);
            instance = klass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            final String message = "Failed to instantiate '{}' XA data source.";
            LOGGER.error(message, BITRONIX_DATA_SOURCE_CLASS_NAME, e);
            throw new PlatformFailureException(message,
                    SystemExceptionIds.EX_SYSTEM_CANNOT_INITIALIZE_XA_FACTORY,
                    BITRONIX_DATA_SOURCE_CLASS_NAME);
        }

        try {

            String url = null;
            for (Entry<Object, Object> entry : properties.entrySet()) {

                if (StringUtils.equalsIgnoreCase("url", entry.getKey().toString())) {
                    url = Objects.nonNull(entry.getValue()) ? entry.getValue().toString() : null;
                    continue;
                }

                Class<?> targetClazz = PropertyUtils.getPropertyType(instance, entry.getKey().toString());
                if (targetClazz != null) {

                    String stringVal = Objects.nonNull(entry.getValue()) ? entry.getValue().toString() : null;
                    if (targetClazz == String.class) {
                        PropertyUtils.setProperty(instance, entry.getKey().toString(), stringVal);
                    } else {

                        Object targetValue = ConvertUtils.convert(stringVal, targetClazz);
                        if (targetValue != stringVal) {
                            PropertyUtils.setProperty(instance, entry.getKey().toString(), targetValue);
                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(url)) {

                Properties p = new Properties();
                p.setProperty("url", url);

                method = klass.getMethod("setDriverProperties", Properties.class);
                method.invoke(instance, p);
            }

            method = klass.getMethod("init");
            method.invoke(instance);

        } catch (Exception e) {
            final String message = "Failed to create XA data source.";
            throw new PlatformFailureException(message, e, SystemExceptionIds.EX_SYSTEM_CANNOT_CREATE_XA_DATASOURCE);
        }

        return (DataSource) instance;
    }

    // Bad stuff.
    public static void shutdown(DataSource ds) {

        if (Objects.isNull(ds)) {
            return;
        }

        try {

            Class<?> tomcatDataSourceKlass = Class.forName(TOMCAT_DATA_SOURCE_CLASS_NAME);
            Class<?> bitronixDataSourceKlass = Class.forName(BITRONIX_DATA_SOURCE_CLASS_NAME);

            if (bitronixDataSourceKlass.isAssignableFrom(ds.getClass())) {
                Method m = ds.getClass().getMethod("close"); // Spring proxy
                m.invoke(ds);
            } else if (tomcatDataSourceKlass.isAssignableFrom(ds.getClass())) {
                Method m = ds.getClass().getMethod("close", boolean.class); // Spring proxy
                m.invoke(ds, true);
            }

        } catch (Exception e) {
            LOGGER.warn("Exception caught, while closing pooling data source!", e);
        }
    }
}
