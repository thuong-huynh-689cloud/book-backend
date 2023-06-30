package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.controllers.model.response.CourseResponse;
import com.cloud.secure.streaming.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, String>, JpaSpecificationExecutor<Course> {

    Course findByIdAndStatus(String id, AppStatus appStatus);

    @Modifying
    @Query("UPDATE Course c set c.status = :status where c.id in :ids")
    void updateByIdInToInactive(@Param("status") AppStatus status,
                                @Param("ids") List<String> ids);

    @Modifying
    @Query("UPDATE Course c set c.status = :status where c.userId in :userId")
    void updateByUserIdInToInactive(@Param("status") AppStatus status,
                                @Param("userId") List<String> userId);

    @Modifying
    @Query("UPDATE Course c set c.status = :status where c.id in :ids and c.userId = :userId")
    void updateByIdInToInactiveAndUserId(@Param("status") AppStatus status,
                                         @Param("ids") List<String> ids,
                                         @Param("userId") String userId);

    List<Course> findAllByIdIn(List<String> ids);

    @Query("select c from Course c, Section s, Lecture l, Resource r  " +
            "where c.id = s.courseId and s.id = l.sectionId and l.id = r.lectureId and r.id = :resourceId")
    List<Course> getAllByResourceId(@Param("resourceId") String resourceId);

    @Query("select c from Course c, Section s, Lecture l where c.id = s.courseId and s.id = l.sectionId and l.id = :lectureId")
    Course findByLectureId(@Param("lectureId") String lectureId);

    @Query("select c from Course c, Section s, Lecture l, Resource r  " +
            "where c.id = s.courseId and s.id = l.sectionId and l.id = r.lectureId and r.id = :resourceId")
    Course findByResourceId(@Param("resourceId") String resourceId);

    @Query("select c from Course c, Section s where c.id = s.courseId and s.id = :sectionId")
    Course findCourseBySectionId(@Param("sectionId") String sectionId);

    @Query("select c from Course c, Section s, Lecture l " +
            "where c.id = s.courseId and s.id = l.sectionId and l.id in :ids")
    List<Course> findCourseByLectureIdIn(@Param("ids") List<String> ids);

    List<Course> findAllByStatusOrderByCreatedDateDesc(AppStatus appStatus);

    List<Course> findAllByIdInAndCourseStatus(List<String> ids, CourseStatus status);

    List<Course> findAllByUserId(String userId);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.CourseResponse(c, u) from Course c ,User u" +
            " where c.userId =u.id and  (:userId is null or u.id = :userId) and (c.courseStatus in :courseStatus) and c.status =:status" +
            " and (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<CourseResponse> findCoursesPage(@Param("userId") String userId,
                                         @Param("searchKey") String searchKey,
                                         @Param("courseStatus") List<CourseStatus> courseStatus,
                                         @Param("status") AppStatus status,
                                         Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.CourseResponse(c, u) from Course c ,User u" +
            " where c.userId =u.id and (:courseStatusSize = 0 or c.courseStatus in :courseStatus) and c.status =:status" +
            " and (:courseLevelsSize = 0 or c.level in :courseLevels) and (:languagesSize = 0 or c.language in :languages) and (:coursePricesSize = 0 or c.coursePrice in :coursePrices) " +
            "and (:fromPrice is null or c.point >= :fromPrice) and (:toPrice is null or c.point <= :toPrice) and (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<CourseResponse> findCoursesPagePublic(@Param("searchKey") String searchKey,
                                               @Param("courseStatusSize") int courseStatusSize,
                                               @Param("courseStatus") List<CourseStatus> courseStatus,
                                               @Param("courseLevelsSize") int courseLevelsSize,
                                               @Param("courseLevels") List<CourseLevel> courseLevels,
                                               @Param("languagesSize") int languagesSize,
                                               @Param("languages") List<Language> languages,
                                               @Param("coursePricesSize") int coursePricesSize,
                                               @Param("coursePrices") List<CoursePrice> coursePrices,
                                               @Param("fromPrice") Double fromPrice,
                                               @Param("toPrice") Double toPrice,
                                               @Param("status") AppStatus status,
                                               Pageable pageable);

    @Query(value = "select count (od.courseId) FROM Course c ,OrderDetail od where c.id = od.courseId" +
            " and (c.id = :courseId) and  c.status =:status  group by od.courseId")
    Long numberOfUser(@Param("courseId") String courseId,
                      @Param("status") AppStatus status);

    @Query("select c from Course  c , OrderDetail od ,Orders o where c.id = od.courseId and od.orderId = o.id and od.courseId = :courseId " +
            "and o.userId = :userId")
    Course getCourseByUserId(@Param("courseId") String courseId,
                             @Param("userId") String userId);
}

