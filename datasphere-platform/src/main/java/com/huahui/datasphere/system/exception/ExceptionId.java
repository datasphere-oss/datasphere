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

package com.huahui.datasphere.system.exception;

import java.util.Objects;
/**
 * @author Alexander Malyshev
 *
 * The exception ID class.
 */
public final class ExceptionId {
    /**
     * Unique exception code. Must be used only once in the app.
     */
    private final String code;
    /**
     * Translation code of a localized message (not the final message).
     */
    private final String message;
    /**
     * Constructor.
     * @param code the code.
     * @param message the translation code of a localized message.
     */
    public ExceptionId(String code, String message) {
        this.code = code;
        this.message = message;
    }
    /**
     * Msg code.
     * @return code
     */
    public String code() {
        return code;
    }
    /**
     * Translation code.
     * @return translation code
     */
    public String message() {
        return message;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExceptionId)) {
            return false;
        }
        ExceptionId that = (ExceptionId) o;
        return Objects.equals(code, that.code);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return code;
    }
}