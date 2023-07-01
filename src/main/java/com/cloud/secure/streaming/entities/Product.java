package com.cloud.secure.ecommerce.entities;

import com.cloud.secure.ecommerce.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
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
@Table(name = "product")
public class Product extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "sale_price")
    private double salePrice;

    @Column(name = "default_image", columnDefinition = "TEXT")
    private String defaultImage;

    @Column(name = "quantity", length = 255)
    private int quantity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "`rank`") // add character `` b/c cannot save data to db with rank name without ``
    private int rank;

    @Column(name = "sku", length = 255)
    private String sku;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
