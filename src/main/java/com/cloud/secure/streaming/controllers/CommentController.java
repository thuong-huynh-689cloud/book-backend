package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.CommentHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCommentRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCommentRequest;
import com.cloud.secure.streaming.entities.Announcement;
import com.cloud.secure.streaming.entities.Comment;
import com.cloud.secure.streaming.services.AnnouncementService;
import com.cloud.secure.streaming.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.COMMENT_API)
@Slf4j
public class CommentController extends AbstractBaseController {

    final CommentService commentService;
    final CommentHelper commentHelper;
    final AnnouncementService announcementService;

    public CommentController(CommentService commentService,
                             CommentHelper commentHelper,
                             AnnouncementService announcementService
    ) {
        this.commentService = commentService;
        this.commentHelper = commentHelper;
        this.announcementService = announcementService;
    }

    /**
     * Create Comment API
     *
     * @param authUser
     * @param createCommentRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @PostMapping
    @Operation(summary = "Create Comment")
    public ResponseEntity<RestAPIResponse> createComment(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid CreateCommentRequest createCommentRequest
    ) {
        // check announcement id
        Announcement announcement = announcementService.getById(createCommentRequest.getAnnouncementId());
        Validator.notNull(announcement, RestAPIStatus.NOT_FOUND, APIStatusMessage.ANNOUNCEMENT_NOT_FOUND);
        // create comment
        Comment comment = commentHelper.createComment(authUser.getId(), createCommentRequest);
        // save
        commentService.save(comment);
        return responseUtil.successResponse(comment);
    }

    /**
     * Delete Comment API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @DeleteMapping
    @Operation(summary = "Delete Comment")
    public ResponseEntity<RestAPIResponse> deleteComment(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        // get comment
        List<Comment> comments = commentService.getAllByIdIn(ids);
        // user only delete for themselves
        if (!comments.isEmpty()) {
            if (authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER)) {
                List<Comment> commentListWithWrongUserId = comments.stream()
                        .filter(comment -> !comment.getUserId().equals(authUser.getId()))
                        .collect(Collectors.toList());
                if (!commentListWithWrongUserId.isEmpty()) {
                    throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
                }
            }
        }
        // delete
        commentService.deleteAllByIdIn(ids);
        return responseUtil.successResponse("OK");
    }

    /**
     * Update Comment API
     *
     * @param authUser
     * @param id
     * @param updateCommentRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Comment")
    public ResponseEntity<RestAPIResponse> updateComment(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id, // comment id
            @RequestBody @Valid UpdateCommentRequest updateCommentRequest
    ) {
        // get comment
        Comment comment = commentService.getById(id);
        Validator.notNull(comment, RestAPIStatus.NOT_FOUND, APIStatusMessage.COMMENT_NOT_FOUND);
        // user only update for themselves
        if ((authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER))
                && !authUser.getId().equals(comment.getUserId())) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
        }
        // update
        comment = commentHelper.updateComment(comment, updateCommentRequest);
        // save
        commentService.save(comment);
        return responseUtil.successResponse(comment);
    }

    /**
     * Get Comment API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Comment")
    public ResponseEntity<RestAPIResponse> getComment(
            @PathVariable(name = "id") String id
    ) {
        // get comment
        Comment comment = commentService.getById(id);
        Validator.notNull(comment, RestAPIStatus.NOT_FOUND,APIStatusMessage.COMMENT_NOT_FOUND);

        return responseUtil.successResponse(comment);
    }

    /**
     * Get List Comments API
     *
     * @param announcementId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping()
    @Operation(summary = "Get Comments")
    public ResponseEntity<RestAPIResponse> getComments(
            @RequestParam(name = "announcement_id") String announcementId
    ) {
        // check announcement id
        Announcement announcement = announcementService.getById(announcementId);
        Validator.notNull(announcement, RestAPIStatus.NOT_FOUND, APIStatusMessage.ANNOUNCEMENT_NOT_FOUND);
        // get all comment DESC by announcement id
        List<Comment> comments = commentService.getAllByAnnouncementIdOrderByCreatedDateDesc(announcementId);

        return responseUtil.successResponse(comments);
    }

    /**
     * Get List Comments Public API
     *
     * @param announcementId
     * @return
     */
    @GetMapping(path = "/public")
    @Operation(summary = "Get Comments")
    public ResponseEntity<RestAPIResponse> getCommentsPublic(
            @RequestParam(name = "announcement_id") String announcementId
    ) {
        // check announcement id
        Announcement announcement = announcementService.getById(announcementId);
        Validator.notNull(announcement, RestAPIStatus.NOT_FOUND, APIStatusMessage.ANNOUNCEMENT_NOT_FOUND);
        // get all comment DESC by announcement id
        List<Comment> comments = commentService.getAllByAnnouncementIdOrderByCreatedDateDesc(announcementId);

        return responseUtil.successResponse(comments);
    }
}
