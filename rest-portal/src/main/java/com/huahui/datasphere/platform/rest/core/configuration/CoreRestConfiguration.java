/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.configuration;

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
import org.unidata.mdm.rest.system.exception.RestExceptionMapper;
import org.unidata.mdm.rest.system.util.OpenApiMetadataFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.huahui.datasphere.platform.rest.core.interceptor.BearerTokenCxfInterceptor;
import com.huahui.datasphere.platform.rest.core.service.AuditSearchRestService;
import com.huahui.datasphere.platform.rest.core.service.ConfigurationRestService;
import com.huahui.datasphere.platform.rest.core.service.CoreRestApplication;
import com.huahui.datasphere.platform.rest.core.service.CustomStorageRestService;
import com.huahui.datasphere.platform.rest.core.service.JobRestService;
import com.huahui.datasphere.platform.rest.core.service.LoginRestService;
import com.huahui.datasphere.platform.rest.core.service.ModuleInfoRestService;
import com.huahui.datasphere.platform.rest.core.service.PipelineRestService;
import com.huahui.datasphere.platform.rest.core.service.RoleRestService;
import com.huahui.datasphere.platform.rest.core.service.SecurityLabelRestService;
import com.huahui.datasphere.platform.rest.core.service.UserLibrariesRestService;
import com.huahui.datasphere.platform.rest.core.service.UserNotificationsRestService;
import com.huahui.datasphere.platform.rest.core.service.UserRestService;
import com.huahui.datasphere.portal.service.SecurityService;
import com.huahui.datasphere.rest.system.service.ReceiveInInterceptor;

/**
 * @author Alexander Malyshev
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
                "org.unidata.mdm.rest.core.service");

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
