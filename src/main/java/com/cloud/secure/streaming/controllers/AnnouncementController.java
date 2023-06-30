package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.AnnouncementHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateAnnouncementRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateAnnouncementRequest;
import com.cloud.secure.streaming.controllers.model.response.AnnouncementResponse;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.Announcement;
import com.cloud.secure.streaming.entities.Comment;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.AnnouncementService;
import com.cloud.secure.streaming.services.CommentService;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(ApiPath.ANNOUNCEMENT_API)
@Slf4j
public class AnnouncementController extends AbstractBaseController {
    final AnnouncementHelper announcementHelper;
    final AnnouncementService announcementService;
    final CourseService courseService;
    final UserService userService;
    final CommentService commentService;

    public AnnouncementController(AnnouncementHelper announcementHelper,
                                  AnnouncementService announcementService,
                                  CourseService courseService,
                                  UserService userService,
                                  CommentService commentService
    ) {
        this.announcementHelper = announcementHelper;
        this.announcementService = announcementService;
        this.courseService = courseService;
        this.userService = userService;
        this.commentService = commentService;
    }

    /**
     * Create Announcement API
     *
     * @param createAnnouncementRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping
    @Operation(summary = "Create Announcement")
    public ResponseEntity<RestAPIResponse> createAnnouncement(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid CreateAnnouncementRequest createAnnouncementRequest
    ) {
        String userId;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                // check user Id
                if (createAnnouncementRequest.getUserId().isEmpty() || createAnnouncementRequest.getUserId() == null) {
                    throw new ApplicationException(RestAPIStatus.BAD_PARAMS, APIStatusMessage.USER_ID_IS_EMPTY);
                }
                userId = createAnnouncementRequest.getUserId();
                break;
            case INSTRUCTOR:
                userId = authUser.getId();
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        // check course id
        Course course = courseService.getById(createAnnouncementRequest.getCourseId());
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // users only create announcement for themselves in their course
        if (!userId.equals(course.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.USER_ONLY_CREATE_RESOURCE_FOR_THEIR_COURSE);
        }
        // get user
        User user = userService.getByIdAndStatus(createAnnouncementRequest.getUserId(), AppStatus.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        // create announcement
        Announcement announcement = announcementHelper.createAnnouncement(userId, createAnnouncementRequest);
        // save
        announcementService.save(announcement);

        return responseUtil.successResponse(announcement);
    }

    /**
     * Update Announcement API
     *
     * @param authUser
     * @param id
     * @param updateAnnouncementRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Announcement")
    public ResponseEntity<RestAPIResponse> updateAnnouncement(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id, // announcement id
            @Valid @RequestBody UpdateAnnouncementRequest updateAnnouncementRequest
    ) {
        Announcement announcement;
        switch (authUser.getType()) {
            case SYSTEM_ADMIN:
                announcement = announcementService.getById(id);
                break;
            case INSTRUCTOR:
                announcement = announcementService.getById(authUser.getId());
                break;
            default:
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        Validator.notNull(announcement, RestAPIStatus.NOT_FOUND, APIStatusMessage.ANNOUNCEMENT_NOT_FOUND);
        // update announcement
        announcement = announcementHelper.updateAnnouncement(announcement, updateAnnouncementRequest);
        // save
        announcementService.save(announcement);
        return responseUtil.successResponse(announcement);
    }

    /**
     * Get Announcement API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Announcement")
    public ResponseEntity<RestAPIResponse> getAnnouncement(
            @PathVariable(name = "id") String id // announcement id
    ) {
        // get announcement
        Announcement announcement = announcementService.getById(id);
        Validator.notNull(announcement, RestAPIStatus.NOT_FOUND, APIStatusMessage.ANNOUNCEMENT_NOT_FOUND);
        // get comments DESC by created date
        List<Comment> comments = commentService.getAllByAnnouncementIdOrderByCreatedDateDesc(id);

        return responseUtil.successResponse(new AnnouncementResponse(announcement, comments));
    }

    /**
     * Delete Announcement API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping
    @Operation(summary = "Delete Announcement")
    public ResponseEntity<RestAPIResponse> deleteAnnouncement(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "id") List<String> ids
    ) {
        // get announcement
        List<Announcement> announcements = announcementService.getAllByIdIn(ids.stream().distinct().collect(Collectors.toList()));
        // users only delete for themselves
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            List<Announcement> announcementListWithWrongUserId = announcements.stream()
                    .filter(announcement -> !announcement.getUserId().equals(authUser.getId()))
                    .collect(Collectors.toList());
            if (!announcementListWithWrongUserId.isEmpty()) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
            }
        }
        // delete announcement
        announcementService.deleteByIdIn(ids);
        // delete comments
        commentService.deleteAllByAnnouncementIdIn(ids);

        return responseUtil.successResponse("OK");
    }

    /**
     * Get Paging Announcement API
     *
     * @param courseId
     * @param searchKey
     * @param sortFieldAnnouncement
     * @param sortDirection
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping
    @Operation(summary = "Get Paging Announcement")
    public ResponseEntity<RestAPIResponse> getPagingAnnouncement(
            @RequestParam(name = "course_id") String courseId,
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldAnnouncement sortFieldAnnouncement,
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE,
                pageNumber, pageSize);
        // check course id
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        Page<Announcement> announcementPage = announcementService.getPage(courseId, searchKey, sortFieldAnnouncement,
                sortDirection, pageNumber, pageSize);
        // get list announcement ids
        List<String> announcementIds = announcementPage.getContent().stream().map(Announcement::getId).collect(Collectors.toList());
        // get all comment by list announcement ids
        List<Comment> commentList = commentService.getAllByAnnouncementIdIn(announcementIds);
        // map
        Map<String, List<Comment>> commentMap = commentList.stream().collect(Collectors.groupingBy(Comment::getAnnouncementId));
        Page<AnnouncementResponse> announcementResponsePage = announcementPage.map(announcement
                -> new AnnouncementResponse(announcement, commentMap.get(announcement.getId())));

        return responseUtil.successResponse(new PagingResponse(announcementResponsePage));
    }

    /**
     * Get Announcements API
     *
     * @param courseId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(ApiPath.ALL)
    @Operation(summary = "Get Announcements")
    public ResponseEntity<RestAPIResponse> getAnnouncements(
            @RequestParam(name = "course_id") String courseId
    ) {
        // check course id
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // get all announcements DESC by created date
        List<Announcement> announcements = announcementService.getAllByCourseIdOrderByCreatedDateDesc(courseId);
        List<String> announcementIds = announcements.stream().map(Announcement::getId).collect(Collectors.toList());
        List<Comment> commentList = commentService.getAllByAnnouncementIdIn(announcementIds);
        // map
        Map<String, List<Comment>> commentMap = commentList.stream().collect(Collectors.groupingBy(Comment::getAnnouncementId));
        // response
        List<AnnouncementResponse> announcementResponses = new ArrayList<>();
        if (!announcements.isEmpty()) {
            announcements.forEach(announcement -> {
                AnnouncementResponse announcementResponse = new AnnouncementResponse(announcement, commentMap.getOrDefault(announcement.getId(),null));
                // add to list response
                announcementResponses.add(announcementResponse);
            });
        }
        return responseUtil.successResponse(announcementResponses);
    }

    /**
     * Get Announcements Public API
     *
     * @param courseId
     * @return
     */
    @GetMapping(path = "/public")
    @Operation(summary = "Get Announcements Public")
    public ResponseEntity<RestAPIResponse> getAnnouncementsPublic(
            @RequestParam("course_id") String courseId
    ) {
        // check course id
        Course course = courseService.getById(courseId);
        Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        // get all announcements DESC by created date
        List<Announcement> announcements = announcementService.getAllByCourseIdOrderByCreatedDateDesc(courseId);
        List<String> announcementIds = announcements.stream().map(Announcement::getId).collect(Collectors.toList());
        List<Comment> commentList = commentService.getAllByAnnouncementIdIn(announcementIds);
        // map
        Map<String, List<Comment>> commentMap = commentList.stream().collect(Collectors.groupingBy(Comment::getAnnouncementId));
        // response
        List<AnnouncementResponse> announcementResponses = new ArrayList<>();
        if (!announcements.isEmpty()) {
            announcements.forEach(announcement -> {
                AnnouncementResponse announcementResponse = new AnnouncementResponse(announcement, commentMap.getOrDefault(announcement.getId(),null));
                // add to list response
                announcementResponses.add(announcementResponse);
            });
        }
        return responseUtil.successResponse(announcementResponses);
    }
}
