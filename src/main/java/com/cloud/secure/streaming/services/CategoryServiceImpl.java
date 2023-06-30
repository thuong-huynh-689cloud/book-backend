package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldCategory;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

;

/**
 * @author 689Cloud
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public Category getById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Page<Category> getPage(String searchKey,
                                  SortFieldCategory sortFieldCategory,
                                  SortDirection sortDirection,
                                  int pageNumber, int pageSize
    ) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldCategory.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return categoryRepository.getPageCategory("%" + searchKey + "%", pageable);
    }

    @Override
    public List<Category> getAllByIdIn(List<String> ids) {
        return categoryRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteByIdIn(List<String> ids) {
        categoryRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<Category> getAllByCourseId(String coursedId) {
        return categoryRepository.getAllByCourseId(coursedId);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllByParentId(String parentId) {
        return categoryRepository.findAllByParentId(parentId);
    }

    @Override
    public void deleteAllByParentIdIn(List<String> ids) {
        categoryRepository.deleteAllByParentIdIn(ids);
    }
}
