package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.*;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.controllers.model.response.*;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.USER_API)
@Slf4j
public class UserController extends AbstractBaseController {

    final UserService userService;
    final UserHelper userHelper;
    final ApplicationConfigureValues appConfig;
    final SessionService sessionService;
    final SessionHelper sessionHelper;

    public UserController(UserService userService, UserHelper userHelper, ApplicationConfigureValues appConfig, SessionService sessionService, SessionHelper sessionHelper) {
        this.userService = userService;
        this.userHelper = userHelper;
        this.appConfig = appConfig;
        this.sessionService = sessionService;
        this.sessionHelper = sessionHelper;
    }


    /**
     * Create User API
     *
     * @param createUserRequest
     * @return
     */
    @AuthorizeValidator(UserType.SYSTEM_ADMIN)
    @PostMapping()
    @Operation(summary = "Create User")
    public ResponseEntity<RestAPIResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest
    ) {
        // Validate Email
        Validator.mustMatch(createUserRequest.getEmail(), Constant.EMAIL_PATTERN, RestAPIStatus.BAD_PARAMS,
                APIStatusMessage.INVALID_EMAIL_FORMAT);
        // check email not exist in database
        User user = userService.getByEmailAndStatusAndType(createUserRequest.getEmail(), AppStatus.ACTIVE,
                createUserRequest.getType());
        Validator.mustNull(user, RestAPIStatus.EXISTED, APIStatusMessage.EMAIL_EXISTED);
        // encrypt password
        String passwordRandom = "";
        String passwordMD5 = "";
        if (createUserRequest.getNewPassword() != null && !createUserRequest.getNewPassword().isEmpty() &&
                createUserRequest.getConfirmNewPassword() != null && !createUserRequest.getConfirmNewPassword().isEmpty()) {
            // check password matching
            if (!createUserRequest.getNewPassword().equals(createUserRequest.getConfirmNewPassword())) {
                throw new ApplicationException(RestAPIStatus.FAIL, APIStatusMessage.PASSWORD_DOES_NOT_MATCH);
            }
            passwordMD5 = createUserRequest.getNewPassword();
        } else {
            // generate password
            passwordRandom = PasswordGenerator.generatePassword(8);
            // encrypt password md5
            try {
                passwordMD5 = AppUtil.encryptMD5(passwordRandom);
            } catch (NoSuchAlgorithmException e) {
                throw new ApplicationException(RestAPIStatus.CANNOT_ENCRYPT_RANDOM_PASSWORD);
            }
        }
        if ((createUserRequest.getPhone() != null && !createUserRequest.getPhone().isEmpty()
                && (createUserRequest.getPhoneDialCode() == null || "".equals(createUserRequest.getPhoneDialCode())))) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.PHONE_DIAL_CODE_MUST_NOT_BE_NULL);
        }
        //create user
        user = userHelper.createUser(createUserRequest, passwordEncoder, passwordMD5);


        return responseUtil.successResponse(user);
    }
}

