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

package com.huahui.datasphere.system.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidata.mdm.system.configuration.SystemConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mikhail Mikhailov
 * JSON related stuff.
 */
public class JsonUtils {

    public static final String DEFAULT_OBJECT_MAPPER_BEAN_NAME = "objectMapper";
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    /**
     * Default object mapper.
     */
    private static ObjectMapper objectMapper;
    /**
     * Constructor.
     */
    private JsonUtils() {
        super();
    }
    /**
     * Convenient init method.
     */
    public static void init() {
        try {
            objectMapper = SystemConfiguration.getBean(ObjectMapper.class); // From WAR spring configuration.
        } catch (Exception exc) {
            LOGGER.warn("Platform configuration bean GET. Exception caught.", exc);
        }
    }
    /**
     * Gets the mapper.
     * @return the mapper.
     */
    public static ObjectMapper getMapper() {
        return objectMapper;
    }
    /**
     * Reads a value from a {@link TreeNode} using supplied target class for target instantiation.
     * This can be used with delayed reads of unknown properties in a JSON tree.
     * @param <T> resulting type
     * @param n the node
     * @param klass resulting class
     * @return value or null
     */
    public static<T> T read(TreeNode n, Class<T> klass) {

        Objects.requireNonNull(n, "TreeNode must not be null");
        Objects.requireNonNull(klass, "Target class type must not be null");

        try {

            // Handle null values, as Jackson itself does it.
            final JsonToken token = n.asToken();
            if (token == JsonToken.VALUE_NULL) {
                return null;
            }

            return objectMapper.readValue(objectMapper.treeAsTokens(n), klass);
        } catch (JsonParseException e) {
            LOGGER.warn("Caught a 'JsonParseException' while reading tree node with type class value.", e);
        } catch (JsonMappingException e) {
            LOGGER.warn("Caught a 'JsonMappingException' while reading tree node with type class value.", e);
        } catch (IOException e) {
            LOGGER.warn("Caught a 'IOException' while reading typed tree node with type class value.", e);
        }

        return null;
    }
    /**
     * Reads a value from a {@link TreeNode} using supplied type reference for target instantiation.
     * This can be used with delayed reads of unknown properties in a JSON tree.
     * @param <T> resulting type
     * @param n the node
     * @param ref resulting class
     * @return value or null
     */
    public static<T> T read(TreeNode n, TypeReference<T> ref) {

        Objects.requireNonNull(n, "TreeNode must not be null");
        Objects.requireNonNull(ref, "Target type reference must not be null");

        try {

            // Handle null values, as Jackson itself does it.
            final JsonToken token = n.asToken();
            if (token == JsonToken.VALUE_NULL) {
                return null;
            }

            return objectMapper.readValue(objectMapper.treeAsTokens(n), ref);
        } catch (JsonParseException e) {
            LOGGER.warn("Caught a 'JsonParseException' while reading tree node with type reference value.", e);
        } catch (JsonMappingException e) {
            LOGGER.warn("Caught a 'JsonMappingException' while reading tree node with type reference value.", e);
        } catch (IOException e) {
            LOGGER.warn("Caught a 'IOException' while reading tree node with type reference value.", e);
        }

        return null;
    }
    /**
     * Reads a value from a string.
     * @param json the JASON string.
     * @param klass the target class
     * @return object or null
     */
    public static<T> T read(String json, Class<T> klass) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return objectMapper.readValue(json, klass);
            } catch (JsonParseException e) {
                LOGGER.warn("Caught a 'JsonParseException' while reading string with type class value.", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("Caught a 'JsonMappingException' while reading string with type class value.", e);
            } catch (IOException e) {
                LOGGER.warn("Caught a 'IOException' while reading string with type class value.", e);
            }
        }

        return null;
    }
    /**
     * Reads a value from a string.
     * @param json the JSON string.
     * @param ref the target ref
     * @return object or null
     */
    public static<T> T read(String json, TypeReference<T> ref) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return objectMapper.readValue(json, ref);
            } catch (JsonParseException e) {
                LOGGER.warn("Caught a 'JsonParseException' while reading string with type reference value.", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("Caught a 'JsonMappingException' while reading string with type reference value.", e);
            } catch (IOException e) {
                LOGGER.warn("Caught a 'IOException' while reading string with type reference value.", e);
            }
        }

        return null;
    }

    /**
     * Reads a value from a file.
     * @param json the JSON FileInputStream.
     * @param klass the target class
     * @return object or null
     */
    public static<T> T read(InputStream json, Class<T> klass) {
        if (Objects.nonNull(json)) {
            try {
                return objectMapper.readValue(json, klass);
            } catch (JsonParseException e) {
                LOGGER.warn("Caught a 'JsonParseException' while reading FIS with type class value.", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("Caught a 'JsonMappingException' while reading FIS with type class value.", e);
            } catch (IOException e) {
                LOGGER.warn("Caught a 'IOException' while reading FIS with type class value.", e);
            }
        }

        return null;
    }

    /**
     * Reads a value from a file.
     * @param json the JASON file.
     * @param ref the target ref
     * @return object or null
     */
    public static<T> T read(InputStream json, TypeReference<T> ref) {
        if (Objects.nonNull(json)) {
            try {
                return objectMapper.readValue(json, ref);
            } catch (JsonParseException e) {
                LOGGER.warn("Caught a 'JsonParseException' while reading FIS with type reference value.", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("Caught a 'JsonMappingException' while reading FIS with type reference value.", e);
            } catch (IOException e) {
                LOGGER.warn("Caught a 'IOException' while reading FIS with type reference value.", e);
            }
        }

        return null;
    }
    /**
     * Reads a value from a file.
     * @param json the JSON file.
     * @param klass the target class
     * @return object or null
     */
    public static<T> T read(File json, Class<T> klass) {
        if (Objects.nonNull(json) && json.exists()) {
            try {
                return objectMapper.readValue(json, klass);
            } catch (JsonParseException e) {
                LOGGER.warn("Caught a 'JsonParseException' while reading file with type class value.", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("Caught a 'JsonMappingException' while reading file with type class value.", e);
            } catch (IOException e) {
                LOGGER.warn("Caught a 'IOException' while reading file with type class value.", e);
            }
        }

        return null;
    }

    /**
     * Reads a value from a file.
     * @param json the JSON file.
     * @param ref the target ref
     * @return object or null
     */
    public static<T> T read(File json, TypeReference<T> ref) {
        if (Objects.nonNull(json) && json.exists()) {
            try {
                return objectMapper.readValue(json, ref);
            } catch (JsonParseException e) {
                LOGGER.warn("Caught a 'JsonParseException' while reading file with type reference value.", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("Caught a 'JsonMappingException' while reading file with type reference value.", e);
            } catch (IOException e) {
                LOGGER.warn("Caught a 'IOException' while reading file with type reference value.", e);
            }
        }

        return null;
    }
    public static JsonNode writeNode(Object value) {
        try {
            return objectMapper.valueToTree(value);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Caught a 'IllegalArgumentException' while writing value to node.", e);
        }

        return null;
    }

    /**
     * Writes a value as string.
     * @param value the value to write.
     * @return string
     */
    public static String write(Object value) {
        if (Objects.nonNull(value)) {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                LOGGER.warn("Caught a 'JsonProcessingException' while writing value.", e);
            }
        }
        return null;
    }
    /**
     * Writes a collection with empty check as string.
     * @param collection the collection to write
     * @return string
     */
    public static String write(Collection<?> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            try {
                return objectMapper.writeValueAsString(collection);
            } catch (JsonProcessingException e) {
                LOGGER.warn("Caught a 'JsonProcessingException' while writing collection value.", e);
            }
        }
        return null;
    }
    /**
     * Writes a map with empty check as string.
     * @param map the map to write
     * @return string
     */
    public static String write(Map<?, ?> map) {
        if (MapUtils.isNotEmpty(map)) {
            try {
                return objectMapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                LOGGER.warn("Caught a 'JsonProcessingException' while writing map value.", e);
            }
        }
        return null;
    }
}
