package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.CourseAdminFeedbackHelper;
import com.cloud.secure.streaming.controllers.helper.CourseCategoryHelper;
import com.cloud.secure.streaming.controllers.helper.CourseHelper;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.controllers.model.response.CourseResponse;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 689Cloud
 */

@RestController
@RequestMapping(ApiPath.COURSE_API)
@Slf4j
public class CourseController extends AbstractBaseController {

    final CourseService courseService;
    final CourseHelper courseHelper;
    final UserService userService;
    final CategoryService categoryService;
    final CourseCategoryHelper courseCategoryHelper;
    final CourseCategoryService courseCategoryService;
    final CourseAdminFeedbackService courseAdminFeedbackService;
    final CourseAdminFeedbackHelper courseAdminFeedbackHelper;
    final IntendedLearnerService intendedLearnerService;
    final OrdersService ordersService;
    final LectureService lectureService;
    final MediaInfoService mediaInfoService;

    public CourseController(CourseService courseService,
                            CourseHelper courseHelper,
                            UserService userService,
                            CategoryService categoryService,
                            CourseCategoryHelper courseCategoryHelper,
                            CourseCategoryService courseCategoryService,
                            CourseAdminFeedbackService courseAdminFeedbackService, CourseAdminFeedbackHelper courseAdminFeedbackHelper,
                            IntendedLearnerService intendedLearnerService, OrdersService ordersService, LectureService lectureService,
                            MediaInfoService mediaInfoService) {
        this.courseService = courseService;
        this.courseHelper = courseHelper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.courseCategoryHelper = courseCategoryHelper;
        this.courseCategoryService = courseCategoryService;
        this.courseAdminFeedbackService = courseAdminFeedbackService;
        this.courseAdminFeedbackHelper = courseAdminFeedbackHelper;
        this.intendedLearnerService = intendedLearnerService;
        this.ordersService = ordersService;
        this.lectureService = lectureService;
        this.mediaInfoService = mediaInfoService;
    }

