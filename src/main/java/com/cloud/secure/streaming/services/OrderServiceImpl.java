//package com.cloud.secure.streaming.services;
//
//import com.cloud.secure.streaming.common.enums.OrderStatus;
//import com.cloud.secure.streaming.common.enums.Status;
//import com.cloud.secure.streaming.entities.Ordering;
//import com.cloud.secure.streaming.repositories.OrderRepository;
//import com.cloud.secure.streaming.repositories.specification.OrderSpecification;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class OrderServiceImpl implements  OrderService{
//    final OrderRepository orderRepository;
//    final OrderSpecification orderingSpecification;
//
//    public OrderServiceImpl(OrderRepository orderRepository, OrderSpecification orderingSpecification) {
//        this.orderRepository = orderRepository;
//        this.orderingSpecification = orderingSpecification;
//    }
//
//
//    @Override
//    public Ordering saveOrder(Ordering ordering) {
//        return orderRepository.save(ordering);
//    }
//
//    @Override
//    public Ordering getById(String id) {
//        return orderRepository.findByIdAndStatus(id,Status.ACTIVE);
//    }
//
//    @Override
//    public List<Ordering> getAllByIdInAndStatus(List<String> ids , Status status) {
//        return orderRepository.findAllByIdInAndStatus(ids, Status.ACTIVE);
//    }
//
//    @Override
//    public void saveAll(List<Ordering> orderings) {
//        orderRepository.saveAll(orderings);
//    }
//
//    @Override
//    public Page<Ordering> getOrderingPage(String searchKey, boolean isAsc, String userId, String sortField, OrderStatus orderStatus, Date fromDate, Date toDate, int pageNumber, int pageSize) {
//        Specification<Ordering> specification = orderingSpecification.doFilterOrdering(searchKey, isAsc,userId,fromDate,toDate ,orderStatus, sortField );
//        PageRequest pageable = PageRequest.of(pageNumber-1,pageSize);
//
//        return orderRepository.findAll(specification,pageable);
//    }
//
//    @Override
//    public Ordering getByIdAndUserId(String id, String userId) {
//        return orderRepository.findByIdAndUserId(id, userId);
//    }
//}
//
