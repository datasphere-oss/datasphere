package com.huahui.datasphere.system.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;

/**
 * @author Mikhail Mikhailov on Dec 22, 2020
 * Illegal, non-public reflection calls.
 * This class is a problem.
 */
final class ReflectionCalls {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionCalls.class);
    /**
     * Current locale method handle.
     */
    private static final Method CURRENT_USER_LOCALE_METHOD;
    /**
     * Current user name method handle.
     */
    private static final Method CURRENT_USER_NAME_METHOD;
    /**
     * Current user endpoint method handle.
     */
    private static final Method CURRENT_USER_ENDPOINT_METHOD;
    /**
     * Current user client IP method handle.
     */
    private static final Method CURRENT_USER_CLIENT_IP_METHOD;
    /**
     * This node IP address method handle.
     */
    private static final Method CURRENT_USER_SERVER_IP_METHOD;
    /**
     * SI.
     */
    static {

        Method currentLocaleMethod = null;
        Method currentUserNameMethod = null;
        Method currentUserEndpointMethod = null;
        Method currentUserClientIpMethod = null;
        Method currentUserServerIpMethod = null;
        try {

            Class<?> klass = Class.forName("org.unidata.mdm.core.util.SecurityUtils");

            currentLocaleMethod = klass.getMethod("getCurrentUserLocale");
            currentUserNameMethod = klass.getMethod("getCurrentUserName");
            currentUserEndpointMethod = klass.getMethod("getCurrentEndpointName");
            currentUserClientIpMethod = klass.getMethod("getCurrentUserClientIp");
            currentUserServerIpMethod = klass.getMethod("getCurrentUserServerIp");

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new PlatformFailureException(
                    "Reflection failure [org.unidata.mdm.core.util.SecurityUtils].",
                    e, SystemExceptionIds.EX_SYSTEM_SECURITY_UTILS_CLASS);
        }

        CURRENT_USER_LOCALE_METHOD = currentLocaleMethod;
        CURRENT_USER_NAME_METHOD = currentUserNameMethod;
        CURRENT_USER_ENDPOINT_METHOD = currentUserEndpointMethod;
        CURRENT_USER_CLIENT_IP_METHOD = currentUserClientIpMethod;
        CURRENT_USER_SERVER_IP_METHOD = currentUserServerIpMethod;
    }
    /**
     * Constructor.
     */
    private ReflectionCalls() {
        super();
    }

    static Locale getCurrentUserLocale() {

        if (CURRENT_USER_LOCALE_METHOD != null) {
            try {
                return (Locale) CURRENT_USER_LOCALE_METHOD.invoke(null, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.warn("Reflection failure [getCurrentUserLocale].", e);
            }
        }

        return null;
    }

    static String getCurrentUserName() {

        if (CURRENT_USER_NAME_METHOD != null) {
            try {
                return (String) CURRENT_USER_NAME_METHOD.invoke(null, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.warn("Reflection failure [getCurrentUserName].", e);
            }
        }

        return null;
    }

    static String getCurrentUserEndpoint() {

        if (CURRENT_USER_ENDPOINT_METHOD != null) {
            try {
                return (String) CURRENT_USER_ENDPOINT_METHOD.invoke(null, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.warn("Reflection failure [getCurrentEndpointName].", e);
            }
        }

        return null;
    }

    static String getCurrentUserClientIp() {

        if (CURRENT_USER_CLIENT_IP_METHOD != null) {
            try {
                return (String) CURRENT_USER_CLIENT_IP_METHOD.invoke(null, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.warn("Reflection failure [getCurrentUserClientIp].", e);
            }
        }

        return null;
    }

    static String getCurrentUserServerIp() {

        if (CURRENT_USER_SERVER_IP_METHOD != null) {
            try {
                return (String) CURRENT_USER_SERVER_IP_METHOD.invoke(null, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.warn("Reflection failure [getCurrentUserServerIp].", e);
            }
        }

        return null;
    }
}
