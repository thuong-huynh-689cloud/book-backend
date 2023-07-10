package com.cloud.secure.streaming.controllers.model.response;
import com.cloud.secure.streaming.entities.Address;
import com.cloud.secure.streaming.entities.OrderDetail;
import com.cloud.secure.streaming.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private String orderId;
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private double total;
    private String addressId;


    public OrderDetailResponse(OrderDetail orderDetail, Product product , Address address) {
        this.orderId = orderDetail.getOrderDetailId().getOrderId();
        this.productId = orderDetail.getOrderDetailId().getProductId();
        this.productName = product.getName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.total = orderDetail.getTotal();
        this.addressId= address.getId();
    }
}
