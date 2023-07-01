package com.cloud.secure.ecommerce.controllers;

import com.cloud.secure.ecommerce.common.enums.ResetCodeType;
import com.cloud.secure.ecommerce.common.enums.Status;
import com.cloud.secure.ecommerce.common.enums.UserRole;
import com.cloud.secure.ecommerce.common.exceptions.ApplicationException;
import com.cloud.secure.ecommerce.common.utils.AppUtils;
import com.cloud.secure.ecommerce.common.utils.RestAPIResponse;
import com.cloud.secure.ecommerce.common.utils.RestAPIStatus;
import com.cloud.secure.ecommerce.common.utils.Validator;
import com.cloud.secure.ecommerce.config.security.AuthSession;
import com.cloud.secure.ecommerce.config.security.AuthUser;
import com.cloud.secure.ecommerce.config.security.AuthorizeValidator;
import com.cloud.secure.ecommerce.controllers.helper.ResetCodeHelper;
import com.cloud.secure.ecommerce.controllers.helper.SessionHelper;
import com.cloud.secure.ecommerce.controllers.helper.UserHelper;
import com.cloud.secure.ecommerce.controllers.model.request.CreateSignUpRequest;
import com.cloud.secure.ecommerce.controllers.model.request.CreateUserRequest;
import com.cloud.secure.ecommerce.controllers.model.request.UpdateUserRequest;
import com.cloud.secure.ecommerce.controllers.model.response.PagingResponse;
import com.cloud.secure.ecommerce.entities.ResetCode;
import com.cloud.secure.ecommerce.entities.User;
import com.cloud.secure.ecommerce.services.ResetCodeService;
import com.cloud.secure.ecommerce.services.SessionService;
import com.cloud.secure.ecommerce.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.USER_API)
public class UserControllers extends AbstractBaseController {
    final UserService userService;
    final UserHelper userHelper;
    final PasswordEncoder passwordEncoder;
    final ResetCodeService resetCodeService;
    final SessionHelper sessionHelper;
    final SessionService sessionService;
    final ResetCodeHelper resetCodeHelper;


    public UserControllers(UserService userService, UserHelper userHelper, PasswordEncoder passwordEncoder, ResetCodeService resetCodeService, SessionHelper sessionHelper, SessionService sessionService, ResetCodeHelper resetCodeHelper) {
        this.userService = userService;
        this.userHelper = userHelper;
        this.passwordEncoder = passwordEncoder;
        this.resetCodeService = resetCodeService;
        this.sessionHelper = sessionHelper;
        this.sessionService = sessionService;
        this.resetCodeHelper = resetCodeHelper;
    }

    /**
     * create user API
     *
     * @param createUserRequest
     * @return
     */

    @PostMapping
    @Operation(summary = "Create User ")
    public ResponseEntity<RestAPIResponse> createUser(
            @RequestBody CreateUserRequest createUserRequest
    ) {
        // validate phone number
        Validator.validPhoneNumber(createUserRequest.getPhoneNumber().trim());
        // create user
        User user = userHelper.createUser(createUserRequest, passwordEncoder);
        // save user
        userService.saveUser(user);
        return responseUtil.successResponse(user);
    }

    /**
     * get User API
     *
     * @param id
     * @return
     */
    @GetMapping(path = ApiPath.ID)
    @Operation(summary = "Get User")
    public ResponseEntity<RestAPIResponse> getUser(
            @PathVariable(name = "id") String id
    ) {
        //enter id user show to display user
        User user = userService.getId(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "id user not found");

        return responseUtil.successResponse(user);
    }

