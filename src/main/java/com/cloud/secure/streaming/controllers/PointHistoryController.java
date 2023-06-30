package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.PointHistoryHelper;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse;

import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.services.PointHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.POINT_HISTORY_API)
@Slf4j
public class PointHistoryController extends AbstractBaseController {

    final PointHistoryService pointHistoryService;
    final PointHistoryHelper pointHistoryHelper;


    public PointHistoryController(PointHistoryService pointHistoryService, PointHistoryHelper pointHistoryHelper) {
        this.pointHistoryService = pointHistoryService;
        this.pointHistoryHelper = pointHistoryHelper;
    }

//    /**
//     * Get Paging Point History API
//     *
//     * @param searchKey
//     * @param userId
//     * @param sortFieldPointHistory
//     * @param sortDirection
//     * @param pageNumber
//     * @param pageSize
//     * @return
//     */
//    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
//    @GetMapping
//    @Operation(summary = "Get Paging Point History")
//    public ResponseEntity<RestAPIResponse> getPagePointHistory(
//            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
//            @RequestParam(name = "user_id", required = false) String userId,
//            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldPointHistory sortFieldPointHistory, //name,student,createdDate
//            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection, // ASC or DESC
//            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
//            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
//
//    ) {
//        // validate page number and page size
//        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_REQUEST, APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE, pageNumber, pageSize);
//        // get page pointHistory
//        Page<PointHistoryResponse> pointHistoryPage = pointHistoryService.getPagePointHistory(userId, searchKey, sortFieldPointHistory, sortDirection, pageNumber, pageSize);
//
//        return responseUtil.successResponse(new PagingResponse(pointHistoryPage));
//    }
//
//    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.LEARNER})
//    @GetMapping(path = "/{id}")
//    @Operation(summary = "Get Detail Point History")
//    public ResponseEntity<RestAPIResponse> getDetailPointHistory(
//            @Parameter(hidden = true) @AuthSession AuthUser authUser,
//            @PathVariable(name = "id") String id
//    ) {
//        PointHistory pointHistory = null;
//        switch (authUser.getType()) {
//
//            case SYSTEM_ADMIN:
//
//                // get pointHistory
//                pointHistory = pointHistoryService.getByIdAndTransactionAndStatus(id, TransactionStatus.SUCCESS);
//
//                break;
//
//            case LEARNER:
//                // get pointHistory
//                pointHistory = pointHistoryService.getByIdAndUserIdAndPointHistoryAndStatus(id, authUser.getId(),
//                        TransactionStatus.SUCCESS);
//                break;
//
//        }
//
//        Validator.notNull(pointHistory, RestAPIStatus.NOT_FOUND, APIStatusMessage.POINT_HISTORY_NOT_FOUND);
//
//        return responseUtil.successResponse(pointHistory);
//    }
}
