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

package org.datasphere.mdm.core.type.data.impl;

import java.util.Arrays;
import java.util.Objects;

import org.datasphere.mdm.core.type.data.CharacterLargeValue;
import org.datasphere.mdm.core.type.data.SimpleAttribute;

/**
 * @author Mikhail Mikhailov
 *  CLOB simple attribute.
 */
public class ClobSimpleAttributeImpl extends AbstractSimpleAttribute<CharacterLargeValue> {

    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected ClobSimpleAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name
     */
    public ClobSimpleAttributeImpl(String name) {
        super(name);
    }

    /**
     * Constructor.
     * @param name
     * @param value
     */
    public ClobSimpleAttributeImpl(String name, CharacterLargeValue value) {
        super(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.CLOB;
    }

    /**
     * Fluent part for compatibility.
     * @param value the value
     * @return self
     */
    public ClobSimpleAttributeImpl withValue(CharacterLargeValue value) {
        setValue(value);
        return this;
    }

    /**
     * @return hash code
     */
    @Override
    public int hashCode() {
        CharacterLargeValue cv = getValue();
        return Objects.hash(SimpleDataType.CLOB,
                cv != null ? cv.getFileName() : null,
                cv != null ? cv.getSize() : null,
                cv != null ? cv.getMimeType() : null,
                cv != null ? cv.getId() : null,
                cv != null ? cv.getAcceptance() : null);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!SimpleAttribute.class.isInstance(obj)) {
            return false;
        }

        SimpleAttribute<?> other = (SimpleAttribute<?>) obj;
        if (getDataType() != other.getDataType()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        SimpleAttribute<CharacterLargeValue> target = (SimpleAttribute<CharacterLargeValue>) other;

        CharacterLargeValue cv1 = getValue();
        Object[] thisAttrs = {
            cv1 != null ? cv1.getFileName() : null,
            cv1 != null ? cv1.getSize() : null,
            cv1 != null ? cv1.getMimeType() : null,
            cv1 != null ? cv1.getId() : null,
            cv1 != null ? cv1.getAcceptance() : null
        };

        CharacterLargeValue cv2 = target.getValue();
        Object[] thatAttrs = {
            cv2 != null ? cv2.getFileName() : null,
            cv2 != null ? cv2.getSize() : null,
            cv2 != null ? cv2.getMimeType() : null,
            cv2 != null ? cv2.getId() : null,
            cv2 != null ? cv2.getAcceptance() : null
        };

        return Arrays.equals(thisAttrs, thatAttrs);
    }
}
