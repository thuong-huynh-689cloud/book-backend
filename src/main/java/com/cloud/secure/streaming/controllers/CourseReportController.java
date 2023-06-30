package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.CourseReportHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCourseReportRequest;
import com.cloud.secure.streaming.entities.CourseReport;
import com.cloud.secure.streaming.services.CourseReportService;
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
@RequestMapping(ApiPath.COURSE_REPORT_API)
@Slf4j
public class CourseReportController extends AbstractBaseController {

    final CourseReportService courseReportService;
    final CourseReportHelper courseReportHelper;

    public CourseReportController(CourseReportService courseReportService, CourseReportHelper courseReportHelper) {
        this.courseReportService = courseReportService;
        this.courseReportHelper = courseReportHelper;
    }

    @AuthorizeValidator({UserType.LEARNER})
    @PostMapping()
    @Operation(summary = "Create Course Report")
    public ResponseEntity<RestAPIResponse> createCourseReport(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateCourseReportRequest createCourseReportRequest
    ) {
        CourseReport courseReport = courseReportHelper.createCourseReport(createCourseReportRequest, authUser.getId());
        courseReportService.save(courseReport);

        return responseUtil.successResponse(courseReport);
    }
}
