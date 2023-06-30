package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.TransactionStatus;
import com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse;
import com.cloud.secure.streaming.entities.PointHistory;
import com.cloud.secure.streaming.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PointHistoryRepository extends JpaRepository<PointHistory, String> {
//
//    @Query("select new com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse(p,u,pp) " +
//            "from PointHistory p , User u ,PointPackage  pp where  p.userId = u.id and (:userId is null or u.id = :userId) " +
//            "and (u.name like :searchKey or u.email like :searchKey) and p.pointPackageId = pp.id")
//    Page<PointHistoryResponse> getPagePointHistorySortByDate(@Param("userId") String userId,
//                                                             @Param("searchKey") String searchKey,
//                                                             Pageable pageable);
//
//    @Query("select new com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse(p,u,pp) " +
//            "from User u, PointHistory p ,PointPackage  pp where  p.userId = u.id and (:userId is null or u.id = :userId) " +
//            "and (u.name like :searchKey or u.email like :searchKey and p.pointPackageId = pp.id)")
//    Page<PointHistoryResponse> getPagePointHistorySortByName(@Param("userId") String userId,
//                                                             @Param("searchKey") String searchKey,
//                                                             Pageable pageable);
//
//    @Query("select new com.cloud.secure.streaming.controllers.model.response.PointHistoryResponse(p,u,pp) " +
//            "from User u, PointHistory p,PointPackage  pp  where  p.userId = u.id and (:userId is null or u.id = :userId)" +
//            " and (u.name like :searchKey or u.email like :searchKey) and p.pointPackageId = pp.id")
//    Page<PointHistoryResponse> getPagePointHistorySortByEmail(@Param("userId") String userId,
//                                                              @Param("searchKey") String searchKey,
//                                                              Pageable pageable);

    @Query("select p from PointHistory p where p.id = :pointHistoryId and p.userId = :userId and p.transactionStatus = :transactionStatus")
    PointHistory getByIdAndUserIdAndTransactionAndStatus(@Param("pointHistoryId") String pointHistoryId,
                                                     @Param("userId") String userId,
                                                     @Param("transactionStatus") TransactionStatus transactionStatus);

    @Query("select p from PointHistory p where p.id = :pointHistoryId and p.transactionStatus = :transactionStatus")
    PointHistory getByIdAndTransactionAndStatus(@Param("pointHistoryId") String pointHistoryId,
                                                         @Param("transactionStatus") TransactionStatus transactionStatus);


}
