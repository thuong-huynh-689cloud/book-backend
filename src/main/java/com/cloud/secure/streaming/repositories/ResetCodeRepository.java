package com.cloud.secure.ecommerce.repositories;

import com.cloud.secure.ecommerce.common.enums.ResetCodeType;
import com.cloud.secure.ecommerce.entities.ResetCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ResetCodeRepository extends PagingAndSortingRepository<ResetCode, String> {

    ResetCode findByIdAndType(String verifyKey, ResetCodeType type);

}
