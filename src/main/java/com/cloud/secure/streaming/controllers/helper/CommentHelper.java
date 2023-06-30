package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateCommentRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCommentRequest;
import com.cloud.secure.streaming.entities.Comment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CommentHelper {

    public Comment createComment(String id, CreateCommentRequest createCommentRequest) {
        Comment comment = new Comment(
                UniqueID.getUUID(), // set id
                createCommentRequest.getContent(), // set content
                createCommentRequest.getAnnouncementId(),  // set announcement id
                id // set user id
        );
        // set created date
        comment.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return comment;
    }

    public Comment updateComment(Comment comment, UpdateCommentRequest updateCommentRequest) {
        // check content
        if (updateCommentRequest.getContent() != null && !updateCommentRequest.getContent().trim().isEmpty()
                && !comment.getContent().equals(updateCommentRequest.getContent().trim())) {
            comment.setContent(updateCommentRequest.getContent().trim());
        }
        return comment;
    }
}
