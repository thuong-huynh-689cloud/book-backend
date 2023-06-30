package com.cloud.secure.streaming.repositories.specification;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.OrderDetail;
import com.cloud.secure.streaming.entities.Orders;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 689Cloud
 */
@Component
public class CourseSpecification {

    public Specification<Course> doFilterCourse(
            String searchKey,
            SortFieldCourse sortFieldCourse,
            List<CourseLevel> courseLevels,
            List<Language> languages,
            List<CoursePrice> coursePrices,
            boolean ascSort

    ) {
        return (
                Root<Course> courseRoot, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
//            cq.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(courseRoot.get("status"), AppStatus.ACTIVE));
//
//            if (searchKey != null && !searchKey.trim().isEmpty()) {
//                Root<OrderDetail> orderDetailRoot = cq.from(OrderDetail.class);
//                predicates.add(cb.equal(courseRoot.get("id"), orderDetailRoot.get("courseId")));
//                Root<Orders> ordersRoot = cq.from(Orders.class);
//                predicates.add(cb.equal(ordersRoot.get("id"), orderDetailRoot.get("orderId")));
//
//                predicates.add(cb.or(cb.like(courseRoot.get("title"), "%" + searchKey + "%")));
//                cq.groupBy(ordersRoot.get("userId"));
//            }
//
//            Expression<Long> orderClause=courseRoot.get("createdDate");
//            switch (sortFieldCourse) {
//                case title:
//                    orderClause = courseRoot.get("title");
//                    break;
//                case point:
//                    orderClause = courseRoot.get("point");
//                    break;
//                case numberOfUsers:
//
//                    Join<Course, OrderDetail> ordersJoin = courseRoot.join("orderDetail", JoinType.LEFT);
//                    ordersJoin.on(cb.equal(ordersJoin.get("courseId"), courseRoot.get("id")));
//
////                    Join<Course, OrderDetail> orderDetails = courseRoot.join("orderDetails", JoinType.LEFT);
////                    orderDetails.on(cb.equal(orderDetails.get("courseId"), courseRoot.get("id")));
//
////                    Root<OrderDetail> orderDetailRoot = cq.from(OrderDetail.class);
////                    Join<OrderDetail, Orders> orders = orderDetails.join("orders", JoinType.LEFT);
////                    orders.on(cb.equal(orders.get("id"), orderDetails.get("orderId")));
//
////                    predicates.add(cb.equal(courseRoot.get("id"), orderDetailRoot.get("courseId")));
////                    predicates.add(cb.equal(ordersRoot.get("id"), orderDetailRoot.get("orderId")));
////                    cq.groupBy(orders.get("userId"));
//
////                    orderClause = cb.count(orders.get("userId"));
//                    break;
//
//                default:
//                    orderClause = courseRoot.get("createdDate");
//            }
//
//
//            if (ascSort) {
//                cq.orderBy(cb.asc(orderClause));
//            } else {
//                cq.orderBy(cb.desc(orderClause));
//            }
//
//
//            if (courseLevels != null && !courseLevels.isEmpty()) {
//                predicates.add(courseRoot.get("level").in(courseLevels));
//            }
//
//            if (languages != null && !languages.isEmpty()) {
//                predicates.add(courseRoot.get("language").in(languages));
//            }
//
//            if (coursePrices != null && !coursePrices.isEmpty()) {
//                predicates.add(courseRoot.get("coursePrice").in(coursePrices));
//            }

            Join<Course, OrderDetail> leftJoin = courseRoot.join("orderDetail", JoinType.LEFT);
            leftJoin.on(cb.equal(leftJoin.get("courseId"), courseRoot.get("id")));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
