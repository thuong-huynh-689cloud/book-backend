package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.CoursePrice;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderDetailHelper {

    public List<OrderDetail> createOrderDetails(String orderId, List<Course> courses) {

        List<OrderDetail> orderDetails = new ArrayList<>();
        courses.forEach(course -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(UniqueID.getUUID());
            orderDetail.setCourseId(course.getId());
            orderDetail.setPoint(course.getCoursePrice() != null && course.getCoursePrice().equals(CoursePrice.FREE) ? 0 : course.getPoint());
            if (course.isShowPromotion() && course.getDiscount() > 0 && course.getExpireDate() != null &&
                    DateUtil.convertToUTC(new Date()).getTime() < course.getExpireDate().getTime()) {
                orderDetail.setDiscount(course.getDiscount());
            }
            orderDetail.setTotalPoint(orderDetail.getPoint() * (1 - (orderDetail.getDiscount() / 100)));
            orderDetail.setOrderId(orderId);
            orderDetail.setCreatedDate(DateUtil.convertToUTC(new Date()));
            orderDetails.add(orderDetail);
        });
        return orderDetails;
    }
}

