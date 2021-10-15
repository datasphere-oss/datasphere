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
package org.datasphere.mdm.core.convert;

import java.beans.PropertyEditorSupport;
import java.time.Instant;

import org.datasphere.mdm.system.util.ConvertUtils;

/**
 * @author Mikhail Mikhailov on Jul 10, 2021
 */
public class InstantPropertyEditor extends PropertyEditorSupport {
    /**
     * Constructor.
     */
    public InstantPropertyEditor() {
        super();
    }
    /**
     * Constructor.
     * @param source
     */
    public InstantPropertyEditor(Object source) {
        super(source);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        return ConvertUtils.instant2String((Instant) super.getValue());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setValue(ConvertUtils.string2Instant(text));
    }
}
