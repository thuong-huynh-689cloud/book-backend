//package com.cloud.secure.streaming.controllers;
//
//import com.cloud.secure.streaming.common.enums.ResetCodeType;
//import com.cloud.secure.streaming.common.exceptions.ApplicationException;
//import com.cloud.secure.streaming.common.utilities.*;
//import com.cloud.secure.streaming.config.security.AuthSession;
//import com.cloud.secure.streaming.config.security.AuthUser;
//import com.cloud.secure.streaming.controllers.ApiPath;
//import com.cloud.secure.streaming.controllers.helper.ResetCodeHelper;
//import com.cloud.secure.streaming.controllers.helper.SessionHelper;
//import com.cloud.secure.streaming.controllers.helper.UserHelper;
//import com.cloud.secure.streaming.controllers.model.request.ChangePasswordRequest;
//import com.cloud.secure.streaming.controllers.model.request.CreateAuthenticateRequest;
//import com.cloud.secure.streaming.controllers.model.request.ForgotPasswordRequest;
//import com.cloud.secure.streaming.controllers.model.request.ResetPasswordRequest;
//import com.cloud.secure.streaming.entities.ResetCode;
//import com.cloud.secure.streaming.entities.Session;
//import com.cloud.secure.streaming.entities.User;
//import com.cloud.secure.streaming.services.ResetCodeService;
//import com.cloud.secure.streaming.services.SessionService;
//import com.cloud.secure.streaming.services.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.Date;
//import java.util.List;
//
//
///**
// * @author 689Cloud
// */
//@RestController
//@RequestMapping(ApiPath.AUTHENTICATE_API)
//@Slf4j
//public class AuthenticateController extends AbstractBaseController {
//
//    final UserService userService;
//    final UserHelper userHelper;
//    final SessionService sessionService;
//    final SessionHelper sessionHelper;
//    final PasswordEncoder passwordEncoder;
//    final ResetCodeHelper resetCodeHelper;
//    final ResetCodeService resetCodeService;
//
//    public AuthenticateController(UserService userService, UserHelper userHelper, SessionService sessionService,
//                                  SessionHelper sessionHelper, PasswordEncoder passwordEncoder, ResetCodeHelper resetCodeHelper, ResetCodeService resetCodeService) {
//        this.userService = userService;
//        this.userHelper = userHelper;
//        this.sessionService = sessionService;
//        this.sessionHelper = sessionHelper;
//        this.passwordEncoder = passwordEncoder;
//        this.resetCodeHelper = resetCodeHelper;
//        this.resetCodeService = resetCodeService;
//    }
//
//    /**
//     * Login to the system
//     *
//     * @param
//     * @return
//     */
//    @PostMapping
//    @Operation(summary = "Login to the system")
//    public ResponseEntity<RestAPIResponse> login(
//            @RequestBody CreateAuthenticateRequest createAuthenticateRequest
//
//    ) {
//        // check email
//        User user = userService.getByEmailUserRole(createAuthenticateRequest.getEmail().trim(),
//                createAuthenticateRequest.getUserRole());
//        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "email not found");
//
//        // validate password
//        if (!passwordEncoder.matches(createAuthenticateRequest.getPassword().trim().concat(user.getSalt()),
//                user.getPasswordHash())) {
//            throw new ApplicationException(RestAPIStatus.INVALID_AUTHENTICATE_CREDENTIAL, "Wrong password");
//        }
//        // create session
//        Session session = sessionHelper.createSession(createAuthenticateRequest.isKeepLoggedIn(), user);
//        sessionService.saveSessionService(session);
//        return responseUtil.successResponse(session);
//    }
//
//    /**
//     * Log Out API
//     *
//     * @return
//     * @paramh ttpServletRequest
//     */
//    @DeleteMapping
//    @Operation(summary = "Logout of the system")
//    public ResponseEntity<RestAPIResponse> logout(
//            HttpServletRequest httpServletRequest
//    ) {
//        // Get token from request header
//        String token = httpServletRequest.getHeader(Constant.HEADER_TOKEN);
//        Session session = sessionService.getId(token);
//        if (session != null)
//            sessionService.deleteSession(session);
//
//        return responseUtil.successResponse("Ok");
//    }
//
//    /**
//     * Get Auth User Info
//     *
//     * @param authUser
//     * @return
//     */
//    @GetMapping
//    @Operation(summary = "Get Auth User Info")
//    public ResponseEntity<RestAPIResponse> getAuthInfo(
//            @Parameter(hidden = true) @AuthSession AuthUser authUser
//    ) {
//        return responseUtil.successResponse(authUser);
//    }
//
//    /**
//     * reset password
//     *
//     * @param createResetCodeRequest
//     * @return
//     */
//
//    @PostMapping(path = ApiPath.FORGOT_PASSWORD)
//    @Operation(summary = "Send Request Forgot Password")
//    public ResponseEntity<RestAPIResponse> requestForgotPassword(
//            @RequestBody ForgotPasswordRequest createResetCodeRequest
//    ) {
//        // get user by email
//        User user = userService.getByEmailAndStatus(createResetCodeRequest.getEmail().trim());
//        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "Invalid User ");
//        // create reset code
//        ResetCode resetCode = resetCodeHelper.createResetCode(user);
//        // save reset code
//        resetCodeService.saveResetCode(resetCode);
//        return responseUtil.successResponse(resetCode);
//    }
//
//    /**
//     * reset key
//     *
//     * @param resetKey
//     * @return
//     */
//    @PutMapping(path = ApiPath.RESET_PASSWORD)
//    @Operation(summary = "Reset Password")
//    public ResponseEntity<RestAPIResponse> resetPassword(
//            @PathVariable(name = "reset_key") String resetKey,
//            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest
//    ) {
//        // get reset key by reset code
//        ResetCode resetCode = resetCodeService.getByIdAndType(resetKey, ResetCodeType.RESET_PASSWORD);
//        Validator.notNull(resetCode, RestAPIStatus.NOT_FOUND, "Invalid reset code");
//        //get user by reset code
//        User user = userService.getId(resetCode.getUserId());
//        // Convert Date to Long
//
//        long newDay = (new Date().getTime());
//        long expirationDate = (resetCode.getExpireDate().getTime());
//        // compare newDay vs expirationDate
//        if (expirationDate > newDay) {
//            if (resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
//                // encode password
//                String newSalt = AppUtil.generateSalt();
//                user.setPasswordHash(passwordEncoder.encode(resetPasswordRequest.getNewPassword().trim().concat(newSalt)));
//                user.setSalt(newSalt);
//
//            } else {
//                throw new ApplicationException(RestAPIStatus.FAIL, "password incorrect, please try again");
//            }
//        } else {
//            throw new ApplicationException(RestAPIStatus.FAIL, "Expired reset key");
//        }
//
//        userService.saveUser(user);
//        Session session = sessionHelper.createSession(true, user);
//        sessionService.saveSessionService(session);
//        resetCodeService.deleteResetCode(resetCode);
//
//        return responseUtil.successResponse(session);
//    }
//
//    /**
//     * change Password
//     *
//     * @param changePasswordRequest
//     * @param authUser
//     * @return
//     */
//
//    @PutMapping(path = ApiPath.CHANGE_PASSWORD)
//    @Operation(summary = "Change Password")
//    public ResponseEntity<RestAPIResponse> changePassword(
//
//            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
//            @Parameter(hidden = true) @AuthSession AuthUser authUser
//    ) {
//        //get user by authUser
//        User user = userService.getId(authUser.getId());
//
//
//        if (passwordEncoder.matches(changePasswordRequest.getEnterOldPassword().trim().concat(user.getSalt()),
//                user.getPasswordHash())) {
//            if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
//                // encode password
//                String newSalt = AppUtil.generateSalt();
//                user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword().trim().concat(newSalt)));
//                user.setSalt(newSalt);
//
//            } else {
//                throw new ApplicationException(RestAPIStatus.FAIL, "password incorrect, please try again");
//            }
//
//        } else {
//            throw new ApplicationException(RestAPIStatus.FAIL, "not same password");
//        }
//        userService.saveUser(user);
//
//        List<Session> sessionList = sessionService.getAllByUserId(user.getId());
//        sessionService.deleteSessions(sessionList);
//        return responseUtil.successResponse("ok");
//    }
//}
//
