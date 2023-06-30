package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.BuyType;
import com.cloud.secure.streaming.common.enums.OrderStatus;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.Orders;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderHelper {

    /**
     * Create CourseOder API
     *
     * @param userId
     * @return
     */
    public Orders createOrder(String userId) {
        Orders orders = new Orders();
        orders.setId(UniqueID.getUUID());
        orders.setUserId(userId);
        orders.setOrderStatus(OrderStatus.ORDERED);
        orders.setBuyType(BuyType.PURCHASED_COURSE);
        orders.setCreatedDate(DateUtil.convertToUTC(new Date()));
        return orders;
    }
}
