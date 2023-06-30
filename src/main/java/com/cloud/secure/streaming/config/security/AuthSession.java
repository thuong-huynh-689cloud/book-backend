package com.cloud.secure.streaming.config.security;

import java.lang.annotation.*;

/**
 * @author 689Cloud
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface AuthSession {
    /**
     * If true then throw ApplicationException when access token header not
     * found
     *
     * @return
     */
    boolean required() default true;

}
