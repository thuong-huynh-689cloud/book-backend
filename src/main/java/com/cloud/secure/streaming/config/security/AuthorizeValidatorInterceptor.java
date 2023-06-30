package com.cloud.secure.streaming.config.security;

import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.Constant;
import com.cloud.secure.streaming.controllers.helper.SessionHelper;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.services.SessionService;
import com.cloud.secure.streaming.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 689Cloud
 */
@Aspect
@Component
@Slf4j
public class AuthorizeValidatorInterceptor {
    final UserService userService;
    final SessionService sessionService;
    final SessionHelper sessionHelper;

    public AuthorizeValidatorInterceptor(UserService userService, SessionService sessionService, SessionHelper sessionHelper) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.sessionHelper = sessionHelper;
    }

    @Before(value = "@annotation(com.cloud.secure.streaming.config.security.AuthorizeValidator)  && @annotation(roles)")
    public void before(JoinPoint caller, AuthorizeValidator roles) {
        // Capture access token from current request
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String authToken = httpServletRequest.getHeader(Constant.HEADER_TOKEN);
        if (authToken == null)
            throw new ApplicationException(RestAPIStatus.UNAUTHORIZED);
        // Check Get current Authenticate user from access token
        AuthUser authUser = sessionHelper.getAuthUserByAuthToken(authToken, sessionService, userService);
        if (authUser == null)
            throw new ApplicationException(RestAPIStatus.UNAUTHORIZED);
        // Validate Role
        boolean isValid = isValidate(authUser, roles);
        if (!isValid)
            throw new ApplicationException(RestAPIStatus.FORBIDDEN);
    }

    public boolean isValidate(AuthUser authUser, AuthorizeValidator types) {
        for (UserType type : types.value()) {
            if(type == authUser.getType())
                return true;
        }

        return false;
    }
}
