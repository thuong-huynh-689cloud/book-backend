package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.SessionHelper;
import com.cloud.secure.streaming.controllers.helper.UserHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateUserRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateUserRequest;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.SessionService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
    final SessionHelper sessionHelper;
    final SessionService sessionService;

    public UserControllers(UserService userService, UserHelper userHelper, PasswordEncoder passwordEncoder,
                           SessionHelper sessionHelper, SessionService sessionService) {
        this.userService = userService;
        this.userHelper = userHelper;
        this.passwordEncoder = passwordEncoder;
        this.sessionHelper = sessionHelper;
        this.sessionService = sessionService;
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
        User user = userService.getByEmailAndStatus(createUserRequest.getEmail());
        Validator.mustNull(user, RestAPIStatus.EXISTED, "EMAIL_EXISTED");
        // create user
        user = userHelper.createUser(createUserRequest, passwordEncoder);
        // save user
        userService.saveUser(user);
        return responseUtil.successResponse(user);
    }

    /**
     * update user API
     *
     * @param id
     * @param updateUserRequest
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN})
    @PutMapping(path = ApiPath.ID)
    @Operation(summary = "Update User ")
    public ResponseEntity<RestAPIResponse> updateUser(
            @PathVariable(name = "id") String id,
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        // check id
        User user = userService.getById(id);
        // update user
        user = userHelper.updateUser(user, updateUserRequest, passwordEncoder);
        userService.saveUser(user);

        return responseUtil.successResponse(user);
    }

    /**
     * Delete User API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN})
    @DeleteMapping
    @Operation(summary = "Delete User")
    public ResponseEntity<RestAPIResponse> deleteUser(
            @RequestParam(name = "ids") List<String> ids
    ) {
        //get user
        List<User> users = userService.getAllByIdInAndStatus(ids, Status.ACTIVE);
        if (users.size() != ids.size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        }
        // delete user
        userService.updateUserByIdInToInactive(ids);

        return responseUtil.successResponse("Ok");
    }

    /**
     * Get Detail User
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN, UserRole.STAFF, UserRole.CUSTOMER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get User Detail")
    public ResponseEntity<RestAPIResponse> getDetailUser(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        User user = null;
        // Check Permission
        if (authUser.getRole().equals(UserRole.STAFF) || authUser.getRole().equals(UserRole.CUSTOMER)) {

            if (!authUser.getId().equals(id)) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, "CAN_ONLY_VIEW_YOUR_OWN_INFORMATION");
            }
            // Get User
            user = userService.getByIdAndStatus(id, Status.ACTIVE);
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, "USER_NOT_FOUND");
        } else {
            // Get User
            user = userService.getByIdAndStatus(id, Status.ACTIVE);
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, "USER_NOT_FOUND");
        }

        return responseUtil.successResponse(user);
    }

    /**
     * Get Paging Users API
     *
     * @param searchKey
     * @param sortFieldUser
     * @param sortDirection
     * @param type
     * @param statuses
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @AuthorizeValidator(UserRole.ADMIN)
    @GetMapping
    @Operation(summary = "Get paging users")
    public ResponseEntity<RestAPIResponse> getPagingUsers(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldUser
                    sortFieldUser,
            @RequestParam(value = "sort_direction", required = false, defaultValue = "ASC") SortDirection
                    sortDirection, // ASC or DESC
            @RequestParam(name = "type", required = false, defaultValue = "") List<UserRole> type,
            @RequestParam(name = "statuses", required = false) List<Status> statuses, // ACTIVE,INACTIVE,PENDING
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_REQUEST,
                APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE, pageNumber, pageSize);
        // check statuses
        if (statuses == null || statuses.isEmpty()) {
            statuses = Arrays.asList(Status.values());
        }
        // check type
        if (type == null || type.isEmpty()) {
            type = Arrays.asList(UserRole.values());
        }
        // get page user
        Page<User> userPage = userService.getPageUser(searchKey, sortFieldUser, sortDirection, type, statuses, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(userPage));
    }
}