    /**
     * Create Course API
     *
     * @param authUser
     * @param createCourseRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping
    @Operation(summary = "Create Course")
    public ResponseEntity<RestAPIResponse> createCourse(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateCourseRequest createCourseRequest
    ) {
        Course course = new Course();
        // check role admin
        if (authUser.getType().equals(UserType.SYSTEM_ADMIN)) {
            // create course
            User user = userService.getByIdAndType(createCourseRequest.getUserId(), UserType.INSTRUCTOR);
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);

            course = courseHelper.createCourse(user.getId(), createCourseRequest);
            // check role user
        }
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            // create course
            course = courseHelper.createCourse(authUser.getId(), createCourseRequest);
        }
        // save course
        courseService.save(course);

        // get category
        List<Category> categoryList = categoryService.getAllByIdIn(createCourseRequest.getCategoryIds());
        if (!categoryList.isEmpty()) {
            for (Category category : categoryList) {
                // get CourseCategory
                CourseCategory courseCategory = courseCategoryHelper.createCourseCategory(course.getId(), category);
                courseCategoryService.save(courseCategory);
            }
        }
        return responseUtil.successResponse(new CourseResponse(course, categoryList));
    }

    /**
     * Update Course API
     *
     * @param authUser
     * @param updateCourseRequest
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Course")
    public ResponseEntity<RestAPIResponse> updateCourse(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid UpdateCourseRequest updateCourseRequest,
            @PathVariable("id") String id  // course id
    ) {
        // get Course
        Course course = courseService.getByIdAndStatus(id,AppStatus.ACTIVE);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // Check Permission INSTRUCTOR
        if (authUser.getType().equals(UserType.INSTRUCTOR) && !authUser.getId().equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // update course
        course = courseHelper.updateCourse(course, updateCourseRequest);

        List<Category> categories = new ArrayList<>();

        List<CourseCategory> courseCategories = new ArrayList<>();

        if (updateCourseRequest.getCategoryIds() != null && !updateCourseRequest.getCategoryIds().isEmpty()) {

            categories = categoryService.getAllByIdIn(updateCourseRequest.getCategoryIds());
            Validator.notNull(categories, RestAPIStatus.NOT_FOUND, APIStatusMessage.CATEGORY_NOT_FOUND);

            for (Category category : categories) {
                CourseCategory courseCategory = courseCategoryHelper.createCourseCategory(course.getId(), category);

                // delete old CourseCategory
                List<CourseCategory> oldCourseCategories = courseCategoryService.getAllByCourseCategoryId_CourseId(course.getId());
                if (!oldCourseCategories.isEmpty()) {
                    for (CourseCategory oldCourseCategory : oldCourseCategories) {
                        courseCategoryService.delete(oldCourseCategory);
                    }
                }

                courseCategories.add(courseCategory);
            }
        }

        boolean isCourseInformationTypeLp = course.getCourseInformationType() == null || course.getCourseInformationType().trim().isEmpty();
        if (isCourseInformationTypeLp || !course.getCourseInformationType().contains(CourseInformationType.LANDING_PAGE.toString())) {
            course.setCourseInformationType(isCourseInformationTypeLp ? CourseInformationType.LANDING_PAGE.toString()
                    : course.getCourseInformationType() + "," + CourseInformationType.LANDING_PAGE.toString());
        }
        courseService.save(course);
        courseCategoryService.saveAll(courseCategories);

        return responseUtil.successResponse(new CourseResponse(course, categories));
    }

    /**
     * Create Course Pricing API
     *
     * @param authUser
     * @param updateCoursePricingRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/course-pricing/{id}")
    @Operation(summary = "Update Course Pricing")
    public ResponseEntity<RestAPIResponse> updateCoursePricing(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody UpdateCoursePricingRequest updateCoursePricingRequest,
            @PathVariable("id") String id  // course id
    ) {
        // get Course
        Course course = courseService.getByIdAndStatus(id,AppStatus.ACTIVE);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        // Check Permission INSTRUCTOR
        if (authUser.getType().equals(UserType.INSTRUCTOR) && !authUser.getId().equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // don't need to check FREE type with point. when user purchase, please check type & point
        if (updateCoursePricingRequest.getCoursePrice() != null) {
            course.setCoursePrice(updateCoursePricingRequest.getCoursePrice());
        }
        if (updateCoursePricingRequest.getPoint() != null && CoursePrice.FREE.equals(updateCoursePricingRequest.getCoursePrice())) {
            course.setPoint(0);
        }
        if (updateCoursePricingRequest.getPoint() != null && !CoursePrice.FREE.equals(updateCoursePricingRequest.getCoursePrice())) {
            course.setPoint(updateCoursePricingRequest.getPoint());
        }

        boolean isCourseInformationTypeLp = course.getCourseInformationType() == null || course.getCourseInformationType().trim().isEmpty();
        if (isCourseInformationTypeLp || !course.getCourseInformationType().contains(CourseInformationType.PRICING.toString())) {
            course.setCourseInformationType(isCourseInformationTypeLp ? CourseInformationType.PRICING.toString()
                    : course.getCourseInformationType() + "," + CourseInformationType.PRICING.toString());
        }
        courseService.save(course);

        return responseUtil.successResponse(course);
    }

    /**
     * Create Course Promotion API
     *
     * @param authUser
     * @param updateCoursePromotionRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "promotion/{id}")
    @Operation(summary = "Update Course Promotion")
    public ResponseEntity<RestAPIResponse> updateCoursePromotion(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody UpdateCoursePromotionRequest updateCoursePromotionRequest,
            @PathVariable("id") String id  // course id
    ) throws ParseException {
        // get Course
        Course course = courseService.getByIdAndStatus(id,AppStatus.ACTIVE);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        // Check Permission INSTRUCTOR
        if (authUser.getType().equals(UserType.INSTRUCTOR) && !authUser.getId().equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // check point
        if (updateCoursePromotionRequest.getDiscount() != null) {
            course.setDiscount(updateCoursePromotionRequest.getDiscount());
        }
        // check showHidden
        if (updateCoursePromotionRequest.getShowPromotion() != null) {
            course.setShowPromotion(updateCoursePromotionRequest.getShowPromotion());
        }

        //add expire_data to data
        if (updateCoursePromotionRequest.getExpireDate() != null && !updateCoursePromotionRequest.getExpireDate().isEmpty()) {

            Date date = new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(updateCoursePromotionRequest.getExpireDate());
            course.setExpireDate(DateUtil.convertToUTC(date, authUser.getZoneId()));

        } else {
            updateCoursePromotionRequest.setExpireDate(null);
        }

        boolean isCourseInformationTypeLp = course.getCourseInformationType() == null || course.getCourseInformationType().trim().isEmpty();
        if (isCourseInformationTypeLp || !course.getCourseInformationType().contains(CourseInformationType.PROMOTION.toString())) {
            course.setCourseInformationType(isCourseInformationTypeLp ? CourseInformationType.PROMOTION.toString()
                    : course.getCourseInformationType() + "," + CourseInformationType.PROMOTION.toString());

        }
        courseService.save(course);

        return responseUtil.successResponse(course);
    }

    /**
     * Update Course Status API
     *
     * @param authUser
     * @param updateCourseStatusRequest
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}/status")
    @Operation(summary = "Update Course Status")
    public ResponseEntity<RestAPIResponse> updateCourseStatus(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid UpdateCourseStatusRequest updateCourseStatusRequest,
            @PathVariable("id") String id  // course id
    ) {
        // get Course
        Course course = courseService.getByIdAndStatus(id,AppStatus.ACTIVE);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        CourseAdminFeedback courseAdminFeedback = null;

        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                if (updateCourseStatusRequest.getCourseStatus() != null
                        && !(CourseStatus.DRAFT.equals(updateCourseStatusRequest.getCourseStatus()) ||
                        CourseStatus.PUBLISH.equals(updateCourseStatusRequest.getCourseStatus()) ||
                        CourseStatus.REJECT.equals(updateCourseStatusRequest.getCourseStatus()))) {
                    throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CANNOT_UPDATE_STATUS_PUBLISH_OR_REJECT_FOR_THIS_COURSE);
                } else {
                    course.setCourseStatus(updateCourseStatusRequest.getCourseStatus());
                }
                if (CourseStatus.REJECT.equals(updateCourseStatusRequest.getCourseStatus())) {
                    // create admin feedback
                    courseAdminFeedback = courseAdminFeedbackHelper.createCourseAdminFeedback(course.getId(),
                            updateCourseStatusRequest.getFeedbackType(), updateCourseStatusRequest.getFeedback());
                    courseAdminFeedbackService.save(courseAdminFeedback);
                }
                if (CourseStatus.PUBLISH.equals(updateCourseStatusRequest.getCourseStatus())) {
                    // delete admin feedback
                    courseAdminFeedbackService.deleteAllByCourseId(course.getId());
                }
                break;
            case INSTRUCTOR:
                if (updateCourseStatusRequest.getCourseStatus() != null
                        && !(CourseStatus.DRAFT.equals(updateCourseStatusRequest.getCourseStatus()) ||
                        CourseStatus.WAITING_FOR_APPROVAL.equals(updateCourseStatusRequest.getCourseStatus()))) {
                    throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CANNOT_UPDATE_STATUS_DRAFT_OR_WAITING_FOR_APPROVAL_FOR_THIS_COURSE);
                } else {
                    course.setCourseStatus(updateCourseStatusRequest.getCourseStatus());
                }
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        courseService.save(course);

        return responseUtil.successResponse(new CourseResponse(course, courseAdminFeedback));
    }

    /**
     * Get Detail Course API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Detail Course")
    public ResponseEntity<RestAPIResponse> getDetailCourse(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id // course id
    ) {

        CourseResponse courseResponse;

        if (authUser.getType().equals(UserType.SYSTEM_ADMIN) || (authUser.getType().equals(UserType.INSTRUCTOR))) {

            // get Course
            Course course = courseService.getByIdAndStatus(id, AppStatus.ACTIVE);
            Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

            // Check Permission INSTRUCTOR
            if (authUser.getType().equals(UserType.INSTRUCTOR) && !authUser.getId().equals(course.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
            }
            // get all categories
            List<Category> categories = categoryService.getAllByCourseId(course.getId());
            // get CourseAdminFeedback
            CourseAdminFeedback courseAdminFeedback = courseAdminFeedbackService.getById(course.getId());
            // get user
            User user = userService.getUserByCourseId(course.getId());
            courseResponse = new CourseResponse(course, user, categories, courseAdminFeedback, false);

        } else {
            // get Course
            Course course = courseService.getById(id);
            Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

            // get all categories
            List<Category> categories = categoryService.getAllByCourseId(course.getId());
            // get CourseAdminFeedback
            CourseAdminFeedback courseAdminFeedback = courseAdminFeedbackService.getById(course.getId());
            // get user
            User user = userService.getUserByCourseId(course.getId());

            boolean payment = false;
            List<Orders> ordersList = ordersService.getAllByCourseIdAndUserId(course.getId(), authUser.getId());
            if (!ordersList.isEmpty()) {
                for (Orders orders : ordersList) {
                    if (OrderStatus.PAID.equals(orders.getOrderStatus())) {

                        payment = true;
                    } else {
                        payment = false;
                    }
                }
            }

            courseResponse = new CourseResponse(course, user, categories, courseAdminFeedback, payment);
        }

        return responseUtil.successResponse(courseResponse);
    }

    /**
     * Get Detail Course API
     *
     * @param id
     * @return
     */

