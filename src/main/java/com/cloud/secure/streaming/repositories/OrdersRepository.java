package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.OrderStatus;
import com.cloud.secure.streaming.controllers.model.response.OrderResponse;
import com.cloud.secure.streaming.entities.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrdersRepository extends JpaRepository<Orders, String> {


    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM Orders o, User u, Course c, OrderDetail od WHERE o.userId = u.id AND o.id = od.orderId " +
            "AND c.id = od.courseId AND  (:userId is null or u.id = :userId) AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersBySystemAdminAndSortByOrderDate(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                                 Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM Course c , Orders o ,User u, OrderDetail od WHERE o.userId = u.id AND o.id = od.orderId " +
            "AND c.id = od.courseId AND (:userId is null or u.id = :userId) AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersBySystemAdminAndSortByOrderTitle(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                                  Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM User u, Orders o , Course c , OrderDetail od WHERE o.userId = u.id AND o.id = od.orderId " +
            "AND c.id = od.courseId AND  (:userId is null or u.id = :userId) AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersBySystemAdminAndSortByOrderName(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                                 Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM Orders o, User u ,OrderDetail  od, Course c WHERE o.id = od.orderId AND o.userId = u.id " +
            "AND c.id = od.courseId AND c.userId = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersByInstructorIdSortByOrderDate(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                               Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM  Course c, Orders o, User u ,OrderDetail  od WHERE o.id = od.orderId AND o.userId = u.id " +
            "AND c.id = od.courseId AND c.userId = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersByInstructorIdSortByOrderTitle(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                                Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM User u ,Orders o,OrderDetail  od, Course c WHERE o.id = od.orderId AND o.userId = u.id " +
            "AND c.id = od.courseId AND u.id = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersByInstructorIdSortByOrderName(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                               Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM Orders o,User u ,OrderDetail  od, Course c WHERE o.id = od.orderId AND o.userId = u.id " +
            "AND c.id = od.courseId AND o.userId = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersByLearnerSortByOrderDate(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                          Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM  Course c,User u ,Orders o,OrderDetail  od WHERE o.id = od.orderId AND o.userId = u.id " +
            "AND c.id = od.courseId AND o.userId = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersByLearnerSortByOrderTitle(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                           Pageable pageable);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM User u ,Orders o,OrderDetail  od, Course c WHERE o.id = od.orderId AND o.userId = u.id " +
            "AND c.id = od.courseId AND o.userId = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    Page<OrderResponse> getOrdersByLearnerSortByOrderName(@Param("userId") String userId, @Param("searchKey") String searchKey,
                                                          Pageable pageable);

    List<Orders> findAllByIdIn(List<String> orderID);

    @Query("select co from  Orders co,OrderDetail cd,Course c where co.id = cd.orderId and" +
            " cd.courseId = c.id and c.id =:courseId  and co.userId = :userId")
    List<Orders> getAllByCourseIdAndUserId(@Param("courseId") String courseId,
                                           @Param("userId") String userId);

    @Query(value = "SELECT COUNT(distinct o.userId) FROM User u ," +
            "Course c ,Orders o , OrderDetail od where u.id = c.userId and c.id = od.courseId " +
            "and od.orderId = o.id and c.userId = :userId and o.orderStatus = :orderStatus and od.courseId = c.id group by u.id")
    Long getAllByUserId(@Param("userId") String userId, @Param("orderStatus") OrderStatus orderStatus);

    @Query("select count (o) FROM Orders o,OrderDetail od WHERE o.id = od.orderId AND od.courseId IN :courseId AND o.userId = :userId")
    int checkOrder(@Param("courseId") List<String> courseId,
                   @Param("userId") String userId);

    List<Orders> findAllByUserIdOrderByCreatedDateDesc(String userId);

    @Query("select new com.cloud.secure.streaming.controllers.model.response.OrderResponse(c, u, o,od) " +
            "FROM Orders o, User u, Course c, OrderDetail od WHERE c.userId = u.id AND o.id = od.orderId " +
            "AND c.id = od.courseId AND  u.id = :userId AND (u.name LIKE :searchKey OR c.title LIKE :searchKey)")
    List<OrderResponse> getAllOrderByUserId(@Param("userId") String userId);
}