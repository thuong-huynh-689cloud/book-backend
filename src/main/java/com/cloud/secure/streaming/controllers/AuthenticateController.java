package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.helper.*;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.controllers.model.response.AuthInfoResponse;
import com.cloud.secure.streaming.controllers.model.response.AuthenticateResponse;
import com.cloud.secure.streaming.entities.Session;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.SessionService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.ZoneId;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.AUTHENTICATE_API)
@Slf4j
public class AuthenticateController extends AbstractBaseController {
    final SessionService sessionService;
    final SessionHelper sessionHelper;
    final UserService userService;
    final UserHelper userHelper;

    public AuthenticateController(SessionService sessionService, SessionHelper sessionHelper, UserService userService, UserHelper userHelper) {
        this.sessionService = sessionService;
        this.sessionHelper = sessionHelper;
        this.userService = userService;
        this.userHelper = userHelper;
    }


    /**
     * Sign In API
     *
     * @param authenticateRequest
     * @return
     */
    @PostMapping
    @Operation(summary = "Sign In")
    public ResponseEntity<RestAPIResponse> signIn(
            @RequestBody @Valid AuthenticateRequest authenticateRequest
    ) {
        // get user by email & Type
        User user = userService.getByEmailAndStatusAndType(authenticateRequest.getAuthId().trim(), AppStatus.ACTIVE, authenticateRequest.getUserType());
        Validator.notNull(user, RestAPIStatus.INVALID_AUTHENTICATE_CREDENTIAL, APIStatusMessage.INVALID_AUTHENTICATE_CREDENTIAL);

        // validate password
        if (!passwordEncoder.matches(authenticateRequest.getPasswordHash().trim().concat(user.getPasswordSalt()),
                user.getPasswordHash())) {
            throw new ApplicationException(RestAPIStatus.INVALID_AUTHENTICATE_CREDENTIAL, "");
        }
        // get zoneId
        ZoneId zoneId = DateUtil.getZoneId(authenticateRequest.getZoneId());

        // create session
        Session session = sessionHelper.createSession(user, authenticateRequest.isKeepLogin(), zoneId);
        sessionService.save(session);

        return responseUtil.successResponse(new AuthenticateResponse(session));
    }

    /**
     * Get Auth Info API
     *
     * @param authUser
     * @return
     */
    @GetMapping
    @Operation(summary = "Get Auth Info")
    public ResponseEntity<RestAPIResponse> getAuthInfo(
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        // return auth user info
        return responseUtil.successResponse(new AuthInfoResponse(authUser));
    }

    /**
     * Sign Out API
     *
     * @param
     * @return httpServletRequest
     */
    @DeleteMapping
    @Operation(summary = "Sign Out")
    public ResponseEntity<RestAPIResponse> signOut(
            HttpServletRequest httpServletRequest
    ) {
        String authToken = httpServletRequest.getHeader(Constant.HEADER_TOKEN);
        Session session = sessionService.getById(authToken);
        if (session != null) {
            sessionService.delete(session);
        }
        return responseUtil.successResponse("Ok");
    }
}