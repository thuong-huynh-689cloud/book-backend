package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.AppUtil;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.OrderHelper;
import com.cloud.secure.streaming.controllers.helper.OrderDetailHelper;
import com.cloud.secure.streaming.controllers.helper.RatingHelper;
import com.cloud.secure.streaming.controllers.helper.ReportHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateOrderRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateRatingRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateReportRequest;
import com.cloud.secure.streaming.controllers.model.response.CourseResponse;
import com.cloud.secure.streaming.controllers.model.response.OrderResponse;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.ORDER_API)
@Slf4j
public class OrderController extends AbstractBaseController {
    final OrdersService ordersService;
    final OrderDetailService orderDetailService;
    final CourseService courseService;
    final MediaInfoService mediaInfoService;
    final UserService userService;
    final OrderDetailHelper orderDetailHelper;
    final OrderHelper orderHelper;
    final RatingService ratingService;
    final RatingHelper ratingHelper;
    final ReportService reportService;
    final ReportHelper reportHelper;

    public OrderController(OrdersService ordersService, OrderDetailService orderDetailService, CourseService courseService,
                           MediaInfoService mediaInfoService, UserService userService, OrderDetailHelper orderDetailHelper, OrderHelper orderHelper, RatingService ratingService, RatingHelper ratingHelper, ReportService reportService, ReportHelper reportHelper) {
        this.ordersService = ordersService;
        this.orderDetailService = orderDetailService;
        this.courseService = courseService;
        this.mediaInfoService = mediaInfoService;
        this.userService = userService;
        this.orderDetailHelper = orderDetailHelper;
        this.orderHelper = orderHelper;
        this.ratingService = ratingService;
        this.ratingHelper = ratingHelper;
        this.reportService = reportService;
        this.reportHelper = reportHelper;
    }

