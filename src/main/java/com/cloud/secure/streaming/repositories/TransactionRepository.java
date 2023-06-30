package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.TransactionStatus;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select t from Transaction t where t.id = :transactionId and t.userId = :userId and t.chargeStatus = :chargeStatus")
    Transaction getByIdAndUserIdAndTransactionStatus(@Param("transactionId") String transactionId,
                                                         @Param("userId") String userId,
                                                         @Param("chargeStatus") String chargeStatus);

    @Query("select t from Transaction t where t.id = :transactionId and t.chargeStatus = :chargeStatus")
    Transaction getByIdAndTransactionStatus(@Param("transactionId") String transactionId,
                                                @Param("chargeStatus") String chargeStatus);

}
