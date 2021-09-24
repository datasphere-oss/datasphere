package com.huahui.datasphere.mdm.system.type.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface ConfigurationRef {
    /**
     * Configuration property name with no default.
     * @return configuration property name
     */
    String value();
}
