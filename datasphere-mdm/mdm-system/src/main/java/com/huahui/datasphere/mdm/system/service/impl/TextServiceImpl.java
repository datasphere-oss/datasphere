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

package com.huahui.datasphere.mdm.system.service.impl;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;
import com.huahui.datasphere.mdm.system.service.TextService;
import com.huahui.datasphere.mdm.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;

/**
 * @author theseusyang on Dec 18, 2019
 */
@Service
public class TextServiceImpl implements TextService {
    /**
     * Default fallback locale.
     */
    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_DEFAULT_LOCALE)
    protected ConfigurationValue<Locale> defaultLocale;
    /**
     * The underlaying message source.
     */
    protected MessageSource messageSource;
    /**
     * Constructor.
     * @param messageSource the MS to use
     */
    protected TextServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getDefaultLocale() {
        return defaultLocale.getValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getCurrentLocale() {
        Locale current  = ReflectionCalls.getCurrentUserLocale();
        return current != null ? current : defaultLocale.getValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getText(String code, Object... args) {
        return getTextWithDefault(code, code, args);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextWithDefault(String code, String defaultMessage, Object... args) {
        return getTextWithLocaleAndDefault(getCurrentLocale(), code, defaultMessage, args);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextWithLocaleAndDefault(Locale locale, String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getText(Enum<?> en) {
        return getTextWithLocaleAndDefault(getCurrentLocale(), en, en != null
                ? (en.getClass().getCanonicalName() + "." + en.name())
                : StringUtils.EMPTY);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextWithLocaleAndDefault(Locale locale, Enum<?> en, String defaultMessage) {

        if (en == null) {
            return StringUtils.EMPTY;
        }

        return getTextWithLocaleAndDefault(locale, en.getClass().getCanonicalName() + "." + en.name(), defaultMessage);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getText(Exception exception) {

        if (exception instanceof PlatformRuntimeException) {
            PlatformRuntimeException pre = (PlatformRuntimeException) exception;
            return getTextWithDefault(pre.getId().message(), pre.getMessage(), pre.getArgs());
        } else {
            return ExceptionUtils.getStackTrace(exception);
        }
    }
}
