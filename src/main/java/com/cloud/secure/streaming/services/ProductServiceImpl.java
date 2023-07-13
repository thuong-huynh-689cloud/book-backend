package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldProduct;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Product;
import com.cloud.secure.streaming.repositories.ProductRepository;
import com.cloud.secure.streaming.repositories.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {
    final ProductRepository productRepository;
    final ProductSpecification productSpecification;

    public ProductServiceImpl(ProductRepository productRepository, ProductSpecification productSpecification) {
        this.productRepository = productRepository;
        this.productSpecification = productSpecification;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getById(String id, Status status) {
        return productRepository.findByIdAndStatus(id, status);
    }

    @Override
    public List<Product> getAllByIdIn(List<String> ids) {
        return productRepository.findAllByIdInAndStatus(ids, Status.ACTIVE);
    }

    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }


    @Override
    public Page<Product> getProductPage(String searchKey, SortFieldProduct sortFieldProduct, SortDirection sortDirection ,List<Status> statuses,int pageNumber,int pageSize) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldProduct.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return productRepository.getByNameContaining("%" + searchKey + "%", statuses, pageable);

    }


}
