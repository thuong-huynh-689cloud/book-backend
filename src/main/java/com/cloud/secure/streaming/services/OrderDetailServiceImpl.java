package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.OrderDetail;
import com.cloud.secure.streaming.repositories.OrderDetailRepository;

import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author 689Cloud
 */
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    final OrderDetailRepository orderDetailRepository;


    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void saveAll(List<OrderDetail> orderDetails) {
        orderDetailRepository.saveAll(orderDetails);
    }

    @Override
    public List<OrderDetail> getAllByCourseOrderId(String courseOrderId) {
        return orderDetailRepository.findAllByOrderId(courseOrderId);
    }

    @Override
    public List<OrderDetail> getAllByOrderIdIn(List<String> orderId) {
        return orderDetailRepository.findAllByOrderIdIn(orderId);
    }

    @Override
    public List<OrderDetail> getAllByUserId(String userId) {
        return orderDetailRepository.findAllByUserId(userId);
    }
}

