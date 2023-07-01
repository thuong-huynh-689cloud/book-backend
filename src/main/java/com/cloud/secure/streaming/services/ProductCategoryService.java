package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.ProductCategory;
import com.cloud.secure.streaming.entities.ProductCategoryId;

import java.util.List;

public interface ProductCategoryService {

    ProductCategory save(ProductCategory productCategory);

    ProductCategory getById(ProductCategoryId productCategoryId);

    void delete(ProductCategory productCategory);

    void deleteAll(List<ProductCategory> productCategory);

    List<ProductCategory> getAllByProductId(String productId);

    List<ProductCategory> getAllByProductIds(List<String> productId);



}
