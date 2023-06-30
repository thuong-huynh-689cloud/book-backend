package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.CourseReviewHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCourseReviewRequest;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.CourseReview;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.CourseReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.COURSE_REVIEW_API)
@Slf4j
public class CourseReviewController extends AbstractBaseController {

    final CourseReviewService courseReviewService;
    final CourseReviewHelper courseReviewHelper;
    final CourseService courseService;

    public CourseReviewController(CourseReviewService courseReviewService, CourseReviewHelper courseReviewHelper, CourseService courseService) {
        this.courseReviewService = courseReviewService;
        this.courseReviewHelper = courseReviewHelper;
        this.courseService = courseService;
    }

    /**
     *  Create Course Review API
     *
     * @param authUser
     * @param createCourseReviewRequest
     * @return
     */
    @AuthorizeValidator({UserType.LEARNER})
    @PostMapping()
    @Operation(summary = "Create Course Review")
    public ResponseEntity<RestAPIResponse> createCourseReview(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateCourseReviewRequest createCourseReviewRequest
    ) {
        // get course
        Course course = courseService.getById(createCourseReviewRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // create reviewCourse
        CourseReview courseReview = courseReviewHelper.createReviewCourse(createCourseReviewRequest, authUser.getId());
        courseReviewService.save(courseReview);

        return responseUtil.successResponse(courseReview);
    }
}