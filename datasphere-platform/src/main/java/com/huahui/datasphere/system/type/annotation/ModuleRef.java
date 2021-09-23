package com.huahui.datasphere.system.type.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface ModuleRef {
    /**
     * Default for concrete class. Named for {@link com.huahui.datasphere.system.type.module.Module} interface.
     * @return module's name
     */
    String value() default "";
}