    /**
     * update user API
     *
     * @param id
     * @param updateUserRequest
     * @return
     */
    @PutMapping(path = ApiPath.ID)
    @Operation(summary = "Update User ")
    public ResponseEntity<RestAPIResponse> updateUser(

            @PathVariable(name = "id") String id,
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {

        if (authUser.getRole().equals(UserRole.CUSTOMER) && !authUser.getId().equals(id)) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        // check id
        User user = userService.getId(id);
        // update user
        user = userHelper.updateUser(user, updateUserRequest, passwordEncoder);
        userService.saveUser(user);

        return responseUtil.successResponse(user);
    }

    /**
     * delete user API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN})
    @DeleteMapping()
    @Operation(summary = "Delete User ")
    public ResponseEntity<RestAPIResponse> deleteUser(
            @RequestParam(name = "ids") List<String> ids
    ) {
        //check ids
        List<String> idsList = ids.stream().distinct().collect(Collectors.toList());
        // get users
        List<User> users = userService.getAllByIdInAndStatus(idsList, Status.ACTIVE);
        if (idsList.size() != users.size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, "user not found");
        }
        // delete user
        users.forEach(user -> user.setStatus(Status.IN_ACTIVE));
        userService.saveAll(users);

        return responseUtil.successResponse(users);
    }

    /**
     * Get Paging API
     *
     * @param
     * @return
     */
    @GetMapping()
    @Operation(summary = "Get Paging User ")
    public ResponseEntity<RestAPIResponse> getUsers(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "is_asc", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(name = "sort_field", required = false, defaultValue = "false") String sortField,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        Page<User> serPage = userService.getByLastNameAndFirstNameContaining(searchKey.trim(), isAsc, sortField, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(serPage));

    }

    /**
     * singUp account
     *
     * @param createSignUpRequest
     * @return
     */

    @PostMapping(path = ApiPath.SIGN_UP)
    @Operation(summary = "Create CUSTOMER account  ")
    public ResponseEntity<RestAPIResponse> signUp(
            @RequestBody CreateSignUpRequest createSignUpRequest
    ) {
        // get user by email
        User user = userService.getByEmail(createSignUpRequest.getEmail().trim());
        if (user != null) {
            //1. status = active
            if (user.getStatus().equals(Status.ACTIVE)) {
                // => show lỗi account đã tồn tại
                throw new ApplicationException(RestAPIStatus.NOT_FOUND, "Email already exists ");
            }
            // 2. status = pending
            if (user.getStatus().equals(Status.PENDING)) {
                // => set lại password
                String newSalt = AppUtils.generateSalt();
                user.setPasswordHash(passwordEncoder.encode(createSignUpRequest.getPasswordHash().trim().concat(newSalt)));
                user.setSalt(newSalt);
            }
            // 3. inactive
            if (user.getStatus().equals(Status.IN_ACTIVE)) {
                // create user
                user = userHelper.createSignUp(createSignUpRequest, passwordEncoder);
            }

        } else if (user == null) {
            // create user
            user = userHelper.createSignUp(createSignUpRequest, passwordEncoder);
        }
        userService.saveUser(user);
        ResetCode resetCode = resetCodeHelper.createSignUp(user);
        resetCodeService.saveResetCode(resetCode);

        return responseUtil.successResponse(resetCode);
    }

    /**
     * verify account
     *
     * @param verifyKey
     * @return
     */


    @PutMapping(path = ApiPath.VERIFY_ACCOUNT)
    @Operation(summary = "Verify CUSTOMER account  ")
    public ResponseEntity<RestAPIResponse> verifyUser(
            @PathVariable("verify_key") String verifyKey

    ) {
        // get resetCode by id and type
        ResetCode resetCode = resetCodeService.getByIdAndType(verifyKey, ResetCodeType.VERIFY_ACCOUNT);
        Validator.notNull(resetCode, RestAPIStatus.NOT_FOUND, "id not found");
        // check data
        long newDay = (new Date().getTime());
        long expirationDate = (resetCode.getExpireDate().getTime());
        // compare newDay vs expirationDate
        if (expirationDate < newDay) {
            throw new ApplicationException(RestAPIStatus.EXPIRED, "Expired reset key");
        }
        User user = userService.getId(resetCode.getUserId());
        user.setStatus(Status.ACTIVE);
        userService.saveUser(user);

        return responseUtil.successResponse(user);
    }
}
