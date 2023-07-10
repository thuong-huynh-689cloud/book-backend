package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.model.request.CreateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCategoryRequest;
import com.cloud.secure.streaming.entities.Category;
import org.springframework.stereotype.Component;


@Component
public class CategoryHelper {

    public Category createCategory(CreateCategoryRequest createCategoryRequest, AuthUser authUser) {
        Category category = new Category();
        //add id to data
        category.setId(UniqueID.getUUID());
        //add set userId to data
        category.setUserId(authUser.getId());
        //add name to data
        category.setName(createCategoryRequest.getName());
        //add Description to data
        category.setDescription(createCategoryRequest.getDescription());
        //add status to data
        category.setStatus(Status.ACTIVE);
        return category;
    }


    public Category updateCategory(Category category, UpdateCategoryRequest updateCategoryRequest
    ) {
        if (updateCategoryRequest.getName() != null && !updateCategoryRequest.getName().trim().isEmpty() &&
        !updateCategoryRequest.getName().trim().equals(category.getName())) {
            category.setName(updateCategoryRequest.getName().trim());
        }
        if (updateCategoryRequest.getDescription() != null && !updateCategoryRequest.getDescription().trim().isEmpty()) {
            category.setDescription(updateCategoryRequest.getDescription().trim());
        }

        return category;
    }


}
