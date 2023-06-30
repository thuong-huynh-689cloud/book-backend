package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("select c from Comment c where c.announcementId = :id order by c.createdDate asc ")
    List<Comment> findAllByAnnouncementIdOrderByCreatedDateDesc(String id);

    List<Comment> findAllByAnnouncementIdIn(List<String> announcementIds);

    void deleteAllByAnnouncementIdIn(List<String> ids);

    List<Comment> findAllByIdIn(List<String> ids);

    void deleteAllByIdIn(List<String> ids);
}
