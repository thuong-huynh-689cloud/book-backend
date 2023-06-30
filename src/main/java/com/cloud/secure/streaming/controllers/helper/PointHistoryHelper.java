package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.entities.PointPackage;
import com.cloud.secure.streaming.entities.Transaction;
import com.cloud.secure.streaming.entities.User;
import com.stripe.model.Charge;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PointHistoryHelper {

    public PointHistory createPointHistory(String userId, String orderId, double amount, int isSuccess) {

        PointHistory pointHistory = new PointHistory();

        pointHistory.setId(UniqueID.getUUID());
        pointHistory.setUserId(userId);
        pointHistory.setPoint(-amount);
        pointHistory.setBuyType(BuyType.PURCHASED_COURSE);
        pointHistory.setTransactionStatus(isSuccess == 0 ? TransactionStatus.FAIL : TransactionStatus.SUCCESS);
        if (isSuccess == 0) {
            pointHistory.setMessage(APIStatusMessage.NOT_ENOUGH_POINT.toString());
        }
        pointHistory.setOrderId(orderId);
        pointHistory.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return pointHistory;
    }

    public PointHistory createPointHistory(String userId, double amount) {

        PointHistory pointHistory = new PointHistory();

        pointHistory.setId(UniqueID.getUUID());
        pointHistory.setUserId(userId);
        pointHistory.setPoint(amount);
        pointHistory.setBuyType(BuyType.ADMIN_GRAND_POINT);
        pointHistory.setTransactionStatus(TransactionStatus.SUCCESS);

        pointHistory.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return pointHistory;
    }

    public PointHistory createPointHistory(User user, Transaction transaction) {

        PointHistory pointHistory = new PointHistory();

        pointHistory.setId(UniqueID.getUUID());
        pointHistory.setUserId(user.getId());
        pointHistory.setTransactionId(transaction.getId());
        pointHistory.setPoint(transaction.getPoint());
        pointHistory.setBuyType(BuyType.PURCHASED_POINT);
        pointHistory.setTotalPoint(user.getTotalPoint());

        return pointHistory;
    }
}
