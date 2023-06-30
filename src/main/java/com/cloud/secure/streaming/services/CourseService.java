package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.controllers.model.response.CourseResponse;
import com.cloud.secure.streaming.entities.Course;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface CourseService {

    Course save(Course course);

    void delete(Course course);

    Course getById(String id);

    Course getByIdAndStatus(String id, AppStatus appStatus);

    void updateByIdInToInactive(List<String> ids);

    void updateByUserIdInToInactive(List<String> userId);

    void updateByIdInToInactiveAndUserId(List<String> ids, String userId);

    List<Course> getAllByIdIn(List<String> ids);

    List<Course> getAllByStatusOrderByCreatedDateDesc();

    List<Course> getAllByResourceId(String resourceId);

    Course getByLectureId(String lectureId);

    Course getCourseByResourceId(String id);

    Course getByCourseSectionId(String sectionId);

    List<Course> getAllByLectureIdIn(List<String> ids);

    List<Course> getAllByIdIn(List<String> ids, CourseStatus status);

    List<Course> getAllByUserId(String userId);

    Page<CourseResponse> getCoursesPage(String userId, String searchKey, SortFieldCourse sortFieldCourse, SortDirection sortDirection,
                                        List<CourseStatus> statuses, int pageNumber, int pageSize);


    Page<CourseResponse> getCoursesPagePublic(String searchKey, SortFieldCourse sortFieldCourse, SortDirection sortDirection,
                                              List<CourseStatus> statuses, List<CourseLevel> courseLevels,
                                              List<Language> languages, List<CoursePrice> coursePrices, Double fromPrice, Double toPrice, int pageNumber, int pageSize);

    Long getNumberOfUser(String courseId);

    Course getCourseByUserId(String courseId,String userId);
}