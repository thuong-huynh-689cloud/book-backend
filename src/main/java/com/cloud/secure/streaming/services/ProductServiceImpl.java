package com.cloud.secure.streaming.services;

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
    public Page<Product> getByNameContaining(String name, boolean isAsc, String field, int pageNumber, int pageSize) {
        String properties = "";
        switch (field) {
            case "name":
                properties = "name";
                break;
            case "status":
                properties = "status";
                break;
            case "sku":
                properties = "sku";
                break;
            default:
                properties = "createdDate";
                break;
        }
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, properties);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);


        return productRepository.getByNameContaining(Status.ACTIVE, "%" + name + "%", pageable);
    }

    @Override
    public Page<Product> getProductPage(String searchKey, boolean isAsc, String sortField, List<String>categoryIds, Double fromPrice, Double toPrice, int page, int size) {
        Specification<Product> specification = productSpecification.doFilterProduct(searchKey,isAsc,sortField,categoryIds,fromPrice,toPrice);

        PageRequest pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(specification,pageable);
    }


}
