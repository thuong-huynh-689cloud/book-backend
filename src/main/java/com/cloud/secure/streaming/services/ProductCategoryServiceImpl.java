package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.ProductCategory;
import com.cloud.secure.streaming.entities.ProductCategoryId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory getById(ProductCategoryId productCategoryId) {
        return productCategoryRepository.findById(productCategoryId).orElse(null);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        productCategoryRepository.delete(productCategory);
    }

    @Override
    public void deleteAll(List<ProductCategory> productCategory) {
        productCategoryRepository.deleteAll(productCategory);
    }

    @Override
    public List<ProductCategory> getAllByProductId(String productId) {
        return productCategoryRepository.findAllByProductCategoryId_ProductId(productId);
    }

    @Override
    public List<ProductCategory> getAllByProductIds(List<String> productId) {
        return productCategoryRepository.findAllByProductCategoryId_ProductIdIn(productId);
    }
}
