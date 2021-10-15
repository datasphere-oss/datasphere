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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.datasphere.mdm.core.serialization.VerifyableArrayAttribute;
import org.datasphere.mdm.core.type.data.ArrayAttribute;
import org.datasphere.mdm.core.type.data.impl.AbstractAttribute;
import org.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;
import org.datasphere.mdm.system.serialization.protostuff.OutpuMappingSupport;
import org.datasphere.mdm.system.util.ByteUtils;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author Mikhail Mikhailov
 * Array attributes.
 */
public class ArrayAttributeSchema implements Schema<ArrayAttribute<?>>, OutpuMappingSupport {
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
    public boolean isInitialized(ArrayAttribute<?> message) {

        if (message instanceof VerifyableArrayAttribute<?>) {
            return ((VerifyableArrayAttribute<?>) message).isValid();
        }

        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayAttribute<?> newMessage() {
        // Should not be called.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return ArrayAttribute.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return ArrayAttribute.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super ArrayAttribute<?>> typeClass() {
        return ArrayAttribute.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, ArrayAttribute<?> message) throws IOException {

        List<Object> objects = new ArrayList<>();
        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.FIELD_NAME_VAL:
                ((AbstractAttribute) message).setName(input.readString());
                break;
            case CoreFieldValues.STRING_ARRAY_VALUE_VAL:
                objects.add(input.readString());
                break;
            case CoreFieldValues.INTEGER_ARRAY_VALUE_VAL:
                objects.add(input.readInt64());
                break;
            case CoreFieldValues.NUMBER_ARRAY_VALUE_VAL:
                objects.add(input.readDouble());
                break;
            case CoreFieldValues.DATE_ARRAY_VALUE_VAL:
                objects.add(ByteUtils.unpackLocalDate(input.readByteArray()));
                break;
            case CoreFieldValues.TIME_ARRAY_VALUE_VAL:
                objects.add(ByteUtils.unpackLocalTime(input.readByteArray()));
                break;
            case CoreFieldValues.TIMESTAMP_ARRAY_VALUE_VAL:
                objects.add(ByteUtils.unpackLocalDateTime(input.readByteArray()));
                break;
            default:
                break;
            }
        }

        message.castValue(objects);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void writeTo(Output output, ArrayAttribute<?> message) throws IOException {

        output.writeString(CoreSchemaFields.FIELD_NAME.getValue(), message.getName(), false);
        if (message.isEmpty()) {
            return;
        }

        switch (message.getDataType()) {
        case STRING:
            Object[] stringVal = ((ArrayAttribute<String>) message).toArray();
            writeArrayTo(output, stringVal, (o, i) -> o.writeString(CoreSchemaFields.STRING_ARRAY_VALUE.getValue(), i.toString(), false));
            break;
        case INTEGER:
            Object[] integerVal = ((ArrayAttribute<Long>) message).toArray();
            writeArrayTo(output, integerVal, (o, i) -> o.writeInt64(CoreSchemaFields.INTEGER_ARRAY_VALUE.getValue(), (Long) i, false));
            break;
        case NUMBER:
            Object[] numberVal = ((ArrayAttribute<Double>) message).toArray();
            writeArrayTo(output, numberVal, (o, i) -> o.writeDouble(CoreSchemaFields.NUMBER_ARRAY_VALUE.getValue(), (Double) i, false));
            break;
        case DATE:
            Object[] dateVal = ((ArrayAttribute<LocalDate>) message).toArray();
            writeArrayTo(output, dateVal, (o, i) -> o.writeByteArray(CoreSchemaFields.DATE_ARRAY_VALUE.getValue(), ByteUtils.packLocalDate((LocalDate) i), false));
            break;
        case TIME:
            Object[] timeVal = ((ArrayAttribute<LocalTime>) message).toArray();
            writeArrayTo(output, timeVal, (o, i) -> o.writeByteArray(CoreSchemaFields.TIME_ARRAY_VALUE.getValue(), ByteUtils.packLocalTime((LocalTime) i), false));
            break;
        case TIMESTAMP:
            Object[] timestampVal = ((ArrayAttribute<LocalDateTime>) message).toArray();
            writeArrayTo(output, timestampVal, (o, i) -> o.writeByteArray(CoreSchemaFields.TIMESTAMP_ARRAY_VALUE.getValue(), ByteUtils.packLocalDateTime((LocalDateTime) i), false));
            break;
        default:
            break;
        }
    }
}
