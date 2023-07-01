package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.controllers.helper.OrderDetailHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.ORDER_DETAIL_API)
public class OrderDetailController  extends AbstractBaseController{
    final OrderDetailService orderDetailService;
    final OrderDetailHelper orderDetailHelper;

    public OrderDetailController(OrderDetailService orderDetailService, OrderDetailHelper orderDetailHelper) {
        this.orderDetailService = orderDetailService;
        this.orderDetailHelper = orderDetailHelper;
    }


    @PostMapping
    public ResponseEntity<RestAPIResponse> login(
    ) {
        return responseUtil.successResponse("");
    }
}

