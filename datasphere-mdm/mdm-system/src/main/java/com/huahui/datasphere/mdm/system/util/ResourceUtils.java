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

package com.huahui.datasphere.mdm.system.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.huahui.datasphere.mdm.system.configuration.SystemConfiguration;

/**
 * @author theseusyang on Apr 9, 2020
 * Public methods from this class load resources in Spring {@link Resource} fashion,
 * supporting file:, http:, jar:, classpath: or plain path schemas.
 */
public final class ResourceUtils {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtils.class);
    /**
     * The app context, which actually loads resources.
     */
    private static ApplicationContext applicationContext;
    /**
     * Constructor.
     */
    private ResourceUtils() {
        super();
    }
    /**
     * Static init method.
     */
    public static void init() {
        try {
            applicationContext = SystemConfiguration.getApplicationContext();
        } catch (Exception exc) {
            LOGGER.warn("Platform configuration AC GET. Exception caught.", exc);
        }
    }
    /**
     * File content from path as UTF8 encoded string.
     * @param path the resource path
     * @return content as string or null
     */
    @Nullable
    public static String asString(String path) {

        InputStream[] result = loadResources(path);
        if (result.length != 1  || result[0] == null) {
            return null;
        }

        try (InputStream is = result[0]) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Error while reading {}.", path, e);
            return null;
        }
    }
    /**
     * Content of several files from paths as UTF8 encoded strings.
     * @param paths the resource paths
     * @return content as strings or empty array
     */
    public static String[] asStrings(String... paths) {

        InputStream[] streams = loadResources(paths);
        String[] result = new String[streams.length];
        for (int i = 0; i < streams.length; i++) {

            InputStream is = streams[i];
            if (Objects.isNull(is)) {
                continue;
            }

            try (is) {
                result[i] = IOUtils.toString(is, StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error("Error while reading resource.", e);
            }
        }

        return result;
    }
    /**
     * Gets content of a resource as input stream.
     * Toy will have to close it after use.
     * @param path the path
     * @return input stream or null
     */
    @Nullable
    public static InputStream asInputStream(String path) {

        InputStream[] result = loadResources(path);
        if (result.length != 1  || result[0] == null) {
            return null;
        }

        return result[0];
    }
    /**
     * Gets content of multiple resources as input streams.
     * Toy will have to close them after use.
     * @param paths the paths
     * @return streams array
     */
    public static InputStream[] asInputStreams(String... paths) {
        return loadResources(paths);
    }

    private static InputStream[] loadResources(String... names) {

        Resource[] raw = Arrays.stream(names)
                .map(applicationContext::getResource)
                .toArray(Resource[]::new);

        if (raw.length > 0) {
            return Stream.of(raw)
                    .map(r -> {
                        try {
                            return r.getInputStream();
                        } catch (IOException ioe) {
                            LOGGER.error("Failed to load resource {}.", r.getFilename(), ioe);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toArray(InputStream[]::new);
        }

        return (InputStream[]) ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
}
