package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.entities.Course;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class CourseHelper {

    /**
     *  Create Course API
     *
     * @param userId
     * @param createCourseRequest
     * @return
     */
    public Course createCourse(String userId, CreateCourseRequest createCourseRequest) {
        Course course = new Course();
        //add id to data
        course.setId(UniqueID.getUUID());
        // add user_id to data
        course.setUserId(userId);
        // add title to data
        course.setTitle(createCourseRequest.getTitle());
        // add subtitle to data
        course.setSubtitle(createCourseRequest.getSubtitle());
        // add description to data
        course.setDescription(createCourseRequest.getDescription());
        //add status to data
        course.setStatus(AppStatus.ACTIVE);
        // add CourseStatus to data
        course.setCourseStatus(CourseStatus.DRAFT);
        // add createDate to date
        course.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return course;
    }

    /**
     * Update Course
     *
     * @param course
     * @param updateCourseRequest
     * @return
     */
    public Course updateCourse(Course course, UpdateCourseRequest updateCourseRequest) {
        // Update Title
        if (updateCourseRequest.getTitle() != null && !updateCourseRequest.getTitle().trim().isEmpty() &&
                !updateCourseRequest.getTitle().trim().equals(course.getTitle())) {
            course.setTitle(updateCourseRequest.getTitle().trim());
        }
        // Update Subtitle
        if (updateCourseRequest.getSubtitle() != null && !updateCourseRequest.getSubtitle().trim().isEmpty() &&
                !updateCourseRequest.getTitle().trim().equals(course.getSubtitle())) {
            course.setSubtitle(updateCourseRequest.getSubtitle().trim());
        }
        // Update Description
        if (updateCourseRequest.getDescription() != null && !updateCourseRequest.getDescription().trim().isEmpty() &&
                !updateCourseRequest.getDescription().trim().equals(course.getDescription())) {
            course.setDescription(updateCourseRequest.getDescription().trim());
        }
        // Update Language
        if (updateCourseRequest.getLanguage() != null &&
                !updateCourseRequest.getLanguage().equals(course.getLanguage())) {
            course.setLanguage(updateCourseRequest.getLanguage());
        }
        // Update Level
        if (updateCourseRequest.getLevel() != null &&
                !updateCourseRequest.getLanguage().equals(course.getLevel())) {
            course.setLevel(updateCourseRequest.getLevel());
        }
        // Update Image
        if (updateCourseRequest.getImage() != null && !updateCourseRequest.getImage().trim().isEmpty() &&
                !updateCourseRequest.getImage().trim().equals(course.getImage())) {
            course.setImage(updateCourseRequest.getImage().trim());
        }
        // Update Promotion Video
        if (updateCourseRequest.getPromotionVideo() != null && !updateCourseRequest.getPromotionVideo().trim().isEmpty() &&
                !updateCourseRequest.getPromotionVideo().trim().equals(course.getPromotionVideo())) {
            course.setPromotionVideo(updateCourseRequest.getPromotionVideo().trim());
        }

        return course;
    }
}
