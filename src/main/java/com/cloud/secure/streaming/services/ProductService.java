package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Product;
import org.springframework.data.domain.Page;


import java.util.List;


public interface ProductService {

    Product saveProduct(Product product);

    Product getById(String id, Status status);

    List<Product> getAllByIdIn(List<String> ids);

    void saveAll(List<Product> products);

//    Page<Product> getByNameContaining(String nam, boolean isAsc, String field, int pageNumber, int pageSize);


    Page<Product> getProductPage(String searchKey, boolean isAsc, String sortField, List<String>categoryIds, Double fromPrice,
                                 Double toPrice, int page, int size);




}
