package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    Category saveCategory (Category category);

    Category getById (String id);

    List<Category> getAllByCategoryIdIn(List<String> categoryIds);

    void saveAll(List<Category> categories);

    Page<Category> getByNameContaining(String name, boolean isAsc, String field, int pageNumber, int pageSize);

    List<Category> getAllByCategory();

    List<Category> getAllByProductId(String productId);

    Category getByNameAndStatus(String name , Status status);

}
