package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface CategoryRepository extends PagingAndSortingRepository<Category, String> {

    Category findByIdAndStatus(String id, Status status);

    List<Category> findAllByIdInAndStatus(List<String> ids, Status status);

    @Query("select c from Category c where c.status = :status and c.name like :searchKey")
    Page<Category> getByNameContaining(@Param("status") Status status,
                                       @Param("searchKey") String searchKey,
                                       Pageable pageable);

    //found status
    List<Category> findByStatus(Status status);


    @Query("select c from Category c, Product p, ProductCategory pc where c.id = pc.productCategoryId.categoryId and p.id = pc.productCategoryId.productId and p.id = :productId")
    List<Category> getAllByProductId(@Param("productId") String productId);

    Category findByNameAndStatus(String name , Status status);


}
