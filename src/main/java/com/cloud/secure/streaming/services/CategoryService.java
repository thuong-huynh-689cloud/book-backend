package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldCategory;
import com.cloud.secure.streaming.entities.Category;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface CategoryService {

    Category save(Category category);

    void delete(Category category);

    Category getById(String id);

    Page<Category> getPage(String searchKey,
                           SortFieldCategory sortFieldCategory,
                           SortDirection sortDirection,
                           int pageNumber, int pageSize);

    List<Category> getAllByIdIn(List<String> ids);

    void deleteByIdIn(List<String> ids);

    List<Category>getAllByCourseId (String coursedId);

    List<Category> getAll();

    List<Category> getAllByParentId(String parentId);

    void deleteAllByParentIdIn(List<String> ids);
}