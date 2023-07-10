package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.OrderStatus;
import com.cloud.secure.streaming.common.enums.PaymentMethod;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Ordering;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DiGiEx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String id;

    private String userId;

    private OrderStatus orderStatus;

    private Status status;

    private PaymentMethod paymentMethod;

    private double total;

    private List<OrderDetailResponse> orderDetailResponse;

    public OrderResponse(Ordering ordering, List<OrderDetailResponse> orderDetailResponse ) {
        this.id = ordering.getId();
        this.userId = ordering.getUserId();
        this.orderStatus = ordering.getOrderStatus();
        this.status = ordering.getStatus();
        this.paymentMethod = ordering.getPaymentMethod();
        this.total = ordering.getTotal();
        this.orderDetailResponse = orderDetailResponse;
    }
}
