package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.OrderDetail;

import java.util.List;
/**
 * @author 689Cloud
 */
public interface OrderDetailService {
    void saveAll(List<OrderDetail> orderDetails);

    List<OrderDetail> getAllByCourseOrderId(String courseOrderId);

    List<OrderDetail> getAllByOrderIdIn(List<String> orderId);

    List<OrderDetail> getAllByUserId(String userId);

//    int countNumCourseEnroled(String id, List<String> courseIds);
}
