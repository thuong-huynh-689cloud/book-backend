package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldProduct;
import com.cloud.secure.streaming.common.enums.SortFieldUser;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Product;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ProductService {

    Product saveProduct(Product product);

    Product getById(String id, Status status);

    List<Product> getAllByIdIn(List<String> ids);

    void saveAll(List<Product> products);

    Page<Product> getProductPage(String searchKey, SortFieldProduct sortFieldProduct, SortDirection sortDirection,List<Status> statuses,int pageNumber,int pageSize);

}
