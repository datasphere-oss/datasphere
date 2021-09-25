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
package com.huahui.datasphere.mdm.rest.core.serialization.json;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.huahui.datasphere.mdm.core.type.job.JobParameterType;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobParameterRO;

/**
 * @author theseusyang
 */
public class JobParameterRODeserializer extends JsonDeserializer<JobParameterRO> {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_MULTISELECT = "multi_select";

    @Override
    public JobParameterRO deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {

        final ObjectCodec oc = p.getCodec();
        final JsonNode node = oc.readTree(p);
        final JsonNode idNode = node.get(FIELD_ID);
        final JsonNode nameNode = node.get(FIELD_NAME);
        final JsonNode typeNode = node.get(FIELD_TYPE);
        final JsonNode valueNode = node.get(FIELD_VALUE);
        final JsonNode multiselectNode = node.get(FIELD_MULTISELECT);

        if (nameNode == null) {
            throw new JsonMappingException(ctx.getParser(), "Name node is required.");
        }
        if (typeNode == null) {
            throw new JsonMappingException(ctx.getParser(), "Type node is required.");
        }
        if (valueNode == null && idNode == null) {
            throw new JsonMappingException(ctx.getParser(), "Either Value node OR Range node is required.");
        }

        final JobParameterType type = JobParameterType.fromValue(typeNode.asText());

        final JobParameterRO result = extractValue(type, nameNode.asText(), valueNode, ctx);

        if (result != null && idNode != null) {
            result.setId(idNode.asLong());
        }

        if (multiselectNode != null) {
            result.setMultiSelect(multiselectNode.asBoolean());
        }

        return result;
    }

    /**
     * Extracts value node value.
     *
     * @param type      the value type
     * @param valueNode the value node
     * @param ctx       Deserialization context.
     * @return value of specified type or <code>null</code>.
     */
    private JobParameterRO extractValue(JobParameterType type, final String name, JsonNode valueNode, DeserializationContext ctx)
            throws JsonMappingException {

        if (valueNode.isNull()) {
            return null;
        }

        final JobParameterRO res;

        List<Object> values = new ArrayList<>();

        if (valueNode.isArray()) {
            Iterator<JsonNode> it = ((ArrayNode) valueNode).elements();
            while (it.hasNext()) {
                JsonNode itemNode = it.next();

                values.add(getValue(type, itemNode, ctx));
            }
        } else {
            values.add(getValue(type, valueNode, ctx));
        }

        switch (type) {
            case BOOLEAN:
                res = new JobParameterRO(name, values.toArray(new Boolean[values.size()]));
                break;
            case DATE:
                res = new JobParameterRO(name, values.toArray(new ZonedDateTime[values.size()]));
                break;
            case LONG:
                res = new JobParameterRO(name, values.toArray(new Long[values.size()]));
                break;
            case DOUBLE:
                res = new JobParameterRO(name, values.toArray(new Double[values.size()]));
                break;
            case STRING:
                res = new JobParameterRO(name, values.toArray(new String[values.size()]));
                break;
            default:
                throw new JsonMappingException(ctx.getParser(), "Types other then simple types ["
                        + type.name()
                        + "] aren't supported.");
        }

        return res;
    }

    /**
     * Get value according to it type
     * @param type
     * @param node
     * @return
     * @throws JsonMappingException
     */
    private Object getValue(JobParameterType type, JsonNode node, DeserializationContext ctx) throws JsonMappingException {
        switch (type) {
            case BOOLEAN:
                return node.asBoolean();
            case DATE:
                return ZonedDateTime.parse(node.asText());
            case LONG:
                return node.asLong();
            case DOUBLE:
                return node.asDouble();
            case STRING:
                return node.asText();
            default:
                throw new JsonMappingException(ctx.getParser(),
                        "Types other then simple types [" + type.name() + "] aren't supported.");
        }
    }
}
