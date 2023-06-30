package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {

    @Query("select a from Announcement a where a.courseId = :courseId and a.content like :searchKey")
    Page<Announcement> getPageAnnouncement(@Param("courseId") String courseId,
                                           @Param("searchKey") String searchKey,
                                           Pageable pageable);

    List<Announcement> findAllByIdIn(List<String> ids);

    void deleteAllByIdIn(List<String> ids);

    @Query("select a from Announcement a where a.courseId = :courseId order by a.createdDate asc ")
    List<Announcement> findAllByCourseIdOrderByCreatedDateDesc(@Param("courseId") String courseId);
}
