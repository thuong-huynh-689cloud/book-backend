package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.controllers.helper.ProductCategoryHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.PRODUCT_CATEGORY_API)
public class ProductCategoryController  extends AbstractBaseController {

    final ProductCategoryService productCategoryService;
    final ProductCategoryHelper productCategoryHelper;

    public ProductCategoryController(ProductCategoryService productCategoryService, ProductCategoryHelper productCategoryRepository) {
        this.productCategoryService = productCategoryService;
        this.productCategoryHelper = productCategoryRepository;
    }


    @PostMapping
    public ResponseEntity<RestAPIResponse> login(
    ) {
        return responseUtil.successResponse("");
    }
}
