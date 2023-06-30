package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.PointPackage;
import com.cloud.secure.streaming.entities.Transaction;
import com.stripe.model.Charge;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionHelper {

    public Transaction createTransaction(Charge charge, String userId, PointPackage pointPackage) {
        Transaction transaction = new Transaction();

        transaction.setId(UniqueID.getUUID());
        transaction.setUserId(userId);
        transaction.setPoint(pointPackage.getPoint());
        transaction.setPrice(pointPackage.getPrice());
        transaction.setChargeStatus(charge.getStatus());
        transaction.setCreatedDate(DateUtil.convertToUTC(new Date()));
        return transaction;
    }

}
