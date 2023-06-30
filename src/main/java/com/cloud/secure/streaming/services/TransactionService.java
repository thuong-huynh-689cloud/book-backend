package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.TransactionStatus;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.entities.Transaction;
import org.springframework.data.repository.query.Param;

/**
 * @author 689Cloud
 */
public interface TransactionService {

    Transaction save(Transaction transaction);

    void delete(Transaction transaction);

    Transaction getById(String id);

    Transaction getByIdAndUserIdAndTransactionStatus(String transactionId, String userId, String chargeStatus);

    Transaction getByIdAndTransactionStatus(String transactionId, String chargeStatus);
}

