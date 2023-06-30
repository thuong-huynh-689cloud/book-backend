package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldPointHistory;
import com.cloud.secure.streaming.common.enums.TransactionStatus;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

/**
 * @author 689Cloud
 */
public interface PointHistoryService {

    PointHistory save(PointHistory pointHistory);

    PointHistory getById(String id);


    PointHistory doRetryPayment(AuthUser authUser, String orderId, double totalPoint);

    PointHistory getByIdAndUserIdAndPointHistoryAndStatus(String pointHistoryId, String userId, TransactionStatus transactionStatus);

    PointHistory getByIdAndTransactionAndStatus(String pointHistoryId, TransactionStatus transactionStatus);

    PointHistory getByOrderIdAndTransactionStatus(String orderId , TransactionStatus transactionStatus);


//        Page<PointHistoryResponse> getPagePointHistory(String userId ,String searchKey , SortFieldPointHistory sortFieldPointHistory, SortDirection sortDirection, int pageNumber, int pageSize);
}
