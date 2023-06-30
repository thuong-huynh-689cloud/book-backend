package com.cloud.secure.streaming.services;
/**
 * @author 689Cloud
 */
import com.cloud.secure.streaming.entities.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllByAnnouncementIdOrderByCreatedDateDesc(String id);

    void save(Comment comment);

    Comment getById(String id);

    List<Comment> getAllByAnnouncementIdIn(List<String> announcementIds);

    void deleteAllByAnnouncementIdIn(List<String> ids);

    List<Comment> getAllByIdIn(List<String> ids);

    void deleteAllByIdIn(List<String> ids);
}
