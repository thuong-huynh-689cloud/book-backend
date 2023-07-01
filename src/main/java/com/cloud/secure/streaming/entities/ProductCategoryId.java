package com.cloud.secure.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryId implements Serializable {

    @Column(name = "product_id", length = 32)
    private String productId;

    @Column(name = "category_id", length = 32)
    private String categoryId;
}
