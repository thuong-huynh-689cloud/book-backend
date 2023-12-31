package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.ProductCategoryHelper;
import com.cloud.secure.streaming.controllers.helper.ProductHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateProductRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateProductRequest;
import com.cloud.secure.streaming.controllers.model.response.PagingResponse;
import com.cloud.secure.streaming.controllers.model.response.ProductResponse;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.Product;
import com.cloud.secure.streaming.entities.ProductCategory;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.CategoryService;
import com.cloud.secure.streaming.services.ProductCategoryService;
import com.cloud.secure.streaming.services.ProductService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.PRODUCT_API)
public class ProductController extends AbstractBaseController {

    final ProductService productService;
    final ProductHelper productHelper;
    final CategoryService categoryService;
    final ProductCategoryService productCategoryService;
    final ProductCategoryHelper productCategoryHelper;
    final UserService userService;

    public ProductController(ProductService productService, ProductHelper productHelper, CategoryService categoryService, ProductCategoryService productCategoryService, ProductCategoryHelper productCategoryHelper, UserService userService) {
        this.productService = productService;
        this.productHelper = productHelper;
        this.categoryService = categoryService;
        this.productCategoryService = productCategoryService;
        this.productCategoryHelper = productCategoryHelper;
        this.userService = userService;
    }

    /**
     * Post Create Product API
     *
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN, UserRole.STAFF})
    @PostMapping()
    @Operation(summary = "Create Product")
    public ResponseEntity<RestAPIResponse> createProduct(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid CreateProductRequest createProductRequest
    ) {
        Product product = null;

        if (authUser.getRole().equals(UserRole.ADMIN)) {

            User user = userService.getById(createProductRequest.getUserId());
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, "user not found");

            product = productHelper.createProduct(createProductRequest, createProductRequest.getUserId());
        } else {

            // create product
            product = productHelper.createProduct(createProductRequest, authUser.getId());
        }

        List<Category> categoryList = categoryService.getAllByCategoryIdIn(createProductRequest.getCategoryIds());
        if (!categoryList.isEmpty()) {
            for (Category category : categoryList) {
                ProductCategory productCategory = productCategoryHelper.createProductCategory(product.getId(), category);
                productCategoryService.save(productCategory);
            }
        }

        productService.saveProduct(product);
        return responseUtil.successResponse(new ProductResponse(product, categoryList));
    }


    /**
     * get Product API
     *
     * @param id
     * @return
     */
    @GetMapping(path = ApiPath.ID)
    @Operation(summary = "Get Product")
    public ResponseEntity<RestAPIResponse> getProduct(
            @PathVariable(name = "id") String id
    ) {
        //enter id product show to display product
        Product product = productService.getById(id, Status.ACTIVE);
        Validator.notNull(product, RestAPIStatus.NOT_FOUND, "product not found");

        List<Category> categories = categoryService.getAllByProductId(product.getId());

        return responseUtil.successResponse(new ProductResponse(product, categories));
    }

    /**
     * put update Product API
     *
     * @param id
     * @param updateProductRequest
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN, UserRole.STAFF})
    @PutMapping(path = ApiPath.ID)
    @Operation(summary = "Update Product")
    public ResponseEntity<RestAPIResponse> updateProduct(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateProductRequest updateProductRequest
    ) {
        //id search
        Product product = productService.getById(id, Status.ACTIVE);
        Validator.notNull(product, RestAPIStatus.NOT_FOUND, "product not found");

        product = productHelper.updateProduct(product, updateProductRequest);
        // delete old product category
        List<ProductCategory> oldProductCategories = productCategoryService.getAllByProductId(product.getId());
        if (!oldProductCategories.isEmpty()) {
            // delete
            for (ProductCategory productCategory : oldProductCategories) {
                productCategoryService.delete(productCategory);
            }
        }
        // set productCategory
        List<Category> categoryList = categoryService.getAllByCategoryIdIn(updateProductRequest.getCategoryIds());
        String productId = product.getId();
        if (!categoryList.isEmpty()) {
            categoryList.forEach(category -> {
                ProductCategory productCategory = productCategoryHelper.createProductCategory(productId, category);
                productCategoryService.save(productCategory);
            });
        }
        productService.saveProduct(product);

        return responseUtil.successResponse(product);
    }

    /**
     * delete product API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserRole.ADMIN, UserRole.STAFF})
    @DeleteMapping()
    @Operation(summary = "Delete Product")
    public ResponseEntity<RestAPIResponse> deleteProduct(
            @RequestParam(name = "ids") List<String> ids
    ) {
        // check ids
        List<String> idList = ids.stream().distinct().collect(Collectors.toList());
        // get product
        List<Product> products = productService.getAllByIdIn(idList);
        if (products.size() != idList.size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, "Product not found");
        }
        // get product categories
        List<ProductCategory> productCategories = productCategoryService.getAllByProductIds(idList);
        if (!productCategories.isEmpty()) {

            productCategoryService.deleteAll(productCategories);
        }
        // delete product
        products.forEach(product -> product.setStatus(Status.INACTIVE));
        productService.saveAll(products);

        return responseUtil.successResponse("ok");
    }

    /**
     * Get Paging API
     *
     * @return
     */
    @GetMapping()
    @Operation(summary = "Get Paging Product")
    public ResponseEntity<RestAPIResponse> getProducts(

            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldProduct
                    sortFieldProduct,
            @RequestParam(value = "sort_direction", required = false, defaultValue = "ASC") SortDirection
                    sortDirection, // ASC or DESC
            @RequestParam(name = "statuses", required = false) List<Status> statuses, // ACTIVE,INACTIVE,PENDING
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_REQUEST,
                APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE, pageNumber, pageSize);

        // check statuses
        if (statuses == null || statuses.isEmpty()) {
            statuses = Arrays.asList(Status.values());
        }

        //get all product in list product
        Page<Product> productPage = productService.getProductPage(searchKey.trim(), sortFieldProduct,sortDirection,statuses,pageNumber, pageSize);

        // get category
        List<ProductResponse> productResponses = new ArrayList<>();
        if (!productPage.getContent().isEmpty()) {
            productPage.getContent().forEach(product -> {

                List<Category> categories = categoryService.getAllByProductId(product.getId());
                ProductResponse productResponse = new ProductResponse(product, categories);
                productResponses.add(productResponse);

            });
        }

        return responseUtil.successResponse(new PagingResponse(productResponses, productPage));
    }
}