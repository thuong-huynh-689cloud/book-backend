package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldCategory;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.CategoryHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCategoryRequest;
import com.cloud.secure.streaming.controllers.model.response.CategoryResponse;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiPath.CATEGORY_API)
@Slf4j
public class CategoryController extends AbstractBaseController {

    final CategoryService categoryService;
    final CategoryHelper categoryHelper;

    public CategoryController(CategoryService categoryService, CategoryHelper categoryHelper) {
        this.categoryService = categoryService;
        this.categoryHelper = categoryHelper;
    }

    /**
     * Create Category API
     *
     * @param createCategoryRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PostMapping()
    @Operation(summary = "Create Category")
    public ResponseEntity<RestAPIResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        if (createCategoryRequest.getParentId() != null && !createCategoryRequest.getParentId().isEmpty()) {
            //validate parent id
            Category categoryId = categoryService.getById(createCategoryRequest.getParentId());
            Validator.notNull(categoryId, RestAPIStatus.NOT_FOUND, APIStatusMessage.CATEGORY_NOT_FOUND);
        }
        // create Category
        Category category = categoryHelper.createCategory(createCategoryRequest);
        categoryService.save(category);
        return responseUtil.successResponse(category);
    }

    /**
     * Get Category API
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Category")
    public ResponseEntity<RestAPIResponse> getCategory(
            @PathVariable(name = "id") String id
    ) {
        //get category
        Category category = categoryService.getById(id);
        Validator.notNull(category, RestAPIStatus.NOT_FOUND, APIStatusMessage.CATEGORY_NOT_FOUND);
        //get sub categories by parent id
        List<Category> categories = categoryService.getAllByParentId(id);

        return responseUtil.successResponse(new CategoryResponse(category, categories));
    }

    /**
     * Get categories API
     *
     * @param searchKey
     * @param sortFieldCategory
     * @param sortDirection
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping
    @Operation(summary = "get paging category")
    public ResponseEntity<RestAPIResponse> getPagingCategory(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldCategory sortFieldCategory,
            @RequestParam(name = "sort_direction", required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE,
                pageNumber, pageSize);
        //search, sort
        Page<Category> categoryPage = categoryService.getPage(searchKey, sortFieldCategory,
                sortDirection, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(categoryPage));
    }

    /**
     * Update Category API
     *
     * @param id
     * @param updateCategoryRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{id}")
    @Operation(summary = "update category")
    public ResponseEntity<RestAPIResponse> updateCategory(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) {
        //get category
        Category category = categoryService.getById(id);
        //Validate category
        Validator.notNull(category, RestAPIStatus.NOT_FOUND, APIStatusMessage.CATEGORY_NOT_FOUND);
        // update category info
        category = categoryHelper.updateCategory(category, updateCategoryRequest);
        categoryService.save(category);

        return responseUtil.successResponse(category);
    }

    /**
     * Delete Category API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @DeleteMapping
    @Operation(summary = "delete category")
    public ResponseEntity<RestAPIResponse> deleteCategory(
            @RequestParam(name = "ids") List<String> ids
    ) {
        //check category
        List<Category> categories = categoryService.getAllByIdIn(ids);
        //delete category
        categoryService.deleteByIdIn(ids);
        //delete sub categories
        categoryService.deleteAllByParentIdIn(ids);
        return responseUtil.successResponse("OK");
    }

    /**
     * Get List Categories
     *
     * @return
     */
    @GetMapping("/all")
    @Operation(summary = "Get List Categories")
    public ResponseEntity<RestAPIResponse> getAllCategories(

    ) {
        // get all categories
        List<Category> categories = categoryService.getAll();

        return responseUtil.successResponse(categories);
    }
}
