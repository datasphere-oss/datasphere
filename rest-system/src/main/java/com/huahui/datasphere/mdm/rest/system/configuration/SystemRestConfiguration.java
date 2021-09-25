package com.huahui.datasphere.mdm.rest.system.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.huahui.datasphere.mdm.system.service.ModularPostProcessingRegistrar;

import com.huahui.datasphere.mdm.rest.system.exception.RestExceptionMapper;
import com.huahui.datasphere.mdm.rest.system.service.ReceiveInInterceptor;
import com.huahui.datasphere.mdm.rest.system.service.impl.ReceiveInInterceptorImpl;

/**
 * @author theseusyang on Apr 24, 2020
 */
@Configuration
public class SystemRestConfiguration {

    @Bean
    public ReceiveInInterceptor receiveInInterceptor(final ModularPostProcessingRegistrar registrar) {
        ReceiveInInterceptorImpl inInterceptor = new ReceiveInInterceptorImpl();
        registrar.registerBeanPostProcessor(inInterceptor);
        return inInterceptor;
    }

    @Bean
    public RestExceptionMapper restExceptionMapper() {
        return new RestExceptionMapper();
    }
}
