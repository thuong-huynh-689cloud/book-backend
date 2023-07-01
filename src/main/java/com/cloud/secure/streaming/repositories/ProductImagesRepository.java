package com.cloud.secure.ecommerce.repositories;

import com.cloud.secure.ecommerce.entities.ProductImages;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProductImagesRepository  extends PagingAndSortingRepository<ProductImages, String> {
}
