package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.SectionHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateSectionRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateSectionRequest;
import com.cloud.secure.streaming.controllers.model.response.LectureResponse;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.controllers.model.response.SectionResponse;
import com.cloud.secure.streaming.controllers.model.response.UserResponse;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.LectureService;
import com.cloud.secure.streaming.services.MediaInfoService;
import com.cloud.secure.streaming.services.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(ApiPath.SECTION_API)
@Slf4j
public class SectionController extends AbstractBaseController {

    final SectionService sectionService;
    final SectionHelper sectionHelper;
    final CourseService courseService;
    final LectureService lectureService;
    final MediaInfoService mediaInfoService;

    public SectionController(SectionService sectionService, SectionHelper sectionHelper, CourseService courseService, LectureService lectureService, MediaInfoService mediaInfoService) {
        this.sectionService = sectionService;
        this.sectionHelper = sectionHelper;
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.mediaInfoService = mediaInfoService;
    }

    /**
     * Create Section API
     *
     * @param createSectionRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping()
    @Operation(summary = "Create Section")
    public ResponseEntity<RestAPIResponse> createSection(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreateSectionRequest createSectionRequest
    ) {
        // get course
        Course course = courseService.getById(createSectionRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        //check permission
        if (authUser.getType().equals(UserType.INSTRUCTOR) && !authUser.getId().equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_CREATE_YOUR_OWN_INFORMATION);
        }
        //check course status
        if (course.getCourseStatus() != null && course.getCourseStatus().equals(CourseStatus.PUBLISH)) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CANNOT_CREATE_WHEN_STATUS_IS_PUBLISHED);
        }
        // create section
        Section section = sectionHelper.createSection(createSectionRequest.getCourseId(), createSectionRequest);
        sectionService.save(section);
        return responseUtil.successResponse(section);
    }

    /**
     * Get Section API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Section")
    public ResponseEntity<RestAPIResponse> getSection(
            @PathVariable(name = "id") String id
    ) {
        //get section
        Section section = sectionService.getById(id);
        Validator.notNull(section, RestAPIStatus.NOT_FOUND, APIStatusMessage.SECTION_NOT_FOUND);
        //get lectures by section id
        List<Lecture> lectures = lectureService.getAllBySectionIdOrderByCreatedDateDesc(id);
        List<LectureResponse> lectureResponses = new ArrayList<>();

        if (!lectures.isEmpty()) {
            lectures.forEach(lecture -> {
                MediaInfo mediaInfo = mediaInfoService.getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(lecture.getId(),
                        MediaInfoType.LECTURE, AppStatus.ACTIVE);
                lectureResponses.add(new LectureResponse(lecture, mediaInfo));
            });
        }

//        List<String> lectureIds = lectures.stream().map(Lecture::getId).collect(Collectors.toList());
//        List<MediaInfo> mediaInfoList = mediaInfoService.getAllByLectureIdInAndTypeAndStatus(lectureIds, MediaInfoType.LECTURE, AppStatus.ACTIVE);
//        Map<String, MediaInfo> mediaInfoMap = IntStream.range(0, lectureIds.size())
//                .boxed()
//                .collect(Collectors.toMap(lectureIds::get, mediaInfoList::get));
//        lectures.forEach(lecture -> {
//            LectureResponse lectureResponse = new LectureResponse(lecture, mediaInfoMap.getOrDefault(lecture.getId(), null));
//            lectureResponses.add(lectureResponse);
//        });
        return responseUtil.successResponse(new SectionResponse(section, lectureResponses));
    }

    /**
     * Get List Sections
     *
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping("/all")
    @Operation(summary = "Get Sections")
    public ResponseEntity<RestAPIResponse> getSections(
            @RequestParam("course_id") String courseId
    ) {
        // check course id
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // get all section by course id
        List<Section> sections = sectionService.getAllByCourseIdOrderByCreatedDateDesc(courseId);
        // get all lectures
        List<SectionResponse> sectionResponses = new ArrayList<>();
        if (!sections.isEmpty()) {
            List<String> sectionIds = sections.stream().map(Section::getId).collect(Collectors.toList());
            List<Lecture> lectures = lectureService.getAllBySectionIdIn(sectionIds);
            List<LectureResponse> lectureResponses = new ArrayList<>();
            if (!lectures.isEmpty()) {
                lectures.forEach(lecture -> {
                    MediaInfo mediaInfo = mediaInfoService.getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(lecture.getId(),
                            MediaInfoType.LECTURE, AppStatus.ACTIVE);
                    lectureResponses.add(new LectureResponse(lecture, mediaInfo));
                });
            }
//            List<String> lectureIds = lectures.stream().map(Lecture::getId).collect(Collectors.toList());
//            List<MediaInfo> mediaInfoList = mediaInfoService.getAllByLectureIdInAndTypeAndStatus(lectureIds, MediaInfoType.LECTURE, AppStatus.ACTIVE);
//            Map<String, MediaInfo> mediaInfoMap = IntStream.range(0, lectureIds.size())
//                    .boxed()
//                    .collect(Collectors.toMap(lectureIds::get, mediaInfoList::get));
//
//            lectures.forEach(lecture -> {
//                LectureResponse lectureResponse = new LectureResponse(lecture, mediaInfoMap.getOrDefault(lecture.getId(), null));
//                lectureResponses.add(lectureResponse);
//            });
            Map<String, List<LectureResponse>> lectureResponseMap = lectureResponses.stream().collect(Collectors.groupingBy(LectureResponse::getSectionId));
            sections.forEach(section -> {
                List<LectureResponse> lectureResponseList = lectureResponseMap.getOrDefault(section.getId(), null);
                SectionResponse sectionResponse = new SectionResponse(section, lectureResponseList);
                sectionResponses.add(sectionResponse);
            });
        }
        return responseUtil.successResponse(sectionResponses);
    }

    /**
     * Get Sections
     *
     * @param searchKey
     * @param sortFieldSection
     * @param sortDirection
     * @param displayStatuses
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping
    @Operation(summary = "Get Paging Sections")
    public ResponseEntity<RestAPIResponse> getPagingSections(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldSection sortFieldSection,
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(name = "display_statuses", required = false) List<DisplayStatus> displayStatuses, //  SHOW ,HIDE
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE,
                pageNumber, pageSize);
        // check display statuses
        if (displayStatuses == null || displayStatuses.isEmpty()) {
            displayStatuses = Arrays.asList(DisplayStatus.values());
        }
        //search, sort
        Page<Section> sectionPage = sectionService.getPage(searchKey, sortFieldSection,
                sortDirection, displayStatuses, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(sectionPage));
    }

    /**
     * update Section API
     *
     * @param id
     * @param updateSectionRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Section")
    public ResponseEntity<RestAPIResponse> updateSection(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateSectionRequest updateSectionRequest
    ) {
        //get section
        Section section = sectionService.getById(id);
        Validator.notNull(section, RestAPIStatus.NOT_FOUND, APIStatusMessage.SECTION_NOT_FOUND);
        // get course to check Permission
        Course course = courseService.getById(section.getCourseId());
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (course != null) {
                if (!authUser.getId().equals(course.getUserId())) {
                    throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
                }
            }
        }
        //check course status
        if (course.getCourseStatus() != null && course.getCourseStatus().equals(CourseStatus.PUBLISH)) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CANNOT_UPDATE_WHEN_STATUS_IS_PUBLISHED);
        }
        // update section name
        section = sectionHelper.updateSection(section, updateSectionRequest);
        sectionService.save(section);

        return responseUtil.successResponse(section);
    }

    /**
     * Delete Section API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping
    @Operation(summary = "Delete Section")
    public ResponseEntity<RestAPIResponse> deleteSection(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        //check section
        List<Section> sections = sectionService.getAllByIdIn(ids);
        //check permission
        if (sections != null && !sections.isEmpty()) {
            for (Section section : sections) {
                Course course = courseService.getById(section.getCourseId());
                if (authUser.getType().equals(UserType.INSTRUCTOR)) {
                    if (course != null) {
                        if (!authUser.getId().equals(course.getUserId())) {
                            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
                        }
                    }
                }
            }
        }
        //delete section
        sectionService.deleteByIdIn(ids);
        //delete lectures
        lectureService.deleteAllBySectionIdIn(ids);

        return responseUtil.successResponse("OK");
    }

    /**
     *  Get Sections Public API
     *
     * @param courseId
     * @return
     */
    @GetMapping(path = "/public")
    @Operation(summary = "Get Sections Public")
    public ResponseEntity<RestAPIResponse> getSectionsPublic(
            @RequestParam("course_id") String courseId
    ) {
        // check course id
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // get all section by course id
        List<Section> sections = sectionService.getAllByCourseIdOrderByCreatedDateDesc(courseId);
        // get all lectures
        List<SectionResponse> sectionResponses = new ArrayList<>();
        if (!sections.isEmpty()) {
            List<LectureResponse> lectureResponses = new ArrayList<>();

            List<String> sectionIds = sections.stream().map(Section::getId).collect(Collectors.toList());

            List<Lecture> lectures = lectureService.getAllBySectionIdIn(sectionIds);

            if (!lectures.isEmpty()) {
                lectures.forEach(lecture -> {
                    lectureResponses.add(new LectureResponse(lecture));
                });
            }
            Map<String, List<LectureResponse>> lectureResponseMap = lectureResponses.stream().collect(Collectors.groupingBy(LectureResponse::getSectionId));
            sections.forEach(section -> {
                List<LectureResponse> lectureResponseList = lectureResponseMap.getOrDefault(section.getId(), null);
                SectionResponse sectionResponse = new SectionResponse(section, lectureResponseList);
                sectionResponses.add(sectionResponse);
            });
        }
        return responseUtil.successResponse(sectionResponses);
    }
}
