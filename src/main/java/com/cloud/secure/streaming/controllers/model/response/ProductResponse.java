package com.cloud.secure.streaming.controllers.model.response;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author DiGiEx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String id;
    private String userId;
    private String name;
    private double salePrice;
    private int quantity;
    private Status status;
    private int rank;
    private String sku;
    private Date createdDate;
    private Date updatedDate;
    private String description;
    private List<Category> categoryList;

    public ProductResponse(Product product, List<Category> categoryList) {
        this.id = product.getId();
        this.userId = product.getUserId();
        this.name = product.getName();
        this.salePrice = product.getSalePrice();
        this.quantity = product.getQuantity();
        this.status= product.getStatus();
        this.rank = product.getRank();
        this.sku = product.getSku();
        this.createdDate = product.getCreatedDate();
        this.updatedDate= product.getUpdatedDate();
        this.description = product.getDescription();
        this.categoryList = categoryList;
    }
}
