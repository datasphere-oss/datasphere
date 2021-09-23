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

package com.huahui.datasphere.system.serialization.protostuff;

import java.io.IOException;

import com.huahui.datasphere.system.serialization.SerializedBooleanVariable;
import com.huahui.datasphere.system.serialization.SerializedDateVariable;
import com.huahui.datasphere.system.serialization.SerializedDoubleVariable;
import com.huahui.datasphere.system.serialization.SerializedFloatVariable;
import com.huahui.datasphere.system.serialization.SerializedInstantVariable;
import com.huahui.datasphere.system.serialization.SerializedIntegerVariable;
import com.huahui.datasphere.system.serialization.SerializedLongVariable;
import com.huahui.datasphere.system.serialization.SerializedStringVariable;
import com.huahui.datasphere.system.serialization.SerializedTimeVariable;
import com.huahui.datasphere.system.serialization.SerializedTimestampVariable;
import com.huahui.datasphere.system.type.variables.Variable;
import com.huahui.datasphere.system.type.variables.Variables;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author Mikhail Mikhailov
 * Date record schema.
 */
public class VariablesSchema implements Schema<Variables> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName(int number) {
        return SystemSchemaFields.intToString(number);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getFieldNumber(String name) {
        return SystemSchemaFields.stringToInt(name);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized(Variables message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Variables newMessage() {
        return new Variables();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return Variables.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return Variables.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Variables> typeClass() {
        return Variables.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, Variables message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case SystemFieldValues.STRING_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedStringVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.INTEGER_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedIntegerVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.LONG_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedLongVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.FLOAT_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedFloatVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.DOUBLE_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedDoubleVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.BOOLEAN_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedBooleanVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.DATE_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedDateVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.TIME_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedTimeVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.TIMESTAMP_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedTimestampVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.INSTANT_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedInstantVariable(), SystemSchemas.VARIABLE_SCHEMA));
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
    public void writeTo(Output output, Variables message) throws IOException {

        for (Variable<?> v : message) {

            switch (v.getType()) {
            case INTEGER:
                output.writeObject(SystemSchemaFields.INTEGER_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case LONG:
                output.writeObject(SystemSchemaFields.LONG_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case BOOLEAN:
                output.writeObject(SystemSchemaFields.BOOLEAN_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case FLOAT:
                output.writeObject(SystemSchemaFields.FLOAT_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case DOUBLE:
                output.writeObject(SystemSchemaFields.DOUBLE_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case STRING:
                output.writeObject(SystemSchemaFields.STRING_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case DATE:
                output.writeObject(SystemSchemaFields.DATE_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case TIME:
                output.writeObject(SystemSchemaFields.TIME_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case TIMESTAMP:
                output.writeObject(SystemSchemaFields.TIMESTAMP_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case INSTANT:
                output.writeObject(SystemSchemaFields.INSTANT_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            default:
                break;
            }
        }
    }
}
