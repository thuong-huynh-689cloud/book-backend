package com.cloud.secure.streaming.entities;

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
public class OrderDetailId implements Serializable {

    @Column(name = "order_id", length = 32)
    private String orderId;

    @Column(name = "product_id", length = 32)
    private String productId;


}
