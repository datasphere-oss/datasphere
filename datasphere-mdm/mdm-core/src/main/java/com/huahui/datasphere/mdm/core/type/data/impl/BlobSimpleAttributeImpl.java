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

import java.util.Arrays;
import java.util.Objects;

import com.huahui.datasphere.mdm.core.type.data.BinaryLargeValue;
import com.huahui.datasphere.mdm.core.type.data.SimpleAttribute;

/**
 * @author theseusyang
 * BLOB simple attribute.
 */
public class BlobSimpleAttributeImpl extends AbstractSimpleAttribute<BinaryLargeValue> {
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected BlobSimpleAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name
     */
    public BlobSimpleAttributeImpl(String name) {
        super(name);
    }
    /**
     * Constructor.
     * @param name
     * @param value
     */
    public BlobSimpleAttributeImpl(String name, BinaryLargeValue value) {
        super(name, value);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.BLOB;
    }

    /**
     * Fluent part for compatibility.
     * @param value the value
     * @return self
     */
    public BlobSimpleAttributeImpl withValue(BinaryLargeValue value) {
        setValue(value);
        return this;
    }
    /**
     * @return hash code
     */
    @Override
    public int hashCode() {
        BinaryLargeValue bv = getValue();
        return Objects.hash(SimpleDataType.BLOB,
                bv != null ? bv.getFileName() : null,
                bv != null ? bv.getSize() : null,
                bv != null ? bv.getMimeType() : null,
                bv != null ? bv.getId() : null,
                bv != null ? bv.getAcceptance() : null);
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
        SimpleAttribute<BinaryLargeValue> target = (SimpleAttribute<BinaryLargeValue>) other;

        BinaryLargeValue bv1 = getValue();
        Object[] thisAttrs = {
            bv1 != null ? bv1.getFileName() : null,
            bv1 != null ? bv1.getSize() : null,
            bv1 != null ? bv1.getMimeType() : null,
            bv1 != null ? bv1.getId() : null,
            bv1 != null ? bv1.getAcceptance() : null
        };

        BinaryLargeValue bv2 = target.getValue();
        Object[] thatAttrs = {
            bv2 != null ? bv2.getFileName() : null,
            bv2 != null ? bv2.getSize() : null,
            bv2 != null ? bv2.getMimeType() : null,
            bv2 != null ? bv2.getId() : null,
            bv2 != null ? bv2.getAcceptance() : null
        };

        return Arrays.equals(thisAttrs, thatAttrs);
    }

}
