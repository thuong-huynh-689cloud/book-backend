package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseCategory;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface CourseCategoryService {

    CourseCategory save(CourseCategory courseCategory);

    void delete(CourseCategory courseCategory);

    CourseCategory getById(String id);

    List<CourseCategory> getAllByCourseCategoryId_CourseId(String courseId);

//    void deleteByCourseCategoryId_CategoryIdIn(List<String> categoryId);

    void saveAll(List<CourseCategory> courseCategories);
}
