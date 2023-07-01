package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.helper.SessionHelper;
import com.cloud.secure.streaming.controllers.helper.UserHelper;
import com.cloud.secure.streaming.controllers.model.request.AuthenticateRequest;
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

    public AuthenticateController(SessionService sessionService, SessionHelper sessionHelper,
                                  UserService userService, UserHelper userHelper) {
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
        User user = userService.getByEmailAndStatusAndType(authenticateRequest.getAuthId().trim(), Status.ACTIVE, authenticateRequest.getUserRole());
        Validator.notNull(user, RestAPIStatus.INVALID_AUTHENTICATE_CREDENTIAL, "INVALID_AUTHENTICATE_CREDENTIAL");

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

//    /**
//     * Request Reset Password API
//     *
//     * @param requestResetPassword
//     * @return
//     */
//    @PostMapping(path = "/reset-password")
//    @Operation(summary = "Request Reset Password")
//    public ResponseEntity<RestAPIResponse> requestResetPassword(
//            @RequestBody @Valid RequestResetPassword requestResetPassword
//    ) {
//        // get user by email and type
//        User user = userService.getByEmailAndStatusAndType(requestResetPassword.getAuthId(),AppStatus.ACTIVE, requestResetPassword.getUserType());
//        if (user != null) {
//            // delete old codes
//            codeService.deleteByUserIdAndType(user.getId(), CodeType.RESET_PASSWORD);
//            // create code
//            Code resetCode = codeHelper.createCode(user, CodeType.RESET_PASSWORD);
//            // save
//            codeService.save(resetCode);
//            // Send Notification (Email/SMS)
//            emailSenderHelper.sendEmailResetPassword(user.getEmail(), user.getType(), user.getName(), resetCode.getId(), requestResetPassword.getLanguage());
//        }
//        return responseUtil.successResponse("OK");
//    }
//
//    /**
//     * Reset Password API
//     *
//     * @param code
//     * @param resetPasswordRequest
//     * @return
//     */
//    @PutMapping(path = "/reset-password/{code}")
//    @Operation(summary = "Reset Password")
//    public ResponseEntity<RestAPIResponse> resetPassword(
//            @PathVariable(name = "code") String code,
//            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest
//    ) {
//        // Get code
//        Code resetCode = codeService.getByIdAndType(code, CodeType.RESET_PASSWORD);
//        Validator.notNull(resetCode, RestAPIStatus.INVALID_AUTHENTICATE_CREDENTIAL, APIStatusMessage.RESET_CODE_NOT_FOUND);
//        // Validate confirm password must equal with new password
//        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
//            throw new ApplicationException(RestAPIStatus.BAD_PARAMS,APIStatusMessage.CONFIRM_PASSWORD_DOES_NOT_MATCH);
//        }
//        // get user by id
//        User user = userService.getById(resetCode.getUserId());
//        Validator.notNull(user, RestAPIStatus.INVALID_AUTHENTICATE_CREDENTIAL, APIStatusMessage.USER_NOT_FOUND);
//
//        // Validate new password must not match with old password
//        if (passwordEncoder.matches(resetPasswordRequest.getNewPassword().trim().concat(user.getPasswordSalt()),
//                user.getPasswordHash())) {
//            throw new ApplicationException(RestAPIStatus.BAD_PARAMS,APIStatusMessage.NEW_PASSWORD_CANNOT_THE_SAME_WITH_THE_OLD_ONE);
//        }
//        // Delete code
//        int isSuccess = codeService.deleteCheckDateAndType(code, DateUtil.convertToUTC(new Date()), CodeType.RESET_PASSWORD);
//        // compare newDay vs expirationDate
//        Validator.mustGreaterThan(0, RestAPIStatus.EXPIRED,APIStatusMessage.THE_LINK_IS_EXPIRED, isSuccess);
//        // generate new password hash
//        String newPasswordHash = passwordEncoder.encode(resetPasswordRequest.getNewPassword()
//                .trim().concat(user.getPasswordSalt()));
//        user.setPasswordHash(newPasswordHash);
//        userService.save(user);
//        // delete old session
//        sessionService.deleteByUserId(user.getId());
//        return responseUtil.successResponse("OK");
//    }
//
//    /**
//     * Change Password API
//     *
//     * @param changePasswordRequest
//     * @param authUser
//     * @return
//     */
//    @PutMapping(path = "/change-password")
//    @Operation(summary = "Change Password")
//    public ResponseEntity<RestAPIResponse> changePassword(
//            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
//            @Parameter(hidden = true) @AuthSession AuthUser authUser
//    ) {
//        //get user by authUser
//        User user = userService.getByIdAndStatus(authUser.getId(), AppStatus.ACTIVE);
//        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
//        // Check current (old) password
//        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword().trim().concat(user.getPasswordSalt()),
//                user.getPasswordHash())) {
//            throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.CURRENT_PASSWORD_IS_INCORRECT);
//        }
//        // Validate new password
//        if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getOldPassword())) {
//            throw new ApplicationException(RestAPIStatus.BAD_PARAMS,APIStatusMessage.NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_PASSWORD);
//        }
//        // Validate confirm new password
//        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
//            throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.CONFIRM_PASSWORD_DOES_NOT_FOUND);
//        }
//        // Set new password
//        String newSalt = AppUtil.generateSalt();
//        user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword().trim().concat(newSalt)));
//        user.setPasswordSalt(newSalt);
//        userService.save(user);
//        // Send Notification (Email/SMS)
//        emailSenderHelper.sendEmailChangePassword(user.getEmail(), user.getType(), user.getName(), changePasswordRequest.getLanguage());
//
//        return responseUtil.successResponse("OK");
//    }
//
//    /**
//     * Request Change Email API
//     *
//     * @param authUser
//     * @return
//     */
//    @PostMapping(path = "/change-email")
//    @Operation(summary = "Request Change Email")
//    public ResponseEntity<RestAPIResponse> requestChangeEmail(
//            @Parameter(hidden = true) @AuthSession AuthUser authUser,
//            @Valid @RequestBody RequestChangeEmail requestChangeEmail
//    ) {
//        // get user by email and type
//        User user = userService.getById(authUser.getId());
//        if (user != null) {
//            // delete old codes
//            codeService.deleteByUserIdAndType(user.getId(), CodeType.CHANGE_EMAIL);
//            // create code
//            Code changeEmailCode = codeHelper.createCode(user, CodeType.CHANGE_EMAIL);
//            // save code simple
//            codeService.save(changeEmailCode);
//
//            // Send Notification (Email/SMS)
//            emailSenderHelper.sendChangeEmailRequest(user.getEmail(), user.getType(), user.getName(), changeEmailCode.getId(), requestChangeEmail.getLanguage());
//        }
//        return responseUtil.successResponse("OK");
//    }
//
//    /**
//     * Change Email API
//     *
//     * @param code
//     * @param changeEmailRequest
//     * @return
//     */
//    @PutMapping(path = "/change-email/{code}")
//    @Operation(summary = "Change Email")
//    public ResponseEntity<RestAPIResponse> changeEmail(
//            @PathVariable(name = "code") String code,
//            @Valid @RequestBody ChangeEmailRequest changeEmailRequest
//    ) {
//        // check email format
//        Validator.mustMatch(changeEmailRequest.getEmail(), Constant.EMAIL_PATTERN, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_EMAIL_FORMAT);
//        // Get code
//        Code changeEmailCode = codeService.getByIdAndType(code, CodeType.CHANGE_EMAIL);
//        Validator.notNull(changeEmailCode, RestAPIStatus.UNAUTHORIZED, APIStatusMessage.INVALID_CONFIRM_CODE_OR_CONFIRM_CODE_IS_EXPIRED);
//
//        // Get user
//        User user = userService.getByIdAndStatus(changeEmailCode.getUserId(), AppStatus.ACTIVE);
//        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
//
//        // Validate email already exists
//        User existedUser = userService.getByEmailAndStatusAndType(changeEmailRequest.getEmail(), AppStatus.ACTIVE, user.getType());
//        Validator.mustNull(existedUser, RestAPIStatus.EXISTED, APIStatusMessage.EMAIL_EXISTED);
//
//        // delete code
//        int isSuccess = codeService.deleteCheckDateAndType(code, new Date(), CodeType.CHANGE_EMAIL);
//        // compare newDay vs expirationDate
//        Validator.mustGreaterThan(0, RestAPIStatus.EXPIRED, APIStatusMessage.THE_LINK_IS_EXPIRED, isSuccess);
//
//        // change email
//        user.setEmail(changeEmailRequest.getEmail());
//        userService.save(user);
//
//        // Log out after changed email
//        // delete old session
//        sessionService.deleteByUserId(user.getId());
//
//        return responseUtil.successResponse("OK");
//    }
//
//    /**
//     * Verify Account API
//     *
//     * @param
//     * @return
//     */
//    @AuthorizeValidator(UserType.SYSTEM_ADMIN)
//    @PutMapping(path = "/verify/{code}")
//    @Operation(summary = "Verify Account")
//    public ResponseEntity<RestAPIResponse> verifyAccount(
//            @PathVariable(name = "code") String code
//    ) {
//        //Get code
//        Code verifyCode = codeService.getByIdAndType(code, CodeType.VERIFY_ACCOUNT);
//        // validate reset code must not be null
//        Validator.notNull(verifyCode, RestAPIStatus.NOT_FOUND, APIStatusMessage.CODE_NOT_FOUND);
//        //Get user
//        User user = userService.getByIdAndStatus(verifyCode.getUserId(), AppStatus.PENDING);
//        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
//        //set status to active
//        user.setStatus(AppStatus.ACTIVE);
//        //delete verify code
//        codeService.delete(verifyCode);
//        //save
//        userService.save(user);
//
//        return responseUtil.successResponse("OK");
//    }
//
//    /**
//     * API Login by Social Account
//     *
//     * @param socialLoginRequest
//     * @return
//     */
////    @AuthorizeValidator({UserType.INSTRUCTOR,UserType.LEARNER})
//    @PostMapping(path = "/social-login")
//    @Operation(summary = "Social Login")
//    public ResponseEntity<RestAPIResponse> socialLogin(
//            @RequestBody @Valid SocialLoginRequest socialLoginRequest
//    ) {
//
//        User user = null;
//        switch (socialLoginRequest.getSocialType()) {
//            case GOOGLE:  // sign in with google
//                AuthenticationModel google = socialHelper.createAuthModelGoogle(socialLoginRequest.getTokenId().trim(), applicationConfigureValues.GOOGLE_CLIENT_ID);
//
//                  user = userService.getByGoogleIdAndStatus(google.getGoogleId(),AppStatus.ACTIVE);
//                    if (user == null) {
//                        user = userService.getByEmail(google.getEmail());
//                        if (user != null) {
//                            user.setGoogleId(google.getGoogleId());
//                        } else {
//                            user = socialHelper.mappingGoogleInfoWith(google);
//                        }
//
//                    userService.save(user);
//
//                } else {
//                     user = userService.getByGoogleIdAndStatus(google.getGoogleId(),AppStatus.INACTIVE);
//                    if (user == null) {
//                        user = userService.getByEmail(google.getEmail());
//                        if (user != null) {
//                            user.setGoogleId(google.getGoogleId());
//                        } else {
//                            user = socialHelper.mappingGoogleInfoWith(google);
//                        }
//                    }
//
//                    userService.save(user);
//                }
//                break;
//            case FACEBOOK:
//
//                AuthenticationModel facebook = socialHelper.createAuthModelFB(socialLoginRequest.getTokenId().trim(), applicationConfigureValues.FACEBOOK_CLIENT_ID);
//
//                     user = userService.getByFacebookId(facebook.getFacebookId());
//                    if (user == null) {
//                        user = userService.getByEmail(facebook.getEmail());
//                        if (user != null) {
//                            user.setFacebookId(facebook.getFacebookId());
//                        } else {
//                            user = socialHelper.mappingFacebookInfoWith(facebook);
//                        }
//                    userService.save(user);
//
//                } else {
//                     user = userService.getByFacebookId(facebook.getFacebookId());
//                    if (user == null) {
//                        user = userService.getByEmail(facebook.getEmail());
//                        if (user != null) {
//                            user.setFacebookId(facebook.getFacebookId());
//                        } else {
//                            user = socialHelper.mappingFacebookInfoWith(facebook);
//                        }
//                    }
//                    userService.save(user);
//                }
//                break;
//            default:
//                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.INVALID_SOCIAL_TYPE);
//        }
//
//        ZoneId zoneId = DateUtil.getZoneId(socialLoginRequest.getTimezone());
//        // create session
//        Session session = sessionHelper.createSession(user, true,  zoneId);
//        sessionService.save(session);
//        return responseUtil.successResponse(new LoginResponse(session.getId(), session.getExpiryDate().getTime()));
//    }

}