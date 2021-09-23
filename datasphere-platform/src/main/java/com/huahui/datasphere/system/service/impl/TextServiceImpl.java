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

package com.huahui.datasphere.system.service.impl;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;

import com.huahui.datasphere.system.exception.PlatformRuntimeException;
import com.huahui.datasphere.system.service.TextService;
import com.huahui.datasphere.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;

/**
 * @author Mikhail Mikhailov on Dec 18, 2019
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
