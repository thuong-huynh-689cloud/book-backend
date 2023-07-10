package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.ProductCategory;
import com.cloud.secure.streaming.entities.ProductCategoryId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, ProductCategoryId> {

    List<ProductCategory> findAllByProductCategoryId_ProductId(String productId);

    List<ProductCategory> findAllByProductCategoryId_ProductIdIn(List<String> productIds);
}
