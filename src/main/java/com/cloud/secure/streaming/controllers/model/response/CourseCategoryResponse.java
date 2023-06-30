package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryResponse {
    private String courseId;
    private String categoryId;


    public CourseCategoryResponse(CourseCategory courseCategory, Category category) {
        this.courseId = courseCategory.getCourseCategoryId().getCourseId();
        this.courseId = courseCategory.getCourseCategoryId().getCategoryId();
    }
}
