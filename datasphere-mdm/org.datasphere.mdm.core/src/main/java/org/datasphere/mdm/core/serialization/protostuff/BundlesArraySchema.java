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

package org.datasphere.mdm.core.serialization.protostuff;

import java.io.IOException;

import org.datasphere.mdm.core.type.formless.BundlesArray;
import org.datasphere.mdm.core.type.formless.DataBundle;
import org.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author Mikhail Mikhailov
 * DataBundle (record + free form variables) schema.
 */
public class BundlesArraySchema implements Schema<BundlesArray> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName(int number) {
        return CoreSchemaFields.intToString(number);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getFieldNumber(String name) {
        return CoreSchemaFields.stringToInt(name);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized(BundlesArray message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BundlesArray newMessage() {
        return new BundlesArray();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return BundlesArray.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return BundlesArray.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<BundlesArray> typeClass() {
        return BundlesArray.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, BundlesArray message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {
            if (field == CoreFieldValues.DATA_BUNDLE_VAL) {
                message.add(input.mergeObject(new DataBundle(), CoreSchemas.DATA_BUNDLE_SCHEMA));
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(Output output, BundlesArray message) throws IOException {
        for (DataBundle b : message) {
            output.writeObject(CoreSchemaFields.DATA_BUNDLE.getValue(), b, CoreSchemas.DATA_BUNDLE_SCHEMA, false);
        }
    }
}
