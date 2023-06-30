package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.CourseAdminFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CourseAdminFeedbackRepository extends JpaRepository<CourseAdminFeedback,String> {

    @Modifying
    @Query("DELETE from CourseAdminFeedback c where c.courseId = :courseId")
    void deleteCourseAdminFeedbackById(@Param("courseId") String courseId);
}
