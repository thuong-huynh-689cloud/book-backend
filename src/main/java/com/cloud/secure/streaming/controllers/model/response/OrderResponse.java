package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.OrderDetail;
import com.cloud.secure.streaming.entities.Orders;

import com.cloud.secure.streaming.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String id;
    private String title;
    private String name;
    private String image;
    private Date createdDate;
    private double totalPoint;

    public OrderResponse(Course course, User user, Orders order, OrderDetail orderDetail) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.name = user.getName();
        this.image = course.getImage();
        this.createdDate = order.getCreatedDate();
        this.totalPoint = orderDetail.getTotalPoint();
    }
}
