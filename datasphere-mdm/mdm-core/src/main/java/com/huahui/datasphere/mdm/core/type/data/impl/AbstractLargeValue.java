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

package com.huahui.datasphere.mdm.core.type.data.impl;

import com.huahui.datasphere.mdm.core.type.data.LargeValue;
import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;

/**
 * @author theseusyang
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
