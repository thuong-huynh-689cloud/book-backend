package com.cloud.secure.ecommerce.entities;

import com.cloud.secure.ecommerce.common.enums.ProductImagesType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImages extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "product_id", length = 32)
    private String productId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProductImagesType type;

    @Column(name = "url", length = 255)
    private String url;
}
