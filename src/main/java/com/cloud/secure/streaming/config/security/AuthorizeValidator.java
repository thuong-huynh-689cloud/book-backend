package com.cloud.secure.streaming.config.security;


import com.cloud.secure.streaming.common.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 689Cloud
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeValidator {
    UserRole[] value() default UserRole.ADMIN;
}
