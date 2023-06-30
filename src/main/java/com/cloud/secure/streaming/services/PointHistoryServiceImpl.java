package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldPointHistory;
import com.cloud.secure.streaming.common.enums.TransactionStatus;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.helper.PointHistoryHelper;
import com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.repositories.PointHistoryRepository;
import com.cloud.secure.streaming.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author 689Cloud
 */
@Service
public class PointHistoryServiceImpl implements PointHistoryService {

    final PointHistoryRepository pointHistoryRepository;
    final UserRepository userRepository;
    final PointHistoryHelper pointHistoryHelper;

    public PointHistoryServiceImpl(PointHistoryRepository pointHistoryRepository, UserRepository userRepository, PointHistoryHelper pointHistoryHelper) {
        this.pointHistoryRepository = pointHistoryRepository;
        this.userRepository = userRepository;
        this.pointHistoryHelper = pointHistoryHelper;
    }

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryRepository.save(pointHistory);
    }

    @Override
    public PointHistory getById(String id) {
        return pointHistoryRepository.findById(id).orElse(null);
    }
    /**
     * Do Retry Course Order
     *
     * @param authUser
     * @param totalPoint
     * @param orderId
     */
    @Override
    @Retryable(value = {ApplicationException.class}, maxAttempts = 2,
            backoff = @Backoff(maxDelay = 100))
    public PointHistory doRetryPayment(AuthUser authUser, String orderId, double totalPoint) {
        // check point user
        int isSuccess = userRepository.updateUserPointById(totalPoint, authUser.getId());

        return pointHistoryHelper.createPointHistory(authUser.getId(), orderId, totalPoint, isSuccess);
    }

    @Override
    public PointHistory getByIdAndUserIdAndPointHistoryAndStatus(String pointHistoryId, String userId, TransactionStatus transactionStatus) {
        return pointHistoryRepository.getByIdAndUserIdAndTransactionAndStatus(pointHistoryId, userId, transactionStatus);
    }

    @Override
    public PointHistory getByIdAndTransactionAndStatus(String pointHistoryId, TransactionStatus transactionStatus) {
        return pointHistoryRepository.getByIdAndTransactionAndStatus(pointHistoryId,transactionStatus);
    }

    @Override
    public PointHistory getByOrderIdAndTransactionStatus(String orderId, TransactionStatus transactionStatus) {
        return pointHistoryRepository.getByIdAndTransactionAndStatus(orderId, transactionStatus);
    }


//    @Override
//    public Page<PointHistoryResponse> getPagePointHistory(String userId, String searchKey, SortFieldPointHistory sortFieldPointHistory, SortDirection sortDirection, int pageNumber, int pageSize) {
//
//        Sort.Direction direction = (sortDirection.equals(SortDirection.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        Sort sort;
//        Pageable pageable;
//        switch (sortFieldPointHistory) {
//            case name:
//                sort = Sort.by(direction, sortFieldPointHistory.toString());
//                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
//                return pointHistoryRepository.getPagePointHistorySortByName(userId, "%" + searchKey + "%", pageable);
//            case email:
//                sort = Sort.by(direction, sortFieldPointHistory.toString());
//                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
//                return pointHistoryRepository.getPagePointHistorySortByEmail(userId, "%" + searchKey + "%", pageable);
//            default:
//                sort = Sort.by(direction, sortFieldPointHistory.toString());
//                pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
//                return pointHistoryRepository.getPagePointHistorySortByDate(userId, "%" + searchKey + "%", pageable);
//        }
//    }
}