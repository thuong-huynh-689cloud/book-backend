package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.IntendedLearnerHelper;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.IntendedLearner;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.IntendedLearnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.INTENDED_LEARNER_API)
@Slf4j
public class IntendedLearnerController extends AbstractBaseController {

    final IntendedLearnerService intendedLearnerService;
    final IntendedLearnerHelper intendedLearnerHelper;
    final CourseService courseService;

    public IntendedLearnerController(IntendedLearnerService intendedLearnerService,
                                     IntendedLearnerHelper intendedLearnerHelper,
                                     CourseService courseService) {
        this.intendedLearnerService = intendedLearnerService;
        this.intendedLearnerHelper = intendedLearnerHelper;
        this.courseService = courseService;
    }

    /**
     * Get Detail Intended Learner API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Intended Learner")
    public ResponseEntity<RestAPIResponse> getIntendedLearner(
            @PathVariable(name = "id") String id

    ) {
        // get intended learner
        IntendedLearner intendedLearner = intendedLearnerService.getById(id);
        Validator.notNull(intendedLearner, RestAPIStatus.NOT_FOUND, APIStatusMessage.INTENDED_LEARNER_NOT_FOUND);

        return responseUtil.successResponse(intendedLearner);
    }

    /**
     * Get List Intended Learner API
     *
     * @param courseId
     * @param intendedLearnerTypes
     * @return
     */

    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping()
    @Operation(summary = "Get List Intended Learner")
    public ResponseEntity<RestAPIResponse> getListIntendedLearner(
            @RequestParam(name = "course_id") String courseId,
            @RequestParam(name = "type", required = false) List<IntendedLearnerType> intendedLearnerTypes
    ) {
        // check type
        if (intendedLearnerTypes == null || intendedLearnerTypes.isEmpty()) {
            intendedLearnerTypes = Arrays.asList(IntendedLearnerType.values());
        }
        // check course
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // get all intended learner by type
        List<IntendedLearner> intendedLearners = intendedLearnerService.getAllByCourseIdAndTypeOrderByCreatedDateDesc(courseId, intendedLearnerTypes);

        return responseUtil.successResponse(intendedLearners);
    }

    /**
     * Get List Intended Learner Public
     *
     * @param courseId
     * @param intendedLearnerTypes
     * @return
     */
    @GetMapping("/public")
    @Operation(summary = "Get List Intended Learner Public")
    public ResponseEntity<RestAPIResponse> getListIntendedLearnerPublic(
            @RequestParam(name = "course_id") String courseId,
            @RequestParam(name = "type", required = false) List<IntendedLearnerType> intendedLearnerTypes
    ) {
        // check type
        if (intendedLearnerTypes == null || intendedLearnerTypes.isEmpty()) {
            intendedLearnerTypes = Arrays.asList(IntendedLearnerType.values());
        }
        // check course
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // get all intended learner by type
        List<IntendedLearner> intendedLearners = intendedLearnerService.getAllByCourseIdAndTypeOrderByCreatedDateDesc(courseId, intendedLearnerTypes);

        return responseUtil.successResponse(intendedLearners);
    }

