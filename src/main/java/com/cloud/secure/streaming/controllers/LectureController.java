package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.LectureHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateLectureRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateLectureRequest;
import com.cloud.secure.streaming.controllers.model.response.LectureResponse;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.LectureService;
import com.cloud.secure.streaming.services.MediaInfoService;
import com.cloud.secure.streaming.services.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.LECTURE_API)
@Slf4j
public class LectureController extends AbstractBaseController {

    final LectureService lectureService;
    final LectureHelper lectureHelper;
    final SectionService sectionService;
    final CourseService courseService;
    final MediaInfoService mediaInfoService;

    public LectureController(LectureService lectureService,
                             LectureHelper lectureHelper,
                             SectionService sectionService,
                             CourseService courseService,
                             MediaInfoService mediaInfoService) {
        this.lectureService = lectureService;
        this.lectureHelper = lectureHelper;
        this.sectionService = sectionService;
        this.courseService = courseService;
        this.mediaInfoService = mediaInfoService;
    }

    /**
     * Create Lecture API
     *
     * @param authUser
     * @param createLectureRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping()
    @Operation(summary = "Create Lecture")
    public ResponseEntity<RestAPIResponse> createLecture(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateLectureRequest createLectureRequest
    ) {
//        //validate lecture type for duration and number of question
//        if (createLectureRequest.getContentType() != null && createLectureRequest.getContentType().equals(ContentType.ASSESSMENT)) {
//            if (createLectureRequest.getDuration() == null || createLectureRequest.getNumberOfQuestion() == null) {
//                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Duration and number of question must be greater than or equal 1");
//            }
//        }
//        if (createLectureRequest.getContentType() != null && createLectureRequest.getContentType().equals(ContentType.ARTICLE)) {
//            if (createLectureRequest.getDuration() == null) {
//                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Duration must be greater than or equal 1");
//            }
//        }
        //validate section id
        Section section = sectionService.getById(createLectureRequest.getSectionId());
        Validator.notNull(section, RestAPIStatus.NOT_FOUND, APIStatusMessage.SECTION_NOT_FOUND);
        //check permission
        Course course = courseService.getByCourseSectionId(createLectureRequest.getSectionId());
        if (course != null) {
            if (authUser.getType().equals(UserType.INSTRUCTOR) && !authUser.getId().equals(course.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.USER_ONLY_CREATE_RESOURCE_FOR_THEIR_COURSE);
            }
        }
        //check course status
        if (course.getCourseStatus() != null && course.getCourseStatus().equals(CourseStatus.PUBLISH)) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CANNOT_CREATE_WHEN_STATUS_IS_PUBLISHED);
        }
        // create lecture
        Lecture lecture = lectureHelper.createLecture(createLectureRequest.getSectionId(), createLectureRequest);
        lectureService.save(lecture);
        return responseUtil.successResponse(lecture);
    }

    /**
     * Get Lecture API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping("/{id}")
    @Operation(summary = "Get Lecture")
    public ResponseEntity<RestAPIResponse> getLecture(
            @PathVariable("id") String id
    ) {
        Lecture lecture = lectureService.getById(id);
        Validator.notNull(lecture, RestAPIStatus.NOT_FOUND, APIStatusMessage.LECTURE_NOT_FOUND);

        MediaInfo mediaInfo = mediaInfoService.getMediaInfoByCourseIdOrLectureIdAndMediaInfoType(id, MediaInfoType.LECTURE);

        return responseUtil.successResponse(new LectureResponse(lecture, mediaInfo));
    }

    /**
     * Get All Lecture API
     *
     * @param sectionId
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping()
    @Operation(summary = "Get Lectures")
    public ResponseEntity<RestAPIResponse> getLectures(
            @RequestParam("section_id") String sectionId
    ) {
        // check section id
        Section section = sectionService.getById(sectionId);
        Validator.notNull(section, RestAPIStatus.NOT_FOUND, APIStatusMessage.SECTION_NOT_FOUND);
        // get all lectures by section id
        List<Lecture> lectures = lectureService.getAllBySectionIdOrderByCreatedDateDesc(sectionId);
        List<LectureResponse> lectureResponses = new ArrayList<>();
        if (!lectures.isEmpty()) {
            lectures.forEach(lecture -> {
                MediaInfo mediaInfo = mediaInfoService.getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(lecture.getId(),
                        MediaInfoType.LECTURE, AppStatus.ACTIVE);
                lectureResponses.add(new LectureResponse(lecture, mediaInfo));
            });
        }
        return responseUtil.successResponse(lectureResponses);

    }

    /**
     * Update Lecture API
     *
     * @param authUser
     * @param id
     * @param updateLectureRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Transactional
    @Operation(summary = "Update Lecture")
    public ResponseEntity<RestAPIResponse> updateLecture(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateLectureRequest updateLectureRequest
    ) {
        //get lecture
        Lecture lecture = lectureService.getById(id);
        Validator.notNull(lecture, RestAPIStatus.NOT_FOUND, APIStatusMessage.LECTURE_NOT_FOUND);
//        // validate lecture type for duration and number of question
//        if (updateLectureRequest.getContentType() != null && updateLectureRequest.getContentType().equals(ContentType.ASSESSMENT)) {
//            if (updateLectureRequest.getDuration() == null || updateLectureRequest.getNumberOfQuestion() == null) {
//                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Duration and number of question must be greater or equal 1");
//            }
//        }
//        if (updateLectureRequest.getContentType() != null && updateLectureRequest.getContentType().equals(ContentType.ARTICLE)) {
//            if (updateLectureRequest.getDuration() == null) {
//                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Duration must be greater than or equal 1");
//            }
//        }
        // check permission
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            Course course = courseService.getByLectureId(id);
            if (course != null && !authUser.getId().equals(course.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
            }
            //check course status
            if (course.getCourseStatus() != null && course.getCourseStatus().equals(CourseStatus.PUBLISH)) {
                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CANNOT_UPDATE_WHEN_STATUS_IS_PUBLISHED);
            }
        }
        //get video duration from media info
        int duration = 0;
        if (updateLectureRequest.getContentType() != null) {
            switch (updateLectureRequest.getContentType()) {
                case VIDEO: {
                    MediaInfo mediaInfo = mediaInfoService.getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(lecture.getId(), MediaInfoType.LECTURE, AppStatus.ACTIVE);
                    Validator.notNull(mediaInfo, RestAPIStatus.NOT_FOUND, APIStatusMessage.MEDIA_INFO_NOT_FOUND);
                    duration = mediaInfo.getDuration().intValue();
                    break;
                }
                case ARTICLE:
                case ASSESSMENT: {
                    if (updateLectureRequest.getDuration() != null) {
                        duration = updateLectureRequest.getDuration();
                    }
                    break;
                }
            }
        }
        // update lecture
        lecture = lectureHelper.updateLecture(lecture, updateLectureRequest, duration);
        // save
        lectureService.save(lecture);
        return responseUtil.successResponse(lecture);
    }

    /**
     * Delete Lectures API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping
    @Operation(summary = "Delete Lectures")
    public ResponseEntity<RestAPIResponse> deleteLectures(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        // get list lecture delete
        List<Lecture> lectures = lectureService.getAllByIdIn(ids);
        //check permission
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (lectures != null && !lectures.isEmpty()) {
                List<Course> courses = courseService.getAllByLectureIdIn(ids);
                if (!courses.isEmpty()) {
                    List<Course> coursesWithWrongUserId = courses.stream()
                            .filter(course -> !authUser.getId().equals(course.getUserId()))
                            .collect(Collectors.toList());
                    if (!coursesWithWrongUserId.isEmpty()) {
                        throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
                    }
                }
            }
        }
        // delete lecture
        lectureService.deleteByIdIn(ids);

        return responseUtil.successResponse("OK");
    }
}