    /**
     * Create Enrol Course API
     *
     * @param authUser
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PostMapping()
    @Operation(summary = "Create Order")
    public ResponseEntity<RestAPIResponse> createOrders(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateOrderRequest createOrderRequest
    ) {
        // check duplicate ids
        List<String> ids = createOrderRequest.getCourseId().stream().distinct().collect(Collectors.toList());
        // check purchased course
        int checkCourse = ordersService.checkOrder(createOrderRequest.getCourseId(), authUser.getId());
        if (checkCourse > 0) {
            throw new ApplicationException(RestAPIStatus.EXISTED, APIStatusMessage.PURCHASED_COURSE);
        }
        // check publish course
        List<Course> courses = courseService.getAllByIdIn(ids, CourseStatus.PUBLISH);
        if (courses.size() != createOrderRequest.getCourseId().size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        }

        Orders orders = orderHelper.createOrder(authUser.getId());

        // do create order courses detail
        List<OrderDetail> orderDetails = orderDetailHelper.createOrderDetails(orders.getId(), courses);

        orders.setTotalPoint(orderDetails.stream().mapToDouble(OrderDetail::getTotalPoint).sum());
        // save orders
        ordersService.save(orders);
        // save orderDetails
        if (!orderDetails.isEmpty()) {
            orderDetailService.saveAll(orderDetails);
        }

        return responseUtil.successResponse(orders);
    }

    /**
     * Get Enrol Course Detail API
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Order Detail")
    public ResponseEntity<RestAPIResponse> getOrders(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        // get courseOrder
        Orders orders = ordersService.getById(id);
        Validator.notNull(orders, RestAPIStatus.NOT_FOUND, APIStatusMessage.ENROLMENT_NOT_FOUND);

        if (authUser.getType().equals(UserType.LEARNER)) {
            if (!authUser.getId().equals(orders.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
            }
        }
        // get list courseOrderDetails
        List<OrderDetail> orderDetails = orderDetailService.getAllByCourseOrderId(orders.getId());

        return responseUtil.successResponse(new CourseResponse(orders, orderDetails));
    }

    /**
     * Get Page Order API
     *
     * @param searchKey
     * @param userId
     * @param sortFieldOrder
     * @param sortDirection
     * @param pageNumber
     * @param pageSize
     * @param authUser
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping
    @Operation(summary = "Get Paging Order")
    public ResponseEntity<RestAPIResponse> getPagingCourseOrder(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "user_id", required = false) String userId,
            @RequestParam(name = "sortFieldOrder", required = false, defaultValue = "createdDate") SortFieldOrder sortFieldOrder,
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize,
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE,
                pageNumber, pageSize);
        Page<OrderResponse> ordersPage;

        switch (authUser.getType()) {
            case SYSTEM_ADMIN:

                User user = null;

                if (userId != null) {
                    user = userService.getById(userId);
                }

                if (user != null && user.getType().equals(UserType.INSTRUCTOR)) {

                    ordersPage = ordersService.getPageOrderByInstructor(user.getId(), searchKey, sortFieldOrder,
                            sortDirection, pageNumber, pageSize);

                } else if (user != null && user.getType().equals(UserType.LEARNER)) {
                    ordersPage = ordersService.getPageOrderByLearner(user.getId(), searchKey, sortFieldOrder,
                            sortDirection, pageNumber, pageSize);

                } else {
                    ordersPage = ordersService.getPageOrderBySystemAdminAndLearner(userId, searchKey, sortFieldOrder,
                            sortDirection, pageNumber, pageSize);
                }
                break;
            case INSTRUCTOR:
                ordersPage = ordersService.getPageOrderByInstructor(authUser.getId(), searchKey, sortFieldOrder,
                        sortDirection, pageNumber, pageSize);
                break;
            case LEARNER:
                ordersPage = ordersService.getPageOrderByLearner(authUser.getId(), searchKey, sortFieldOrder,
                        sortDirection, pageNumber, pageSize);
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
        }

        return responseUtil.successResponse(new PagingResponse(ordersPage));
    }


    /**
     * Get List File CSV Orders API
     *
     * @param authUser
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @GetMapping("all")
    @Operation(summary = "Get List File CSV Order")
    public ResponseEntity<RestAPIResponse> getListFileCSVOrders(
            HttpServletResponse response,
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "user_id", required = false) String userId

    ) {
        List<OrderResponse> orderResponses;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                orderResponses = ordersService.getAllOrderByUserId(userId);

                break;
            case INSTRUCTOR:
                orderResponses = ordersService.getAllOrderByUserId(authUser.getId());
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
        }
        String[] headersOrder = {"title", "name", "email", "createdDate", "totalPoint"};
        String[] headersOrderMapping = {"title", "name", "email", "createdDate", "totalPoint"};

        AppUtil.exportCSV(headersOrder, headersOrderMapping, orderResponses, "order.csv", response);

        return responseUtil.successResponse(orderResponses);
    }

    /**
     * Create Rating API
     *
     * @param createRatingRequestRequest
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PostMapping("/rating")
    @Operation(summary = "Create Rating")
    public ResponseEntity<RestAPIResponse> createRating(
            @Valid @RequestBody CreateRatingRequest createRatingRequestRequest,
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        // check course
        Course course = courseService.getById(createRatingRequestRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        if (authUser.getType().equals(UserType.LEARNER) && !authUser.getId().equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // Create rating
        Rating rating = ratingHelper.createRating(createRatingRequestRequest);

        rating = ratingService.save(rating);
        return responseUtil.successResponse(rating);
    }

    /**
     * Create Report API
     *
     * @param createReportRequest
     * @param authUser
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PostMapping("/report")
    @Operation(summary = "Create Report")
    public ResponseEntity<RestAPIResponse> createReport(
            @Valid @RequestBody CreateReportRequest createReportRequest,
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        // check course
        Course course = courseService.getById(createReportRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        if (authUser.getType().equals(UserType.LEARNER) && !authUser.getId().equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // Create rating
        Report report = reportHelper.createReport(createReportRequest);

        report = reportService.save(report);
        return responseUtil.successResponse(report);
    }
}
