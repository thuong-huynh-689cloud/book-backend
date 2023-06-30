package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.CourseCategory;
import com.cloud.secure.streaming.entities.CourseCategoryId;
import org.springframework.stereotype.Component;

/**
 * @author 689Cloud
 */
@Component
public class CourseCategoryHelper {

    /**
     *  Create CourseCategory
     *
     * @param courseId
     * @param category
     * @return
     */
    public CourseCategory createCourseCategory(String courseId, Category category) {
        CourseCategory courseCategory = new CourseCategory();
        CourseCategoryId courseCategoryId = new CourseCategoryId();
        courseCategoryId.setCourseId(courseId);
        courseCategoryId.setCategoryId(category.getId());
        courseCategory.setCourseCategoryId(courseCategoryId);
        return courseCategory;
    }
}
