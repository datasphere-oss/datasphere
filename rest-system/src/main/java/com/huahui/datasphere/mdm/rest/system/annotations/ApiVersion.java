package com.huahui.datasphere.mdm.rest.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The version of the api used.
 * This annotation must be annotated with the service class or the method that implements the service function.
 * Based on the received version, a rule for describing composite objects will be determined
 *
 * @author theseusyang
 * @since 29.09.2020
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiVersion {

    String CURRENT_VERSION = "current";

    String value() default CURRENT_VERSION;
}
