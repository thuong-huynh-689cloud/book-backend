package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.Announcement;
import com.cloud.secure.streaming.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponse {
    private String id;
    private String content;
    private String courseId;
    private String userId;
    private Date createdDate;
    private List<Comment> comments;

    public AnnouncementResponse(Announcement announcement, List<Comment> comments){
        this.id = announcement.getId();
        this.content = announcement.getContent();
        this.courseId = announcement.getCourseId();
        this.userId = announcement.getUserId();
        this.createdDate = announcement.getCreatedDate();
        this.comments = comments;
    }
}
