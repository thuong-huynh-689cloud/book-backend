package com.cloud.secure.streaming.controllers;


import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.helper.CategoryHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.CATEGORY_API)
public class CategoryControllers extends AbstractBaseController {
    final CategoryService categoryService;
    final CategoryHelper categoryHelper;

    public CategoryControllers(CategoryService categoryService, CategoryHelper categoryHelper) {
        this.categoryService = categoryService;
        this.categoryHelper = categoryHelper;
    }

    /**
     * @param createCategoryRequest
     * @return
     */

    @PostMapping()
    @Operation(summary = "Create Category")
    public ResponseEntity<RestAPIResponse> createCategory(
            @RequestBody CreateCategoryRequest createCategoryRequest,
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {

        Category category = categoryService.getByNameAndStatus(createCategoryRequest.getName(), Status.ACTIVE);
        //1
        Validator.mustNull(category, RestAPIStatus.EXISTED, "Category name already existed.");
        //2
//        if(category != null){
//            throw  new ApplicationException(RestAPIStatus.EXISTED, "Category name already existed.");
//        }

        // create category
        category = categoryHelper.createCategory(createCategoryRequest, authUser);
        categoryService.saveCategory(category);
        return responseUtil.successResponse(category);
    }

    /**
     * Get category API
     *
     * @param
     * @return
     */
    @GetMapping(path = ApiPath.ID)
    @Operation(summary = "Get Category")
    public ResponseEntity<RestAPIResponse> getCategory(
            @PathVariable(name = "id") String id
    ) {
        // id search
        Category category = categoryService.getById(id);
        Validator.notNull(category, RestAPIStatus.NOT_FOUND, "Category not found");

        return responseUtil.successResponse(category);
    }

    /**
     * Get All Product API
     *
     * @return
     */
    @GetMapping(path = ApiPath.ALL)
    @Operation(summary = "Get All Category")
    public ResponseEntity<RestAPIResponse> getAllCategory() {
        return responseUtil.successResponse(categoryService.getAllByCategory());
    }

    /**
     * Get Paging API
     *
     * @param
     * @return
     */
    @GetMapping()
    @Operation(summary = "Get Paging Category")
    public ResponseEntity<RestAPIResponse> getCategories(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "is_asc", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(name = "sort_field", required = false, defaultValue = "false") String sortField,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        //get all category in list category
        Page<Category> categoryPage = categoryService.getByNameContaining(searchKey.trim(), isAsc,
                sortField, pageNumber, pageSize);

        return responseUtil.successResponse(new PagingResponse(categoryPage));
    }

    /**
     * Update Category API
     *
     * @param
     * @param
     * @return
     */
    @PutMapping(path = ApiPath.ID)
    @Operation(summary = "Update Category")
    public ResponseEntity<RestAPIResponse> updateCategory(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) {
        //id search
        Category category = categoryService.getById(id);
        Validator.notNull(category, RestAPIStatus.NOT_FOUND, "Category not found");
        // update category info
        category = categoryHelper.updateCategory(category, updateCategoryRequest);
        categoryService.saveCategory(category);

        return responseUtil.successResponse(category);
    }

    /**
     * Delete Categories
     *
     * @param ids
     * @return
     */
    @DeleteMapping()
    @Operation(summary = "Delete Category")
    public ResponseEntity<RestAPIResponse> deleteCategories(
            @RequestParam(name = "ids") List<String> ids
    ) {
        // check Ids
        List<String> idsCategoryDistinct = ids.stream().distinct().collect(Collectors.toList());
        // validate duplicate
        if (ids.size() != idsCategoryDistinct.size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, "deleted");
        }
        List<Category> categoryList = categoryService.getAllByCategoryIdIn(ids);
        if (categoryList.size() != ids.size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, "category not found, or deleted");
        }
        categoryList.forEach(category -> category.setStatus(Status.IN_ACTIVE));
        categoryService.saveAll(categoryList);

        return responseUtil.successResponse("Delete successful!");
    }
}
