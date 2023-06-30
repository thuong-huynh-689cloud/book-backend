package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.CourseCategoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, CourseCategoryId> {

    @Query("select c from Category c where c.nameEn like :searchKey or c.nameJa like :searchKey" +
            " or c.nameVi like :searchKey")
    Page<Category> getPageCategory(@Param("searchKey") String searchKey,
                                                 Pageable pageable);

    List<Category> findAllByIdIn(List<String> ids);

    void deleteAllByIdIn(List<String> ids);

    @Query("select c from Category c, Course p, CourseCategory pc where c.id = pc.courseCategoryId.categoryId and p.id = pc.courseCategoryId.courseId and p.id = :courseId")
    List<Category> getAllByCourseId(@Param("courseId") String courseId);

    List<Category> findAllByParentId(String parentId);

    void deleteAllByParentIdIn(List<String> ids);

    Category findById(String id);
}
