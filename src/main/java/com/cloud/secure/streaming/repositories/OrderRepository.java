package com.cloud.secure.ecommerce.repositories;


import com.cloud.secure.ecommerce.entities.Ordering;

import com.cloud.secure.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Status;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends PagingAndSortingRepository<Ordering, String>, JpaSpecificationExecutor<Ordering> {

    Ordering findByIdAndStatus(String id, Status status);

    List<Ordering> findAllByIdInAndStatus(List<String> ids , com.cloud.secure.ecommerce.common.enums.Status  status);

    Ordering findByIdAndUserId(String id , String userId);


}
