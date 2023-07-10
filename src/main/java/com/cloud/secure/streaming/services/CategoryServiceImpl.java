package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(String id) {
        return categoryRepository.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public List<Category> getAllByCategoryIdIn(List<String> categoryIds) {
        return categoryRepository.findAllByIdInAndStatus(categoryIds, Status.ACTIVE);
    }

    @Override
    public void saveAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    @Override
    public Page<Category> getByNameContaining(String name, boolean isAsc, String field, int pageNumber, int pageSize) {
        String properties = "";
        switch (field) {
            case "name":
                properties = "name";
                break;
            case "status":
                properties = "status";
                break;
            default:
                properties = "createdDate";
                break;
        }

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, properties);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return categoryRepository.getByNameContaining(Status.ACTIVE, "%" + name + "%", pageable);
    }

    @Override
    public List<Category> getAllByCategory() {
        return categoryRepository.findByStatus(Status.ACTIVE);
    }

    @Override
    public List<Category> getAllByProductId(String productId) {
        return categoryRepository.getAllByProductId(productId);
    }

    @Override
    public Category getByNameAndStatus(String name, Status status) {
        return categoryRepository.findByNameAndStatus(name,status);
    }

}
