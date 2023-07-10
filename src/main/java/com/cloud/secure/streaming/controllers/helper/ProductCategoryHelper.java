package com.cloud.secure.streaming.controllers.helper;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.ProductCategory;
import com.cloud.secure.streaming.entities.ProductCategoryId;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryHelper {

    public ProductCategory createProductCategory(String productId, Category category) {
        ProductCategory productCategory = new ProductCategory();
        ProductCategoryId productCategoryId = new ProductCategoryId();
        productCategoryId.setProductId(productId);
        productCategoryId.setCategoryId(category.getId());
        productCategory.setProductCategoryId(productCategoryId);
        return productCategory;
    }

}
