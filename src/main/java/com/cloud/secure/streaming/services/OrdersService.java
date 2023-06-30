package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldOrder;
import com.cloud.secure.streaming.controllers.model.response.OrderResponse;
import com.cloud.secure.streaming.entities.Orders;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface OrdersService {

    void save(Orders orders);

    Orders getById(String id);

    List<Orders> getAllByCourseIdAndUserId(String courseId, String userId);

    Page<OrderResponse> getPageOrderBySystemAdminAndLearner(String userId, String searchKey, SortFieldOrder sortFieldOrder,
                                                            SortDirection sortDirection, int pageNumber, int pageSize);

    Page<OrderResponse> getPageOrderByInstructor(String userId, String searchKey, SortFieldOrder sortFieldOrder,
                                                 SortDirection sortDirection, int pageNumber, int pageSize);

    Page<OrderResponse> getPageOrderByLearner(String userId, String searchKey, SortFieldOrder sortFieldOrder,
                                              SortDirection sortDirection, int pageNumber, int pageSize);

    List<Orders> getAllByIdIn(List<String> orderID);

    Long getAllByUserId(String userId);

    int checkOrder(List<String> courseId, String userId);

    List<Orders> getAllByUserIdOrderByCreatedDateDesc(String userId);

    List<OrderResponse> getAllOrderByUserId(String userId);
}
