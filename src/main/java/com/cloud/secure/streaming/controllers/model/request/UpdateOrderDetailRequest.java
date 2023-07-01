package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.entities.OrderDetailId;

import javax.persistence.Column;

public class UpdateOrderDetailRequest {

    private OrderDetailId orderDetailId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "total")
    private double total;

}
