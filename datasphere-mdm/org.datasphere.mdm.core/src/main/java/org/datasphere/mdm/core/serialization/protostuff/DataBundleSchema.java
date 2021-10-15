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

import org.datasphere.mdm.core.type.data.impl.SerializableDataRecord;
import org.datasphere.mdm.core.type.formless.DataBundle;
import org.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;
import org.datasphere.mdm.system.serialization.protostuff.SystemFieldValues;
import org.datasphere.mdm.system.serialization.protostuff.SystemSchemaFields;
import org.datasphere.mdm.system.serialization.protostuff.SystemSchemas;
import org.datasphere.mdm.system.type.variables.Variables;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author Mikhail Mikhailov
 * DataBundle (record + free form variables) schema.
 */
public class DataBundleSchema implements Schema<DataBundle> {
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
    public boolean isInitialized(DataBundle message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataBundle newMessage() {
        return new DataBundle();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return DataBundle.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return DataBundle.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<DataBundle> typeClass() {
        return DataBundle.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, DataBundle message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.DATA_RECORD_VAL:
                message.setRecord(input.mergeObject(new SerializableDataRecord(), CoreSchemas.DATA_RECORD_SCHEMA));
                break;
            case SystemFieldValues.VARIABLES_VAL:
                message.setVariables(input.mergeObject(new Variables(), SystemSchemas.VARIABLES_SCHEMA));
                break;
            default:
                break;
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(Output output, DataBundle message) throws IOException {

        if (message.hasVariables()) {
            output.writeObject(SystemSchemaFields.VARIABLES.getValue(), message.getVariables(), SystemSchemas.VARIABLES_SCHEMA, false);
        }

        if (message.hasRecord()) {
            output.writeObject(CoreSchemaFields.DATA_RECORD.getValue(), message.getRecord(), CoreSchemas.DATA_RECORD_SCHEMA, false);
        }
    }
}
