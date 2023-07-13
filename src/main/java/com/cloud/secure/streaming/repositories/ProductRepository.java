package com.cloud.secure.streaming.repositories;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends PagingAndSortingRepository<Product, String>, JpaSpecificationExecutor<Product> {

    Product findByIdAndStatus(String id, Status status);

    List<Product> findAllByIdInAndStatus(List<String> ids, Status status);

    @Query("select p from Product p where p.status in :status and (p.name like :searchKey)")
    Page<Product> getByNameContaining(@Param("searchKey") String searchKey,
                                      @Param("status") List<Status> status,
                                      Pageable pageable);


}
