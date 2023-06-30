package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.Constant;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.BankInformationHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateBankInformationRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateBankInformationRequest;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.BankInformation;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.BankInformationService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.BANK_INFORMATION_API)
@Slf4j
public class BankInformationController extends AbstractBaseController {
    final UserService userService;
    final BankInformationHelper bankInformationHelper;
    final BankInformationService bankInformationService;

    public BankInformationController(UserService userService,
                                     BankInformationHelper bankInformationHelper,
                                     BankInformationService bankInformationService
    ) {
        this.userService = userService;
        this.bankInformationHelper = bankInformationHelper;
        this.bankInformationService = bankInformationService;
    }

    /**
     * Create Bank Information API
     *
     * @param createBankInformationRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @PostMapping
    @Operation(summary = "Create Bank Information")
    public ResponseEntity<RestAPIResponse> createBankInformation(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateBankInformationRequest createBankInformationRequest
    ) {
        // check account number
        Validator.mustMatch(createBankInformationRequest.getAccountNumber(), Constant.BANK_ACCOUNT_NUMBER_PATTERN, RestAPIStatus.BAD_PARAMS,APIStatusMessage.INVALID_BANK_ACCOUNT_NUMBER);
        String userId;
        switch (authUser.getType()){
            case SYSTEM_ADMIN:
                if (createBankInformationRequest.getUserId() == null || createBankInformationRequest.getUserId().isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS,APIStatusMessage.USER_ID_IS_EMPTY);
                }
                userId = createBankInformationRequest.getUserId();
                break;
            case LEARNER:
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        User user = userService.getByIdAndStatus(userId, AppStatus.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        BankInformation bankInformation = bankInformationHelper.createBankInformation(user.getId(), createBankInformationRequest);
//        // user only create for themselves
//        if ((authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER))
//                && !authUser.getId().equals(createBankInformationRequest.getUserId())) {
//            throw new ApplicationException(RestAPIStatus.FORBIDDEN, "User only create for themselves");
//        }
//        BankInformation bankInformation = new BankInformation();
//        // admin create
//        if (authUser.getType().equals(UserType.SYSTEM_ADMIN)) {
//            // check user id
//            if (createBankInformationRequest.getUserId() == null || createBankInformationRequest.getUserId().isEmpty()) {
//                throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "User id is empty");
//            }
//            User user = userService.getByIdAndStatus(createBankInformationRequest.getUserId(), AppStatus.ACTIVE);
//            Validator.notNull(user, RestAPIStatus.NOT_FOUND, "User not found");
//            // create bank information
//            bankInformation = bankInformationHelper.createBankInformation(user.getId(), createBankInformationRequest);
//        }
//        // user create
//        if (authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER)) {
//            // create bank information
//            bankInformation = bankInformationHelper.createBankInformation(authUser.getId(), createBankInformationRequest);
//        }
        // save
        bankInformationService.save(bankInformation);

        return responseUtil.successResponse(bankInformation);
    }

    /**
     * Update Bank Information API
     *
     * @param authUser
     * @param id
     * @param updateBankInformationRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Bank Information")
    public ResponseEntity<RestAPIResponse> updateBankInformation(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id, // bank information id
            @Valid @RequestBody UpdateBankInformationRequest updateBankInformationRequest
    ) {
        // check account number
        Validator.mustMatch(updateBankInformationRequest.getAccountNumber(), Constant.BANK_ACCOUNT_NUMBER_PATTERN, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_BANK_ACCOUNT_NUMBER);
        // check bank information exists
        BankInformation bankInformation = bankInformationService.getById(id);
        Validator.notNull(bankInformation, RestAPIStatus.NOT_FOUND, APIStatusMessage.BANK_INFORMATION_NOT_FOUND);
        // user only update for themselves
        if ((authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER))
                && !authUser.getId().equals(bankInformation.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // update information
        bankInformation = bankInformationHelper.updateBankInformation(bankInformation, updateBankInformationRequest);
        // save
        bankInformationService.save(bankInformation);

        return responseUtil.successResponse(bankInformation);
    }

    /**
     * Get Detail Bank Information API
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Bank Information")
    public ResponseEntity<RestAPIResponse> getBankInformation(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id // bank information id
    ) {
        // get bank information
        BankInformation bankInformation = bankInformationService.getById(id);
        Validator.notNull(bankInformation, RestAPIStatus.NOT_FOUND, APIStatusMessage.BANK_INFORMATION_NOT_FOUND);
        // user only see of themselves
        if ((authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER))
                && !authUser.getId().equals(bankInformation.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
        }
        return responseUtil.successResponse(bankInformation);
    }

    /**
     * Delete Bank Information API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @DeleteMapping
    @Operation(summary = "Delete Bank Information")
    public ResponseEntity<RestAPIResponse> deleteBankInformation(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        // check user type
        List<BankInformation> bankInformations = bankInformationService.getAllByIdIn(ids);
        // user only delete for themselves
        if (authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER)) {
            List<BankInformation> bankInformationListWithWrongUserId = bankInformations.stream()
                    .filter(bankInformation -> !bankInformation.getUserId().equals(authUser.getId()))
                    .collect(Collectors.toList());
            if (!bankInformationListWithWrongUserId.isEmpty()) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
            }
        }
        // delete
        bankInformationService.deleteByIdIn(ids);
        return responseUtil.successResponse("OK");
    }

    /**
     * Get Paging Bank Information API
     *
     * @param authUser
     * @param userId
     * @param searchKey
     * @param sortFieldBankInformation
     * @param sortDirection
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping
    @Operation(summary = "Get Paging Bank Information")
    public ResponseEntity<RestAPIResponse> getPagingBankInformation(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "user_Id", required = false, defaultValue = "") String userId,
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldBankInformation sortFieldBankInformation,
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE,
                pageNumber, pageSize);
        Page<BankInformation> bankInformationPage;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                // user id null --> get all
                if (userId == null || userId.isEmpty()) {
                    // get all bank information
                    bankInformationPage = bankInformationService.getPage(searchKey, sortFieldBankInformation, sortDirection, pageNumber, pageSize);
                } else {
                    bankInformationPage = bankInformationService.getPageByUserId(userId, searchKey, sortFieldBankInformation, sortDirection, pageNumber, pageSize);
                }
                break;
            case INSTRUCTOR:
            case LEARNER:
                bankInformationPage = bankInformationService.getPageByUserId(authUser.getId(),searchKey, sortFieldBankInformation, sortDirection, pageNumber, pageSize);
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
//        Page<BankInformation> bankInformationPage = null;
//        // user get page
//        if (authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER)) {
//            bankInformationPage = bankInformationService.getPageByUserId(authUser.getId(),searchKey, sortFieldBankInformation, sortDirection, pageNumber, pageSize);
//        }
//        // admin get page
//        if (authUser.getType().equals(UserType.SYSTEM_ADMIN)) {
//            // user id null --> get all
//            if (userId == null || userId.isEmpty()) {
//                // get all bank information
//                bankInformationPage = bankInformationService.getPage(searchKey, sortFieldBankInformation, sortDirection, pageNumber, pageSize);
//            }
//            // user id not null --> get page by user id
//            if (!userId.isEmpty()) {
//                bankInformationPage = bankInformationService.getPageByUserId(userId, searchKey, sortFieldBankInformation, sortDirection, pageNumber, pageSize);
//            }
//        }
        return responseUtil.successResponse(new PagingResponse(bankInformationPage));
    }

    /**
     * Get List Bank Informations API
     *
     * @param authUser
     * @param userId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/user/{user_id}")
    @Operation(summary = "Get Bank Information")
    public ResponseEntity<RestAPIResponse> getBankInformations(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(value = "user_id", required = false) String userId
    ) {
        // Check Permission
        switch (authUser.getType()){
            case SYSTEM_ADMIN:
                if (userId == null || userId.isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS,APIStatusMessage.USER_ID_IS_EMPTY);
                }
                break;
            case LEARNER:
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
//        if ((authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER))
//                && !authUser.getId().equals(userId)) {
//            throw new ApplicationException(RestAPIStatus.FORBIDDEN, "User only see information of themselves");
        List<BankInformation> bankInformations = bankInformationService.getAllByUserIdOrderByCreatedDateDesc(userId);

        return responseUtil.successResponse(bankInformations);
    }
}
