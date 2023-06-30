package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface LectureRepository extends JpaRepository<Lecture, String> {

    List<Lecture> findAllBySectionIdOrderByCreatedDateDesc(String sectionId);

    List<Lecture> findAllByIdIn(List<String> ids);
    void deleteAllByIdIn(List<String> ids);

    void deleteAllBySectionIdIn(List<String> ids);

    List<Lecture> findAllBySectionIdIn(List<String> sectionIds);

    @Query("select l from Course c, Section s, Lecture l where c.id = s.courseId and s.id = l.sectionId and l.id = :lectureId and (:userId is null or c.userId = :userId)")
    Lecture findByLectureIdAndUserId(@Param("lectureId") String lectureId, @Param("userId") String userId);

    @Query("select l from Lecture l, Section s where l.sectionId = s.id and s.courseId = :courseId")
    List<Lecture> findAllByCourseId(@Param("courseId") String courseId);

    List<Lecture> findAllBySectionId(String sectionId);
}
