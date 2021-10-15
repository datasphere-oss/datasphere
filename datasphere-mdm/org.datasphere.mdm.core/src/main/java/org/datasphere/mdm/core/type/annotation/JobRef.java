package org.datasphere.mdm.core.type.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JobRef {
    /**
     * Job ref with no default value.
     * @return referenced job name
     */
    String value();
}
