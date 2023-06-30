package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseCategory;
import com.cloud.secure.streaming.repositories.CourseCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author 689Cloud
 */
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
    final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryServiceImpl(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public CourseCategory save(CourseCategory courseCategory) {
        return courseCategoryRepository.save(courseCategory);
    }

    @Override
    public void delete(CourseCategory courseCategory) {
        courseCategoryRepository.delete(courseCategory);
    }

    @Override
    public CourseCategory getById(String id) {
        return courseCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<CourseCategory> getAllByCourseCategoryId_CourseId(String courseId) {
        return courseCategoryRepository.findAllByCourseCategoryId_CourseId(courseId);
    }

//    @Override
//    public void deleteByCourseCategoryId_CategoryIdIn(List<String> categoryId) {
//        courseCategoryRepository.deleteByCourseCategoryId_CategoryIdIn(categoryId);
//    }

    @Override
    public void saveAll(List<CourseCategory> courseCategories) {
        courseCategoryRepository.saveAll(courseCategories);
    }
}

