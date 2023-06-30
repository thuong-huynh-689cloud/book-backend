package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.TransactionStatus;
import com.cloud.secure.streaming.controllers.helper.TransactionHelper;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.entities.Transaction;
import com.cloud.secure.streaming.repositories.TransactionRepository;
import com.cloud.secure.streaming.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author 689Cloud
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    final TransactionRepository transactionRepository;
    final UserRepository userRepository;
    final TransactionHelper transactionHelper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, TransactionHelper transactionHelper) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.transactionHelper = transactionHelper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public Transaction getById(String id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaction getByIdAndUserIdAndTransactionStatus(String transactionId, String userId, String chargeStatus) {
        return transactionRepository.getByIdAndUserIdAndTransactionStatus(transactionId, userId, chargeStatus);
    }

    @Override
    public Transaction getByIdAndTransactionStatus(String transactionId, String chargeStatus) {
        return transactionRepository.getByIdAndTransactionStatus(transactionId, chargeStatus);
    }
}
