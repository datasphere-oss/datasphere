package org.unidata.mdm.rest.system.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unidata.mdm.rest.system.exception.RestExceptionMapper;
import org.unidata.mdm.system.service.ModularPostProcessingRegistrar;

import com.huahui.datasphere.rest.system.service.ReceiveInInterceptor;
import com.huahui.datasphere.rest.system.service.impl.ReceiveInInterceptorImpl;

/**
 * @author Mikhail Mikhailov on Apr 24, 2020
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
