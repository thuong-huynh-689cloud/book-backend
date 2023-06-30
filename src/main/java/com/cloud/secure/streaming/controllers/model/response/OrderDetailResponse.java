package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.OrderDetail;
import com.cloud.secure.streaming.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {

    private String id;

    private String courseId;

    private double point;

    private double discount;

    private String ordersId;

    private String course;

    private String student;

    public OrderDetailResponse(OrderDetail orderDetail, Course course, User user){

        this.id = orderDetail.getId();
        this.courseId = orderDetail.getCourseId();
        this.point = orderDetail.getPoint();
        this.discount = orderDetail.getDiscount();
        this.ordersId = orderDetail.getOrderId();
        this.course = course.getTitle();
        this.student = user.getName();

    }
}