    /**
     * Implement Intended Learner API
     *
     * @param authUser
     * @param implementIntendedLearnerRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping()
    @Operation(summary = "Implement Intended Learner")
    public ResponseEntity<RestAPIResponse> implementIntendedLearner(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody ImplementIntendedLearnerRequest implementIntendedLearnerRequest
    ) {
        String userId = null;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                break;
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        Course course = courseService.getById(implementIntendedLearnerRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        if (userId != null && !course.getUserId().equals(userId)) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CANNOT_IMPLEMENTS_FOR_OTHER_USERS);
        }
        // get list exist
        List<IntendedLearner> intendedLearnerList = intendedLearnerService.getAllByCourseId(course.getId());
        List<CreateIntendedLearnerRequest> createIntendedLearnerRequests = implementIntendedLearnerRequest.getCreateIntendedLearnerRequests();
        List<IntendedLearner> intendedLearners = new ArrayList<>();

        if (intendedLearnerList.isEmpty()) {
            intendedLearners = intendedLearnerHelper.createIntendedLearner(course.getId(),
                    implementIntendedLearnerRequest.getCreateIntendedLearnerRequests());
        } else {
            // add new: filter list not contain ID --> add new
            List<CreateIntendedLearnerRequest> intendedLearnerAdd = createIntendedLearnerRequests.stream().filter(createIntendedLearnerRequest
                            -> (createIntendedLearnerRequest.getId() == null || createIntendedLearnerRequest.getId().isEmpty()))
                    .collect(Collectors.toList());
            // do create
            if (!intendedLearnerAdd.isEmpty()){
                intendedLearners = intendedLearnerHelper.createIntendedLearner(course.getId(), intendedLearnerAdd);
            }
            createIntendedLearnerRequests.removeAll(intendedLearnerAdd);
            // update
            List<String> idsRequest = createIntendedLearnerRequests.stream().distinct()
                    .map(CreateIntendedLearnerRequest::getId).collect(Collectors.toList());
            List<IntendedLearner> listUpdate = intendedLearnerList.stream().filter(intendedLearner
                    -> idsRequest.contains(intendedLearner.getId())).collect(Collectors.toList());
            // do update
            if (!listUpdate.isEmpty()) {
                listUpdate = intendedLearnerHelper.updateIntendedLearner(listUpdate, createIntendedLearnerRequests);
                intendedLearnerService.saveAll(listUpdate);
            }
            // delete ids not in
            List<String> idsDelete = intendedLearnerList.stream().map(IntendedLearner::getId)
                    .filter(id -> !idsRequest.contains(id)).collect(Collectors.toList());
            if (!idsDelete.isEmpty()) {
                intendedLearnerService.deleteIntendedLearnerByIdIn(idsDelete);
            }
        }
        //save
        if (!intendedLearners.isEmpty()){
            intendedLearnerService.saveAll(intendedLearners);
        }
        return responseUtil.successResponse("OK");
    }

    /**
     * Update Intended Learner API
     *
     * @param authUser
     * @param id
     * @param updateIntendedLearnerRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Intended Learner")
    public ResponseEntity<RestAPIResponse> updateIntendedLearner(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateIntendedLearnerRequest updateIntendedLearnerRequest

    ) {
        String userId = null;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                break;
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        IntendedLearner intendedLearner = intendedLearnerService.getById(id);
        Validator.notNull(intendedLearner, RestAPIStatus.NOT_FOUND, APIStatusMessage.INTENDED_LEARNER_NOT_FOUND);
        Course course = courseService.getById(intendedLearner.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        if (userId != null && !course.getUserId().equals(userId)) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        intendedLearner = intendedLearnerHelper.updateIntendedLearner(intendedLearner, updateIntendedLearnerRequest);
        // save
        intendedLearnerService.save(intendedLearner);

        return responseUtil.successResponse(intendedLearner);
    }

    /**
     * Delete Intended Learner API
     *
     * @param authUser
     * @param courseId
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping()
    @Operation(summary = "Delete Intended Learner")
    public ResponseEntity<RestAPIResponse> deleteIntendedLearner(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "course_id", required = false) String courseId,
            @RequestParam(name = "ids") List<String> ids // intendedLearner ids
    ) {
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                intendedLearnerService.deleteIntendedLearnerByIdIn(ids);
                break;
            case INSTRUCTOR:
                if (courseId == null || courseId.isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS,APIStatusMessage.MUST_HAVE_COURSE_ID);
                }
                Course course = courseService.getById(courseId);
                if (!course.getUserId().equals(authUser.getId())) {
                    throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
                }
                Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
                intendedLearnerService.deleteByIdIn(ids, course.getId());
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        return responseUtil.successResponse("OK");
    }

    /**
     * Create Course Preview API
     *
     * @param authUser
     * @param createCoursePreviewRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/course-preview")
    @Operation(summary = "Implement Course Preview")
    public ResponseEntity<RestAPIResponse> implementCoursePreview(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateCoursePreviewRequest createCoursePreviewRequest
    ){
        String userId = null;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                break;
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        Course course = courseService.getById(createCoursePreviewRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        if (userId != null && !course.getUserId().equals(userId)) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CANNOT_IMPLEMENTS_FOR_OTHER_USERS);
        }
        // get list exist
        List<IntendedLearner> coursePreviewList = intendedLearnerService.getAllByCourseIdAndType(course.getId(), IntendedLearnerType.PREVIEW);
        List<IntendedLearner> coursePreviews = new ArrayList<>();
        List<CreateDetailCoursePreview> request = createCoursePreviewRequest.getCreateDetailCoursePreviews();
        if (coursePreviewList.isEmpty()) {
            coursePreviews = intendedLearnerHelper.createCoursePreview(course.getId(), createCoursePreviewRequest.getCreateDetailCoursePreviews());
        } else {
            // add new: filter list not contain ID --> add new
            List<CreateDetailCoursePreview> coursePreviewAdd = createCoursePreviewRequest.getCreateDetailCoursePreviews()
                    .stream().filter(coursePreviewRequest
                            -> (coursePreviewRequest.getId() == null || coursePreviewRequest.getId().isEmpty()))
                    .collect(Collectors.toList());
            // do create
            if (!coursePreviewAdd.isEmpty()){
                coursePreviews = intendedLearnerHelper.createCoursePreview(course.getId(), coursePreviewAdd);
            }
            request.removeAll(coursePreviewAdd);
            // update
            List<String> idsRequest = request.stream().distinct()
                    .map(CreateDetailCoursePreview::getId).collect(Collectors.toList());
            List<IntendedLearner> listUpdate = coursePreviewList.stream().filter(intendedLearner
                    -> idsRequest.contains(intendedLearner.getId())).collect(Collectors.toList());
            // do update
            if (!listUpdate.isEmpty()) {
                listUpdate = intendedLearnerHelper.updateCoursePreview(listUpdate, request);
                intendedLearnerService.saveAll(listUpdate);
            }
            // delete ids not in
            List<String> idsDelete = coursePreviewList.stream().map(IntendedLearner::getId)
                    .filter(id -> !idsRequest.contains(id)).collect(Collectors.toList());
            if (!idsDelete.isEmpty()) {
                intendedLearnerService.deleteIntendedLearnerByIdIn(idsDelete);
            }
        }
        //save
        if (!coursePreviews.isEmpty()){
            intendedLearnerService.saveAll(coursePreviews);
        }
        return responseUtil.successResponse(coursePreviews);
    }
}

