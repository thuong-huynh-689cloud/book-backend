package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.controllers.model.response.CourseResponse;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.repositories.CourseRepository;
import com.cloud.secure.streaming.repositories.specification.CourseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author 689Cloud
 */
@Service
public class CourseServiceImpl implements CourseService {
    final CourseRepository courseRepository;
    final CourseSpecification courseSpecification;

    public CourseServiceImpl(CourseRepository courseRepository, CourseSpecification courseSpecification) {
        this.courseRepository = courseRepository;
        this.courseSpecification = courseSpecification;
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void delete(Course course) {
        courseRepository.delete(course);
    }

    @Override
    public Course getById(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public Course getByIdAndStatus(String id, AppStatus appStatus) {
        return courseRepository.findByIdAndStatus(id, appStatus);
    }


    @Override
    public void updateByIdInToInactive(List<String> ids) {
        courseRepository.updateByIdInToInactive(AppStatus.INACTIVE, ids);
    }

    @Override
    public void updateByUserIdInToInactive(List<String> userId) {
        courseRepository.updateByUserIdInToInactive(AppStatus.INACTIVE,userId);
    }

    @Override
    public void updateByIdInToInactiveAndUserId(List<String> ids, String userId) {
        courseRepository.updateByIdInToInactiveAndUserId(AppStatus.INACTIVE, ids, userId);
    }

    @Override
    public List<Course> getAllByIdIn(List<String> ids) {
        return courseRepository.findAllByIdIn(ids);
    }

    @Override
    public List<Course> getAllByStatusOrderByCreatedDateDesc() {
        return courseRepository.findAllByStatusOrderByCreatedDateDesc(AppStatus.ACTIVE);
    }

    @Override
    public List<Course> getAllByResourceId(String resourceId) {
        return courseRepository.getAllByResourceId(resourceId);
    }


    @Override
    public Course getByLectureId(String lectureId) {
        return courseRepository.findByLectureId(lectureId);
    }

    @Override
    public Course getCourseByResourceId(String id) {
        return courseRepository.findByResourceId(id);
    }

    @Override
    public Course getByCourseSectionId(String sectionId) {
        return courseRepository.findCourseBySectionId(sectionId);
    }

    @Override
    public List<Course> getAllByLectureIdIn(List<String> ids) {
        return courseRepository.findCourseByLectureIdIn(ids);
    }

    @Override
    public List<Course> getAllByIdIn(List<String> ids, CourseStatus status) {
        return courseRepository.findAllByIdInAndCourseStatus(ids, status);
    }


    @Override
    public List<Course> getAllByUserId(String userId) {
        return courseRepository.findAllByUserId(userId);
    }


    @Override
    public Page<CourseResponse> getCoursesPage(String userId, String searchKey, SortFieldCourse sortFieldCourse, SortDirection sortDirection,
                                               List<CourseStatus> statuses, int pageNumber, int pageSize) {

        Sort.Direction direction = (sortDirection.equals(SortDirection.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortFieldCourse.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return courseRepository.findCoursesPage(userId, "%" + searchKey + "%", statuses, AppStatus.ACTIVE, pageable);
    }

    @Override
    public Page<CourseResponse> getCoursesPagePublic(String searchKey, SortFieldCourse sortFieldCourse, SortDirection sortDirection,
                                                     List<CourseStatus> statuses, List<CourseLevel> courseLevels,
                                                     List<Language> languages, List<CoursePrice> coursePrices,
                                                     Double fromPrice, Double toPrice, int pageNumber, int pageSize) {

        Sort.Direction direction = (sortDirection.equals(SortDirection.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortFieldCourse.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return courseRepository.findCoursesPagePublic("%" + searchKey + "%",statuses.size(), statuses, courseLevels.size(),courseLevels,languages.size(), languages,coursePrices.size(), coursePrices, fromPrice, toPrice, AppStatus.ACTIVE, pageable);
    }

    @Override
    public Long getNumberOfUser(String courseId) {
        return courseRepository.numberOfUser(courseId, AppStatus.ACTIVE);
    }

    @Override
    public Course getCourseByUserId(String courseId, String userId) {
        return courseRepository.getCourseByUserId(courseId, userId);
    }
}

