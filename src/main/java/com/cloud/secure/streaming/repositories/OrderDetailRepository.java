package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    List<OrderDetail> findAllByOrderId(String ordersId);

    List<OrderDetail> findAllByOrderIdIn(List<String> orderId);

    @Query("select od from OrderDetail od,Orders o where od.orderId = o.id and o.userId = :userId ")
    List<OrderDetail> findAllByUserId(@Param("userId") String userId);


//    @Query("select cod from OrderDetail cod left join Course c on cod.courseId = c.id " +
//            "left join User u on c.userId = u.id where (c.title like :searchKey or u.name like :searchKey)")
//    Page<OrderDetail> findCourseOrderDetailPage(@Param("searchKey") String searchKey,
//                                                Pageable pageable);
}
