package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.CourseCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public interface CourseCategoryRepository extends JpaRepository<CourseCategory,String> {

    List<CourseCategory> findAllByCourseCategoryId_CourseId(String courseId);

//    void deleteByCourseCategoryId_CategoryIdIn(List<String> categoryId);

}
