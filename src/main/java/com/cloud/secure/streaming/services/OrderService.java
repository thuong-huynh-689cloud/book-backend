package com.cloud.secure.streaming.services;
import com.cloud.secure.streaming.common.enums.OrderStatus;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Ordering;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;


public interface OrderService {
    Ordering saveOrder(Ordering ordering);

    Ordering getById(String id);

    List<Ordering> getAllByIdInAndStatus(List<String> ids , Status status);

    void saveAll(List<Ordering> orderings);

    Page<Ordering> getOrderingPage(String searchKey, boolean isAsc, String userId, String sortField, OrderStatus orderStatus,
                                   Date fromDate, Date toDate, int pageNumber, int pageSize);


    Ordering getByIdAndUserId(String id , String userId);


}
