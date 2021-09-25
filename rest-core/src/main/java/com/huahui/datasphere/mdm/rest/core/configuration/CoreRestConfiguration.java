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
package com.huahui.datasphere.mdm.rest.core.configuration;

import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.huahui.datasphere.mdm.core.service.SecurityService;
import com.huahui.datasphere.mdm.rest.system.service.ReceiveInInterceptor;
import com.huahui.datasphere.mdm.rest.system.util.OpenApiMetadataFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.huahui.datasphere.mdm.rest.core.interceptor.BearerTokenCxfInterceptor;
import com.huahui.datasphere.mdm.rest.core.service.AuditSearchRestService;
import com.huahui.datasphere.mdm.rest.core.service.ConfigurationRestService;
import com.huahui.datasphere.mdm.rest.core.service.CoreRestApplication;
import com.huahui.datasphere.mdm.rest.core.service.CustomStorageRestService;
import com.huahui.datasphere.mdm.rest.core.service.JobRestService;
import com.huahui.datasphere.mdm.rest.core.service.LoginRestService;
import com.huahui.datasphere.mdm.rest.core.service.ModuleInfoRestService;
import com.huahui.datasphere.mdm.rest.core.service.PipelineRestService;
import com.huahui.datasphere.mdm.rest.core.service.RoleRestService;
import com.huahui.datasphere.mdm.rest.core.service.SecurityLabelRestService;
import com.huahui.datasphere.mdm.rest.core.service.UserLibrariesRestService;
import com.huahui.datasphere.mdm.rest.core.service.UserNotificationsRestService;
import com.huahui.datasphere.mdm.rest.core.service.UserRestService;
import com.huahui.datasphere.rest.system.exception.RestExceptionMapper;

/**
 * @author theseusyang
 */
@Configuration
public class CoreRestConfiguration {

    @Bean
    public Application coreRestApplication() {
        return new CoreRestApplication();
    }

    @Bean
    public PipelineRestService pipelineRestService() {
        return new PipelineRestService();
    }

    @Bean
    public LoginRestService loginRestService() {
        return new LoginRestService();
    }

    @Bean
    public RoleRestService roleRestService() {
        return new RoleRestService();
    }

    @Bean
    public SecurityLabelRestService securityLabelRestService() {
        return new SecurityLabelRestService();
    }

    @Bean
    public UserRestService userRestService() {
        return new UserRestService();
    }

    @Bean
    public ConfigurationRestService configurationRestService() {
        return new ConfigurationRestService();
    }

    @Bean
    public CustomStorageRestService customStorageRestService() {
        return new CustomStorageRestService();
    }

    @Bean
    public UserNotificationsRestService userNotificationsRestService() {
        return new UserNotificationsRestService();
    }

    @Bean
    public ModuleInfoRestService moduleInfoRestService() {
        return new ModuleInfoRestService();
    }

    @Bean
    public JobRestService jobRestService() {
        return new JobRestService();
    }

    @Bean
    public AuditSearchRestService auditSearchRestService() {
        return new AuditSearchRestService();
    }

    @Bean
    public UserLibrariesRestService userLibrariesRestService() {
        return new UserLibrariesRestService();
    }

    @Bean
    public BearerTokenCxfInterceptor bearerTokenCxfInterceptor(final SecurityService securityService) {
        return new BearerTokenCxfInterceptor(securityService);
    }

    @Bean
    public Server server(
            final Bus cxf,
            final Application coreRestApplication,
            final JacksonJaxbJsonProvider jacksonJaxbJsonProvider,
            final RestExceptionMapper restExceptionMapper,
            final ReceiveInInterceptor receiveInInterceptor,
            final LoginRestService loginRestService,
            final RoleRestService roleRestService,
            final SecurityLabelRestService securityLabelRestService,
            final UserRestService userRestService,
            final ConfigurationRestService configurationRestService,
            final CustomStorageRestService customStorageRestService,
            final PipelineRestService pipelineRestService,
            final UserNotificationsRestService userNotificationsRestService,
            final JobRestService jobRestService,
            final ModuleInfoRestService moduleInfoRestService,
            final AuditSearchRestService auditSearchRestService,
            final UserLibrariesRestService userLibrariesRestService) {

        JAXRSServerFactoryBean jaxrsServerFactoryBean = RuntimeDelegate.getInstance()
                .createEndpoint(coreRestApplication, JAXRSServerFactoryBean.class);

        final OpenApiFeature coreOpenApiFeature = OpenApiMetadataFactory.openApiFeature(
                "Unidata Core API",
                "Unidata Core REST API operations",
                coreRestApplication, cxf,
                "com.huahui.datasphere.mdm.rest.core.service");

        jaxrsServerFactoryBean.setProviders(Arrays.asList(jacksonJaxbJsonProvider, restExceptionMapper));
        jaxrsServerFactoryBean.setFeatures(Arrays.asList(coreOpenApiFeature));
        jaxrsServerFactoryBean.setInInterceptors(Collections.singletonList(receiveInInterceptor));
        jaxrsServerFactoryBean.setServiceBeans(
                Arrays.asList(
                        loginRestService,
                        roleRestService,
                        securityLabelRestService,
                        userRestService,
                        configurationRestService,
                        customStorageRestService,
                        pipelineRestService,
                        userNotificationsRestService,
                        jobRestService,
                        moduleInfoRestService,
                        auditSearchRestService,
                        userLibrariesRestService
                )
        );

        return jaxrsServerFactoryBean.create();
    }
}
