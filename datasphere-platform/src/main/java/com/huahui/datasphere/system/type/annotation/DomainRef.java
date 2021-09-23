package com.huahui.datasphere.system.type.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * Messaging domain instance reference.
 * @author Mikhail Mikhailov on Jul 14, 2020
 */
public @interface DomainRef {
    /**
     * Messaging domain name with no default.
     * @return messaging domain name
     */
    String value();
}
