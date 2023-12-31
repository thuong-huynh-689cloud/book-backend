package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateProductRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateProductRequest;
import com.cloud.secure.streaming.entities.Product;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductHelper {
    public Product createProduct(CreateProductRequest createProductRequest, String userId) {
        Product product = new Product();
        //add id to data
        product.setId(UniqueID.getUUID());
        //add UserId to data
        product.setUserId(userId);
        //add Name to data
        product.setName(createProductRequest.getName());
        //add SalePrice to data
        product.setPrice(createProductRequest.getPrice());
        //add Quantity to data
        product.setQuantity(createProductRequest.getQuantity());
        //add Status to data
        product.setStatus(Status.ACTIVE);
        //add Description to data
        product.setDescription(createProductRequest.getDescription());
        //add image to data
        product.setImage(createProductRequest.getImage());
        // add createDate to date
        product.setCreatedDate(DateUtil.convertToUTC(new Date()));
        return product;
    }

    public Product updateProduct(Product product, UpdateProductRequest updateProductRequest) {

        // check name
        if (updateProductRequest.getName() != null && !updateProductRequest.getName().trim().isEmpty() &&
                !updateProductRequest.getName().trim().equals(product.getName())) {
            product.setName(updateProductRequest.getName().trim());
        }
        // check sale price
        if (updateProductRequest.getPrice() != null) {
            product.setPrice(updateProductRequest.getPrice());
        }
        // check quantity
        if (updateProductRequest.getQuantity() != null) {
            product.setQuantity(updateProductRequest.getQuantity());
        }
        // check description
        if (updateProductRequest.getDescription() != null && !updateProductRequest.getDescription().trim().isEmpty() &&
                !updateProductRequest.getDescription().trim().equals(product.getDescription())) {
            product.setDescription(updateProductRequest.getDescription());
        }
        // check description
        if (updateProductRequest.getImage() != null && !updateProductRequest.getImage().trim().isEmpty() &&
                !updateProductRequest.getImage().trim().equals(product.getImage())) {
            product.setImage(updateProductRequest.getImage());
        }

        return product;
    }
}
