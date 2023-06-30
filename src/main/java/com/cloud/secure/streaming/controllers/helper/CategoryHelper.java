package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.response.CategoryResponse;
import com.cloud.secure.streaming.entities.Category;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryHelper {

    /**
     * createCategory
     *
     * @param createCategoryRequest
     * @return
     */

    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category();
        //set Id
        category.setId(UniqueID.getUUID());
        //set English name
        category.setNameEn(createCategoryRequest.getNameEn());
        //set Japanese name
        category.setNameJa(createCategoryRequest.getNameJa());
        //set Vietnamese name
        category.setNameVi(createCategoryRequest.getNameVi());
        //set created date
        category.setCreatedDate(DateUtil.convertToUTC(new Date()));
        //set parent Id
        category.setParentId(createCategoryRequest.getParentId());

        return category;
    }

    /**
     * updateCategory
     *
     * @param category
     * @param updateCategoryRequest
     * @return
     */

    public Category updateCategory(Category category, UpdateCategoryRequest updateCategoryRequest
    ) {
        //check parent_id
        if (updateCategoryRequest.getParentId() != null && !updateCategoryRequest.getParentId().trim().isEmpty() &&
                !updateCategoryRequest.getParentId().trim().equals(category.getParentId())) {
            category.setParentId(updateCategoryRequest.getParentId().trim());
        }
        //check English name
        if (updateCategoryRequest.getNameEn() != null && !updateCategoryRequest.getNameEn().trim().isEmpty() &&
                !updateCategoryRequest.getNameEn().trim().equals(category.getNameEn())) {
            category.setNameEn(updateCategoryRequest.getNameEn().trim());
        }
        //check Japanese name
        if (updateCategoryRequest.getNameJa() != null && !updateCategoryRequest.getNameJa().trim().isEmpty() &&
                !updateCategoryRequest.getNameJa().trim().equals(category.getNameJa())) {
            category.setNameJa(updateCategoryRequest.getNameJa().trim());
        }
        //check Vietnamese name
        if (updateCategoryRequest.getNameVi() != null && !updateCategoryRequest.getNameVi().trim().isEmpty() &&
                !updateCategoryRequest.getNameVi().trim().equals(category.getNameVi())) {
            category.setNameVi(updateCategoryRequest.getNameVi().trim());
        }
        return category;
    }

    public List<CategoryResponse> addToResponse(List<CategoryResponse> categoryResponses,List<Category> categories, List<Category> subCategories){
        Map<String, List<Category>> categoryMap = subCategories.stream().collect(Collectors.groupingBy(Category::getParentId));
        categories.forEach(category -> {
            List<Category> categoryList = categoryMap.getOrDefault(category.getId(), null);
            CategoryResponse categoryResponse = new CategoryResponse(category, categoryList);
            categoryResponses.add(categoryResponse);
        });
        return categoryResponses;
    }
    public void addToHierarchy(List<CategoryResponse> categoryResponses,
                               List<Category> categories,
                               List<Category> subCategories
    ) {
        categories.removeAll(subCategories);
        subCategories.forEach(category -> {
            List<Category> subCategoryList = subCategories.stream()
                    .filter(cat -> category.getId().equals(cat.getParentId())).collect(Collectors.toList());
        });
        addToResponse(categoryResponses,categories,subCategories);
    }
}
