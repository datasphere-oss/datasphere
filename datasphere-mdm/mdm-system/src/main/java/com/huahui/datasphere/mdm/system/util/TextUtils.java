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

package com.huahui.datasphere.mdm.system.util;

import java.util.Locale;

import com.huahui.datasphere.mdm.system.configuration.SystemConfiguration;
import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;
import com.huahui.datasphere.mdm.system.service.TextService;

/**
 * system module message utils
 *
 * @author maria.chistyakova
 * @since  05.11.2019
 */
public class TextUtils {
    /**
     * The TT instance.
     */
    private static TextService textService;
    /**
     * Constructor.
     */
    private TextUtils() {
        super();
    }
    /**
     * Public "internal" init method.
     */
    public static void init() {
        textService = SystemConfiguration.getBean(TextService.class);
    }
    /**
     * Get default system locale.
     * @return locale default system locale.
     */
    public static Locale getDefaultLocale() {
        return textService.getDefaultLocale();
    }
    /**
     * Get current user's locale.
     * @return locale default system locale.
     */
    public static Locale getCurrentLocale() {
        return textService.getCurrentLocale();
    }
    /**
     * Gets translation for supplied code.
     * @param code the code
     * @param args optional arguments
     * @return translation
     */
    public static String getText(String code, Object... args) {
        return textService.getText(code, args);
    }
    /**
     * Gets translation for supplied code, returning default value, if the translation was not found.
     * @param code the code
     * @param defaultText fallback to use
     * @param args optional arguments
     * @return translation
     */
    public static String getTextWithDefault(String code, String defaultText, Object... args) {
        return textService.getTextWithDefault(code, defaultText, args);
    }

    public static String getTextWithLocaleAndDefault(Locale locale, String code, String defaultMessage, Object... args) {
        return textService.getTextWithLocaleAndDefault(locale, code, defaultMessage, args);
    }

    public static String getTextWithLocaleAndDefault(Locale locale, Enum<?> en, String defaultMessage) {
        return textService.getTextWithLocaleAndDefault(locale, en, defaultMessage);
    }
    /**
     * Extracts code and does translation, if thic exception is a {@link PlatformRuntimeException}, prints stack trace otherwise.
     * @param exception the exception to process
     * @return message
     */
    public static String getText(Exception exception) {
        return textService.getText(exception);
    }
    /**
     * Gets a enum label translation.
     * @param en enum label
     * @return translation
     */
    public static String getText(Enum<?> en) {
        return textService.getText(en);
    }
}
