package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.controllers.helper.ProductImagesHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.PRODUCT_IMAGES_API)
public class ProductImagesController  extends AbstractBaseController{
  final ProductImagesService productImagesService;
  final ProductImagesHelper productImagesHelper;

    public ProductImagesController(ProductImagesService productImagesService, ProductImagesHelper productImagesRepository) {
        this.productImagesService = productImagesService;
        this.productImagesHelper = productImagesRepository;
    }

    @PostMapping
    public ResponseEntity<RestAPIResponse> login(
    ) {
        return responseUtil.successResponse("");
    }

}

