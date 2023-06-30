package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.controllers.helper.OrderHelper;
import com.cloud.secure.streaming.controllers.model.response.OrderResponse;
import com.cloud.secure.streaming.entities.Orders;
import com.cloud.secure.streaming.repositories.OrdersRepository;
import com.cloud.secure.streaming.repositories.UserRepository;
import com.cloud.secure.streaming.repositories.specification.OrderSpecification;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class OrdersServiceImpl implements OrdersService {
    final OrdersRepository orderRepository;
    final UserRepository userRepository;
    final OrderHelper orderHelper;
    final OrderSpecification orderSpecification;

    public OrdersServiceImpl(OrdersRepository orderRepository,
                             UserRepository userRepository,
                             @Lazy OrderHelper orderHelper, OrderSpecification orderSpecification) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderHelper = orderHelper;
        this.orderSpecification = orderSpecification;
    }

    @Override
    public void save(Orders orders) {
        orderRepository.save(orders);
    }

    @Override
    public Orders getById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Orders> getAllByCourseIdAndUserId(String courseId, String userId) {
        return orderRepository.getAllByCourseIdAndUserId(courseId, userId);
    }

    @Override
    public Page<OrderResponse> getPageOrderBySystemAdminAndLearner(String userId, String searchKey, SortFieldOrder sortFieldOrder,
                                                                   SortDirection sortDirection, int pageNumber, int pageSize) {

        Sort.Direction direction = (sortDirection.equals(SortDirection.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort;
        Pageable pageable;
        switch (sortFieldOrder) {
            case name:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersBySystemAdminAndSortByOrderName(userId, "%" + searchKey + "%", pageable);
            case title:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersBySystemAdminAndSortByOrderTitle(userId, "%" + searchKey + "%", pageable);
            default:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersBySystemAdminAndSortByOrderDate(userId, "%" + searchKey + "%", pageable);
        }
    }

    @Override
    public Page<OrderResponse> getPageOrderByInstructor(String userId, String searchKey, SortFieldOrder sortFieldOrder,
                                                        SortDirection sortDirection, int pageNumber, int pageSize) {

        Sort.Direction direction = (sortDirection.equals(SortDirection.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort;
        Pageable pageable;
        switch (sortFieldOrder) {
            case name:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersByInstructorIdSortByOrderName(userId, "%" + searchKey + "%", pageable);

            case title:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersByInstructorIdSortByOrderTitle(userId, "%" + searchKey + "%", pageable);

            default:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersByInstructorIdSortByOrderDate(userId, "%" + searchKey + "%", pageable);
        }
    }

    @Override
    public Page<OrderResponse> getPageOrderByLearner(String userId, String searchKey, SortFieldOrder sortFieldOrder, SortDirection sortDirection, int pageNumber, int pageSize) {

        Sort.Direction direction = (sortDirection.equals(SortDirection.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort;
        Pageable pageable;
        switch (sortFieldOrder) {
            case name:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersByLearnerSortByOrderName(userId, "%" + searchKey + "%", pageable);

            case title:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersByLearnerSortByOrderTitle(userId, "%" + searchKey + "%", pageable);

            default:
                sort = Sort.by(direction, sortFieldOrder.toString());
                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return orderRepository.getOrdersByLearnerSortByOrderDate(userId, "%" + searchKey + "%", pageable);
        }
    }

    @Override
    public List<Orders> getAllByIdIn(List<String> orderID) {
        return orderRepository.findAllByIdIn(orderID);
    }

    @Override
    public Long getAllByUserId(String userId) {
        return orderRepository.getAllByUserId(userId, OrderStatus.PAID);
    }

    @Override
    public int checkOrder(List<String> courseId, String userId) {
        return orderRepository.checkOrder(courseId, userId);
    }

    @Override
    public List<Orders> getAllByUserIdOrderByCreatedDateDesc(String userId) {
        return orderRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }

    @Override
    public List<OrderResponse> getAllOrderByUserId(String userId) {
        return orderRepository.getAllOrderByUserId(userId);
    }
}

