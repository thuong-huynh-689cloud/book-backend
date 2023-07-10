package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.repositories.ProductImagesRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductImagesServiceImpl implements ProductImagesService{
    final ProductImagesRepository productImagesRepository;

    public ProductImagesServiceImpl(ProductImagesRepository productImagesRepository) {
        this.productImagesRepository = productImagesRepository;
    }
}
