package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.BankInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface BankInformationRepository extends JpaRepository<BankInformation, String> {
    void deleteAllByIdIn(List<String> ids);

    List<BankInformation> findAllByIdIn(List<String> ids);


    @Query("select b from BankInformation b where b.bankName like :searchKey or b.branchName like :searchKey")
    Page<BankInformation> findPageBankInformation(@Param("searchKey") String searchKey,
                                                  Pageable pageable);

    List<BankInformation> findAllByUserIdOrderByCreatedDateDesc(String userId);

    @Query("select b from BankInformation b where b.userId like :userId and (b.bankName like :searchKey or b.branchName like :searchKey)")
    Page<BankInformation> findPageBankInformationByUserId(@Param("userId") String userId,
                                                          @Param("searchKey") String searchKey,
                                                          Pageable pageable);
}