    @GetMapping(path = "/{id}/public")
    @Operation(summary = "Get Detail Course Public")
    public ResponseEntity<RestAPIResponse> getDetailCoursePublic(
            @PathVariable(name = "id") String id // course id
    ) {
        // get Course
        Course course = courseService.getByIdAndStatus(id, AppStatus.ACTIVE);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        // get all categories
        List<Category> categories = categoryService.getAllByCourseId(course.getId());
        // get CourseAdminFeedback
        CourseAdminFeedback courseAdminFeedback = courseAdminFeedbackService.getById(course.getId());
        // get user
        User user = userService.getUserByCourseId(course.getId());

        MediaInfo mediaInfo = mediaInfoService.getMediaInfosByCourseIdAndStatus(id);

        return responseUtil.successResponse(new CourseResponse(course, user, categories, courseAdminFeedback, mediaInfo));
    }

    /**
     * Get Detail Course API
     *
     * @param courseId
     * @return
     */
    @AuthorizeValidator({UserType.LEARNER})
    @GetMapping(path = "/{course_id}/status")
    @Operation(summary = "Get Detail Course Status")
    public ResponseEntity<RestAPIResponse> getDetailCourseStatus(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "course_id") String courseId // course id
    ) {
        // get Course
        Course course = courseService.getCourseByUserId(courseId, authUser.getId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        return responseUtil.successResponse(course);
    }

    /**
     * Get List Course API
     *
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping("all")
    @Operation(summary = "Get List Course")
    public ResponseEntity<RestAPIResponse> getListCourse(
    ) {
        List<CourseResponse> courseResponses = new ArrayList<>();
        // get list course
        List<Course> courseList = courseService.getAllByStatusOrderByCreatedDateDesc();

        for (Course course : courseList) {
            // get all categories
            List<Category> categories = categoryService.getAllByCourseId(course.getId());

            // get CourseAdminFeedback
            CourseAdminFeedback courseAdminFeedback = courseAdminFeedbackService.getById(course.getId());
            // get user
            User user = userService.getUserByCourseId(course.getId());

            CourseResponse courseResponse = new CourseResponse(course, user, categories, courseAdminFeedback);

            courseResponses.add(courseResponse);
        }

        return responseUtil.successResponse(courseResponses);
    }

    /**
     * Delete Course API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping()
    @Operation(summary = "Delete Course")
    public ResponseEntity<RestAPIResponse> deleteCourse(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids // course ids
    ) {
        if (UserType.SYSTEM_ADMIN.equals(authUser.getType())) {
            courseService.updateByIdInToInactive(ids);
        } else {
            courseService.updateByIdInToInactiveAndUserId(ids, authUser.getId());
        }
        return responseUtil.successResponse("Ok");
    }

    /**
     * Get Paging Courses API
     *
     * @param searchKey
     * @param userId
     * @param sortFieldCourse
     * @param sortDirection
     * @param courseStatuses
     * @param pageNumber
     * @param pageSize
     * @param authUser
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @GetMapping
    @Operation(summary = "Get Paging Courses")
    public ResponseEntity<RestAPIResponse> getPageCourses(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "user_id", required = false) String userId,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldCourse sortFieldCourse, //title,subtitle,createdDate
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,// ASC or DESC
            @RequestParam(name = "course_status", required = false, defaultValue = "") List<CourseStatus> courseStatuses, // REJECT,WAITING_FOR_APPROVAL,PUBLISH,DRAFT
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize,
            @Parameter(hidden = true) @AuthSession AuthUser authUser

    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_REQUEST,
                APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE, pageNumber, pageSize);

        // check status
        if (courseStatuses == null || courseStatuses.isEmpty()) {
            courseStatuses = Arrays.asList(CourseStatus.values());
        }
        // get page course
        Page<CourseResponse> courseResponses;

        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                courseResponses = courseService.getCoursesPage(userId, searchKey, sortFieldCourse, sortDirection, courseStatuses, pageNumber, pageSize);
                break;
            case INSTRUCTOR:
                courseResponses = courseService.getCoursesPage(authUser.getId(), searchKey, sortFieldCourse, sortDirection, courseStatuses, pageNumber, pageSize);
                break;
            default:
                throw new ApplicationException(RestAPIStatus.BAD_PARAMS);
        }
        return responseUtil.successResponse(new PagingResponse(courseResponses));
    }

    /**
     * Get Paging Courses Public API
     *
     * @param searchKey
     * @param sortFieldCourse
     * @param sortDirection
     * @param courseStatuses
     * @param courseLevels
     * @param languages
     * @param coursePrices
     * @param fromPrice
     * @param toPrice
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/public")
    @Operation(summary = "Get Paging Courses Public")
    public ResponseEntity<RestAPIResponse> getPageCoursesPublic(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldCourse sortFieldCourse, //title,subtitle,createdDate
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,// ASC or DESC
            @RequestParam(name = "course_status", required = false, defaultValue = "") List<CourseStatus> courseStatuses, // REJECT,WAITING_FOR_APPROVAL,PUBLISH,DRAFT
            @RequestParam(name = "filter_levels", required = false, defaultValue = "") List<CourseLevel> courseLevels, // BEGINNER, INTERMEDIATE, EXPERT, ALL_LEVELS
            @RequestParam(name = "filter_languages", required = false, defaultValue = "") List<Language> languages, // ENGLISH, JAPANESE, VIETNAMESE
            @RequestParam(name = "filter_price", required = false, defaultValue = "") List<CoursePrice> coursePrices, // FREE,INPUT_PRICE
            @RequestParam(name = "from_price", required = false) Double fromPrice,
            @RequestParam(name = "to_price", required = false) Double toPrice,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_REQUEST,
                APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE, pageNumber, pageSize);

        // get page course
        Page<CourseResponse> courseResponses = courseService.getCoursesPagePublic(searchKey, sortFieldCourse, sortDirection,
                courseStatuses, courseLevels, languages, coursePrices, fromPrice, toPrice, pageNumber, pageSize);

        return responseUtil.successResponse(new PagingResponse(courseResponses));
    }

    /**
     * Get Number Of User API
     *
     * @param course_id
     * @return
     */
    @GetMapping(path = "{course_id}/number-of-learner/public")
    @Operation(summary = "Get Number Of User")
    public ResponseEntity<RestAPIResponse> getNumberOfUser(
            @PathVariable(name = "course_id") String course_id // course id
    ) {
        //get user
        Course course = courseService.getByIdAndStatus(course_id, AppStatus.ACTIVE);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        // get numberOfUser
        Long numberOfUser = courseService.getNumberOfUser(course.getId());

        if (numberOfUser != null) {

            return responseUtil.successResponse(numberOfUser);
        } else {

            return responseUtil.successResponse(0);
        }
    }
}


