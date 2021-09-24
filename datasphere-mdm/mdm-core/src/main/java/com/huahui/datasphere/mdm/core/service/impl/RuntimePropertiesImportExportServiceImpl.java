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

package com.huahui.datasphere.mdm.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.huahui.datasphere.mdm.system.service.RuntimePropertiesService;
import com.huahui.datasphere.mdm.system.service.impl.RuntimePropertiesServiceImpl;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.mdm.system.util.TextUtils;

import com.huahui.datasphere.mdm.core.context.UpsertLargeObjectContext;
import com.huahui.datasphere.mdm.core.context.UpsertUserEventRequestContext;
import com.huahui.datasphere.mdm.core.dto.UserEventDTO;
import com.huahui.datasphere.mdm.core.service.LargeObjectsService;
import com.huahui.datasphere.mdm.core.service.RuntimePropertiesImportExportService;
import com.huahui.datasphere.mdm.core.service.UserService;
import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * @author theseusyang
 */
@Service
public class RuntimePropertiesImportExportServiceImpl implements RuntimePropertiesImportExportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimePropertiesImportExportServiceImpl.class);

    private static final String CONFIG_EXPORT_SUCCESS = "app.user.events.export.config.success";
    private static final String CONFIG_EXPORT_FAIL = "app.user.events.export.config.fail";
    private static final String CONFIG_IMPORT_SUCCESS = "app.user.events.import.config.success";
    private static final String CONFIG_IMPORT_FAIL = "app.user.events.import.config.fail";

    private final RuntimePropertiesService runtimePropertiesService;

    private final UserService userService;

    private final LargeObjectsService largeObjectsServiceComponent;

    public RuntimePropertiesImportExportServiceImpl(
            final RuntimePropertiesService runtimePropertiesService,
            final UserService userService,
            final LargeObjectsService largeObjectsServiceComponent) {
        this.runtimePropertiesService = runtimePropertiesService;
        this.userService = userService;
        this.largeObjectsServiceComponent = largeObjectsServiceComponent;
    }

    @Override
    public void exportProperties() {
        final String data = runtimePropertiesService.getAll().stream()
                .collect(Collectors.groupingBy(p -> p.getProperty().getGroupKey()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(this::generateGroupString)
                .collect(Collectors.joining("\n"));
        sendExportDataToUser(data, SecurityUtils.getCurrentUserName());
    }

    private String generateGroupString(Map.Entry<String, List<ConfigurationValue<?>>> e) {
        return "# " + e.getKey() + "\n" + e.getValue().stream()
                .sorted(Comparator.comparing(p -> p.getProperty().getKey()))
                .map(this::generatePropertyString)
                .collect(Collectors.joining("\n"));
    }

    private String generatePropertyString(ConfigurationValue<?> p) {
        return "## " + TextUtils.getText(p.getProperty().getKey()) + "\n"
                + p.getProperty().getKey() + "="
                + (p.getValue() == null ? RuntimePropertiesServiceImpl.NIL : p.getValue());
    }

    private void sendExportDataToUser(
            final String data,
            final String currentUserName
    ) {
        final UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder configExportUserEvent =
                new UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder()
                        .login(currentUserName)
                        .type("CONFIG_EXPORT");
        try (final InputStream is = new ByteArrayInputStream(data.getBytes())) {
            final UpsertUserEventRequestContext upsertUserEventRequestContext =
                    configExportUserEvent
                            .content(TextUtils.getText(CONFIG_EXPORT_SUCCESS))
                            .build();
            final UserEventDTO userEventDTO = userService.upsert(upsertUserEventRequestContext);
            final UpsertLargeObjectContext ctx = UpsertLargeObjectContext.builder()
                            .subjectId(userEventDTO.getId())
                            .mimeType("text/plain")
                            .binary(false)
                            .input(is)
                            .filename(fileName())
                            .acceptance(LargeObjectAcceptance.ACCEPTED)
                            .build();
            largeObjectsServiceComponent.saveLargeObject(ctx);
        } catch (IOException e) {
            LOGGER.error("Can't export backend configuration file", e);
            final UpsertUserEventRequestContext upsertUserEventRequestContext =
                    configExportUserEvent
                            .content(TextUtils.getText(CONFIG_EXPORT_FAIL))
                            .build();
            userService.upsert(upsertUserEventRequestContext);
        }
    }

    private String fileName() {
        try {
            return URLEncoder.encode(
                    "config_"
                            + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd_HH-mm-ss")
                            + ".properties",
                    StandardCharsets.UTF_8.name()
            );
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error generating properties file name", e);
        }
        return "backend.properties";
    }

    @Override
    public void importProperties(Path path) {
        final String currentUserName = SecurityUtils.getCurrentUserName();
        final UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder configImportUserEvent =
                new UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder()
                        .login(currentUserName)
                        .type("CONFIG_IMPORT");
        final UpsertUserEventRequestContext upsertUserEventRequestContext =
                configImportUserEvent
                        .content(TextUtils.getText(importFile(path)))
                        .build();
        userService.upsert(upsertUserEventRequestContext);
    }

    private String importFile(Path path) {
        final Properties properties = new Properties();
        try (final InputStream inputStream = new FileInputStream(path.toFile())) {
            properties.load(inputStream);
            runtimePropertiesService.update(
                    properties.entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            e -> e.getKey().toString(),
                                            e -> e.getValue().toString()
                                    )
                            )
            );
            return CONFIG_IMPORT_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Can't import backend configuration file", e);
            return CONFIG_IMPORT_FAIL;
        }
    }
}
