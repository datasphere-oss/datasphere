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

import org.datasphere.mdm.core.type.data.LargeValue;
import org.datasphere.mdm.core.type.lob.LargeObjectAcceptance;

/**
 * @author Mikhail Mikhailov
 * Binary large value holder object.
 */
public abstract class AbstractLargeValue<X extends AbstractLargeValue<X>> implements LargeValue {
    /**
     * Data.
     */
    protected byte[] data;
    /**
     * Record id.
     */
    protected String id;
    /**
     * File name
     */
    protected String fileName;
    /**
     * MIME type.
     */
    protected String mimeType;
    /**
     * Size.
     */
    protected long size;
    /**
     * Acceptance state.
     */
    protected LargeObjectAcceptance acceptance;
    /**
     * Constructor.
     */
    protected AbstractLargeValue() {
        super();
    }
    /**
     * Gets the value of the data property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getData() {
        return data;
    }
    /**
     * Sets the value of the data property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setData(byte[] value) {
        this.data = value;
    }
    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Override
    public String getId() {
        return id;
    }
    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName() {
        return fileName;
    }
    /**
     * Sets the value of the fileName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFileName(String value) {
        this.fileName = value;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMimeType() {
        return mimeType;
    }
    /**
     * Sets the value of the mimeType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize() {
        return size;
    }
    /**
     * Sets the value of the size property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setSize(Long value) {
        this.size = value;
    }
    /**
     * @return the acceptance
     */
    @Override
    public LargeObjectAcceptance getAcceptance() {
        return acceptance;
    }
    /**
     * @param acceptance the acceptance to set
     */
    public void setAcceptance(LargeObjectAcceptance acceptance) {
        this.acceptance = acceptance;
    }
    /**
     * Sets data value.
     * @param value the value
     * @return self
     */
    public X withData(byte[] value) {
        setData(value);
        return self();
    }
    /**
     * Sets id.
     * @param value the id
     * @return self
     */
    public X withId(String value) {
        setId(value);
        return self();
    }
    /**
     * Sets file name.
     * @param value the file name
     * @return self
     */
    public X withFileName(String value) {
        setFileName(value);
        return self();
    }
    /**
     * Seats mime type.
     * @param value the mime type
     * @return self
     */
    public X withMimeType(String value) {
        setMimeType(value);
        return self();
    }
    /**
     * Sets size.
     * @param value the size
     * @return self
     */
    public X withSize(long value) {
        setSize(value);
        return self();
    }
    /**
     * Sets accepatnce state.
     * @param value the state
     * @return self
     */
    public X withAcceptance(LargeObjectAcceptance value) {
        setAcceptance(value);
        return self();
    }
    /**
     * Self-cast.
     * @return self
     */
    @SuppressWarnings("unchecked")
    protected X self() {
        return (X) this;
    }
}
