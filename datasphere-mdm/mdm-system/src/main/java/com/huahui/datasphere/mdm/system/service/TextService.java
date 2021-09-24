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

package com.huahui.datasphere.mdm.system.service;

import java.util.Locale;

import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;

/**
 * Message and translation (i18n) service.
 */
public interface TextService {
    /**
     * Gets default locale.
     * @return default locale
     */
    Locale getDefaultLocale();
    /**
     * Gets currently active locale - either one,
     * set on the user profile,
     * or default, if the user locale is not available.
     * @return locale
     */
    Locale getCurrentLocale();
    /**
     * Gets text for the given code using arguments to substitute placeholders and default locale.
     * @param code the code
     * @param args the arguments
     * @return processed text or the original code
     */
    String getText(String code, Object... args);
    /**
     * Gets text for the given code using arguments to substitute placeholders and default locale.
     * Returns default message, if the code cannot be found.
     * @param code the code
     * @param defaultMessage the default message to return
     * @param args the arguments
     * @return processed text or default message, if the code cannot be found
     */
    String getTextWithDefault(String code, String defaultMessage, Object... args);
    /**
     * Gets text for the given code using arguments to substitute placeholders and given locale.
     * Returns default message, if the code cannot be found.
     * @param locale the locale to look the translation for
     * @param code the code
     * @param defaultMessage the default message to return
     * @param args the arguments
     * @return processed text or default message, if the code cannot be found
     */
    String getTextWithLocaleAndDefault(Locale locale, String code, String defaultMessage, Object... args);
    /**
     * Gets a enum label translation using default system locale.
     * @param en the enum
     * @return translation or enum's FQN, if not found
     */
    String getText(Enum<?> en);
    /**
     * Gets a enum label translation using supplied locale.
     * @param locale the locale to translate for
     * @param en the enum
     * @param defaultMessage default message
     * @return translation or enum's FQN, if not found
     */
    String getTextWithLocaleAndDefault(Locale locale, Enum<?> en, String defaultMessage);
    /**
     * Gets a translation for exception (an {@linkplain PlatformRuntimeException} instance) using default system locale.
     * @param exception the exception
     * @return translation or exception's FQN, if not found
     */
    String getText(Exception exception);
}
