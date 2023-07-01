package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.entities.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductHelper {
    public Product createProduct(CreateProductRequest createProductRequest, AuthUser authUser) {
        Product product = new Product();
        //add id to data
        product.setId(UniqueID.getUUID());
        //add UserId to data
        product.setUserId(authUser.getId());
        //add Name to data
        product.setName(createProductRequest.getName());
        //add SalePrice to data
        product.setSalePrice(createProductRequest.getSalePrice());
        //add DefaultImage to data
//        product.setDefaultImage(createProductRequest.getDefaultImage());

        //add Quantity to data
        product.setQuantity(createProductRequest.getQuantity());
        //add Status to data
        product.setStatus(Status.ACTIVE);
        //add Rank to data
        product.setRank(createProductRequest.getRank());
        //add Sku to data
        product.setSku(createProductRequest.getSku());
        //add Description to data
        product.setDescription(createProductRequest.getDescription());

        return product;
    }

    public Product updateProduct(Product product, UpdateProductRequest updateProductRequest) {

        // check name
        if (updateProductRequest.getName() != null && !updateProductRequest.getName().trim().isEmpty() &&
                !updateProductRequest.getName().trim().equals(product.getName())) {
            product.setName(updateProductRequest.getName().trim());
        }
        // check sale price
        if (updateProductRequest.getSalePrice() != null) {
            product.setSalePrice(updateProductRequest.getSalePrice());
        }
        // check default image
        if (updateProductRequest.getDefaultImage() != null && !updateProductRequest.getDefaultImage().trim().isEmpty() &&
                !updateProductRequest.getDefaultImage().trim().equals(product.getDefaultImage())) {
            product.setDefaultImage(updateProductRequest.getDefaultImage());
        }
        // check quantity
        if (updateProductRequest.getQuantity() != null) {
            product.setQuantity(updateProductRequest.getQuantity());
        }
        // check rank
        if (updateProductRequest.getRank() != null) {
            product.setRank(updateProductRequest.getRank());
        }
        // check sku
        if (updateProductRequest.getSku() != null && !updateProductRequest.getSku().trim().isEmpty() &&
                !updateProductRequest.getSku().trim().equals(product.getSku())) {
            product.setSku(updateProductRequest.getSku());
        }
        // check description
        if (updateProductRequest.getDescription() != null && !updateProductRequest.getDescription().trim().isEmpty() &&
                !updateProductRequest.getDescription().trim().equals(product.getDescription())){
            product.setDescription(updateProductRequest.getDescription());
        }


        return product;
    }
}
