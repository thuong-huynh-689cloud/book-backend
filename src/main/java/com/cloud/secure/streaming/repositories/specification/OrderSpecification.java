//package com.cloud.secure.streaming.repositories.specification;
//import com.cloud.secure.streaming.controllers.AbstractBaseController;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.criteria.*;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class OrderSpecification extends AbstractBaseController {
//    public Specification<Ordering> doFilterOrdering(
//
//            String searchKey,
//            Boolean isAsc,
//            String userId,
//            Date fromDate,
//            Date toDate,
//            OrderStatus orderStatus,
//            String sortField
//
//    ) {
//        return (Root<Ordering> orderingRoot, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
//            cq.distinct(true);
//
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (fromDate !=null && toDate != null){
////              predicates.add(cb.between(orderingRoot.get("createdDate"),fromDate,toDate));
//                predicates.add(cb.greaterThanOrEqualTo(orderingRoot.get("createdDate"), fromDate));
//                predicates.add(cb.lessThanOrEqualTo(orderingRoot.get("createdDate"), toDate));
//            }
//
//            if (orderStatus !=null ) {
//                predicates.add(cb.equal(orderingRoot.get("orderStatus"), orderStatus));
//            }
//            if (userId != null && !userId.trim().isEmpty()) {
//                predicates.add(cb.equal(orderingRoot.get("userId"), userId));
//            }
//
//            if (searchKey != null && !searchKey.trim().isEmpty()) {
//                Root<OrderDetail> orderDetailRoot = cq.from(OrderDetail.class);
//                predicates.add(cb.equal(orderingRoot.get("id"), orderDetailRoot.get("orderDetailId").get("orderId")));
//                Root<Product> productRoot = cq.from(Product.class);
//                Root<User> userRoot = cq.from(User.class);
//                predicates.add(cb.equal(orderingRoot.get("userId"),userRoot.get("id")));
//                predicates.add(cb.equal(productRoot.get("id"), orderDetailRoot.get("orderDetailId").get("productId")));
//
//                predicates.add(cb.or(cb.like(productRoot.get("name"), "%" + searchKey + "%"),
//                        cb.like(cb.concat(cb.concat(userRoot.get("firstName"), " "), userRoot.get("lastName")), "%" + searchKey + "%")));
//            }
//
//
//            Path orderClause;
//            switch (sortField) {
//                case Constant.USER_NAME:
//                    orderClause = orderingRoot.get("name");
//                    break;
//                default:
//                    orderClause = orderingRoot.get("createdDate");
//                    break;
//            }
//
//            if (isAsc) {
//                cq.orderBy(cb.asc(orderClause));
//            } else {
//                cq.orderBy(cb.desc(orderClause));
//            }
//
//            return cb.and(predicates.toArray(new Predicate[]{}));
//        };
//    }
//}
