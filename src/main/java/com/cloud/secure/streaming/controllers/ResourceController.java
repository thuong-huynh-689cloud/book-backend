package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.Constant;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.ResourceHelper;
import com.cloud.secure.streaming.controllers.helper.UploadHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateResourceRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateResourceRequest;
import com.cloud.secure.streaming.controllers.model.response.ResourceResponse;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.Lecture;
import com.cloud.secure.streaming.entities.Resource;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.LectureService;
import com.cloud.secure.streaming.services.ResourceService;
import com.cloud.secure.streaming.services.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.RESOURCE_API)
@Slf4j
public class ResourceController extends AbstractBaseController {

    final UploadHelper uploadHelper;
    final ResourceHelper resourceHelper;
    final ResourceService resourceService;
    final CourseService courseService;
    final SectionService sectionService;
    final LectureService lectureService;

    public ResourceController(UploadHelper uploadHelper,
                              ResourceHelper resourceHelper,
                              ResourceService resourceService,
                              CourseService courseService,
                              SectionService sectionService,
                              LectureService lectureService
    ) {
        this.uploadHelper = uploadHelper;
        this.resourceHelper = resourceHelper;
        this.resourceService = resourceService;
        this.courseService = courseService;
        this.sectionService = sectionService;
        this.lectureService = lectureService;
    }

    /**
     * Create Resource API
     *
     * @param authUser
     * @param createResourceRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping()
    @Operation(summary = "Create Resource")
    public ResponseEntity<RestAPIResponse> createResource(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid CreateResourceRequest createResourceRequest
    ) {
//        // check resource TYPE
//        switch (createResourceRequest.getType()) {
//            case FILE:
//            case SOURCE_CODE:
//                // resource type file does not have link
//                if (createResourceRequest.getExternalLink() != null) {
//                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.THIS_RESOURCE_TYPE_DOES_NOT_HAVE_LINK);
//                }
//                // check must have file name
//                if (createResourceRequest.getFileName() == null || createResourceRequest.getFileName().isEmpty()) {
//                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.THIS_RESOURCE_TYPE_MUST_NOT_FILE_NAME);
//                }
//                break;
//            case EXTERNAL_RESOURCE: {
//                // check title not null
//                if (createResourceRequest.getTitle() == null || createResourceRequest.getTitle().isEmpty()) {
//                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.EXTERNAL_RESOURCE_MUST_HAVE_TITLE);
//                }
//                // check url pattern
//                Validator.mustMatch(createResourceRequest.getExternalLink(), Constant.URL_PATTERN,
//                        RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_URL);
//                break;
//            }
//        }
        String userId = null;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                break;
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.USER_ONLY_CREATE_RESOURCE_FOR_THEIR_COURSE);
        }
        // validate lecture id
        Lecture lecture = lectureService.getByIdAndUserId(createResourceRequest.getLectureId(), userId);
        Validator.notNull(lecture, RestAPIStatus.NOT_FOUND, APIStatusMessage.LECTURE_NOT_FOUND);
        // create resource
        Resource resource = resourceHelper.createResource(createResourceRequest.getLectureId(), createResourceRequest);
        // save resource
        resourceService.save(resource);

        return responseUtil.successResponse(resource);
    }

    /**
     * Get Detail Resource API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Resource")
    public ResponseEntity<RestAPIResponse> getResource(
            @PathVariable(name = "id") String id // resource_id
    ) {
        Resource resource = resourceService.getById(id);
        Validator.notNull(resource, RestAPIStatus.NOT_FOUND, APIStatusMessage.RESOURCE_NOT_FOUND);

        return responseUtil.successResponse(resource);
    }

    /**
     * Get Resource API
     *
     * @param lectureId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping()
    @Operation(summary = "Get Resources")
    public ResponseEntity<RestAPIResponse> getResources(
            @RequestParam("lecture_id") String lectureId
    ) {
        // get all resource by lecture id
        List<Resource> resources = resourceService.getAllByLectureIdOrderByCreatedDateDesc(lectureId);

        return responseUtil.successResponse(resources);
    }

    /**
     * Update Resource API
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Resource")
    public ResponseEntity<RestAPIResponse> updateResource(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable("id") String id, // resource id
            @RequestBody @Valid UpdateResourceRequest updateResourceRequest
    ) {
        Resource resource = resourceService.getById(id);
        Validator.notNull(resource, RestAPIStatus.NOT_FOUND, APIStatusMessage.RESOURCE_NOT_FOUND);
        // check resource type
        switch (resource.getType()) {
            case FILE:
            case SOURCE_CODE:
                // resource type file does not have link
                if (updateResourceRequest.getExternalLink() != null || !updateResourceRequest.getExternalLink().isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.THIS_RESOURCE_TYPE_DOES_NOT_HAVE_LINK);
                }
                // check must have file name
                if (updateResourceRequest.getFileName() == null || updateResourceRequest.getFileName().isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.THIS_RESOURCE_TYPE_MUST_NOT_FILE_NAME);
                }
                break;
            case EXTERNAL_RESOURCE: {
                // check title not null
                if (updateResourceRequest.getTitle() == null || updateResourceRequest.getTitle().isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.THIS_RESOURCE_TYPE_MUST_HAVE_MUST_HAVE_TITLE);
                }
                // check url pattern
                Validator.mustMatch(updateResourceRequest.getExternalLink(), Constant.URL_PATTERN,
                        RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_URL);
                break;
            }
        }
        String userId = null;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                break;
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        Course course = courseService.getCourseByResourceId(userId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // update
        resource = resourceHelper.updateResource(resource, updateResourceRequest);
        // save
        resourceService.save(resource);

        return responseUtil.successResponse(resource);
    }

    /**
     * Delete Resource API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping()
    @Operation(summary = "Delete Resource")
    public ResponseEntity<RestAPIResponse> deleteResource(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids // resource ids
    ) {
        // get list resourceList delete
        List<Resource> resourceList = resourceService.getAllByIdIn(ids);
        // Check Permission
        if (authUser.getType().equals(UserType.INSTRUCTOR) && !resourceList.isEmpty()) {
            for (Resource resource : resourceList) {
                List<Course> courses = courseService.getAllByResourceId(resource.getId());
                if (!courses.isEmpty()) {
                    List<Course> coursesWithWrongUserId = courses.stream()
                            .filter(course -> !course.getUserId().equals(authUser.getId()))
                            .collect(Collectors.toList());
                    if (!coursesWithWrongUserId.isEmpty()) {
                        throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
                    }
                }
            }
        }
        // delete resource
        resourceService.deleteResourceByIdIn(ids);

        return responseUtil.successResponse("Ok");
    }

    /**
     * Get Resource Public API
     *
     * @param lectureId
     * @return
     */
    @GetMapping(path = "/public")
    @Operation(summary = "Get Resources Public")
    public ResponseEntity<RestAPIResponse> getResourcesPublic(
            @RequestParam("lecture_id") String lectureId
    ) {
        List<ResourceResponse> resourceResponses = new ArrayList<>();
        // get all resource by lecture id
        List<Resource> resources = resourceService.getAllByLectureIdOrderByCreatedDateDesc(lectureId);
        if (!resources.isEmpty()) {
            for (Resource resource : resources) {

                resourceResponses.add(new ResourceResponse(resource));
            }
        }

        return responseUtil.successResponse(resourceResponses);
    }
}
