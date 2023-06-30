package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.ResponseUtil;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.PointHistoryHelper;
import com.cloud.secure.streaming.controllers.helper.TransactionHelper;
import com.cloud.secure.streaming.controllers.model.request.CreatePaymentRequest;
import com.cloud.secure.streaming.controllers.model.request.PaymentRequest;
import com.cloud.secure.streaming.controllers.model.response.PaymentResponse;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.*;
import com.stripe.model.Charge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.TRANSACTION_API)
@Slf4j
public class PaymentController extends AbstractBaseController {

    final TransactionService transactionService;
    final TransactionHelper transactionHelper;
    final OrdersService ordersService;
    final UserService userService;
    final PointPackageService pointPackageService;
    final StripePaymentService stripePaymentService;
    final PointHistoryHelper pointHistoryHelper;
    final PointHistoryService pointHistoryService;

    public PaymentController(TransactionService transactionService, TransactionHelper transactionHelper, OrdersService ordersService, UserService userService, PointPackageService pointPackageService, StripePaymentService stripePaymentService, PointHistoryHelper pointHistoryHelper, PointHistoryService pointHistoryService) {
        this.transactionService = transactionService;
        this.transactionHelper = transactionHelper;
        this.ordersService = ordersService;
        this.userService = userService;
        this.pointPackageService = pointPackageService;
        this.stripePaymentService = stripePaymentService;
        this.pointHistoryHelper = pointHistoryHelper;
        this.pointHistoryService = pointHistoryService;
    }

    /**
     * Create Payment API
     *
     * @param authUser
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PostMapping()
    @Operation(summary = "Create Payment")
    public ResponseEntity<RestAPIResponse> createPayment(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody CreatePaymentRequest createPaymentRequest
    ) {
        // get order
        Orders orders = ordersService.getById(createPaymentRequest.getOrderId());
        Validator.notNull(orders, RestAPIStatus.NOT_FOUND, APIStatusMessage.ORDER_NOT_FOUND);

        // check pointHistoryExisted
        PointHistory pointHistoryExisted = pointHistoryService.getByOrderIdAndTransactionStatus(orders.getId(), TransactionStatus.SUCCESS);
        Validator.mustNull(pointHistoryExisted, RestAPIStatus.EXISTED, APIStatusMessage.ORDER_EXISTED);
        // create transaction
        PointHistory pointHistory = pointHistoryService.doRetryPayment(authUser, orders.getId(), orders.getTotalPoint());

        pointHistoryService.save(pointHistory);

        return responseUtil.successResponse(pointHistory);
    }

    /**
     * update Status Orders API
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update Status Orders")
    public ResponseEntity<RestAPIResponse> updateStatusOrders(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable("id") String id   // transactionId
    ) {
        // get pointHistory
        PointHistory pointHistory = pointHistoryService.getByIdAndUserIdAndPointHistoryAndStatus(id, authUser.getId(),
                TransactionStatus.SUCCESS);
        Validator.notNull(pointHistory, RestAPIStatus.NOT_FOUND, APIStatusMessage.TRANSACTION_NOT_FOUND);

        // get order
        Orders orders = ordersService.getById(pointHistory.getOrderId());
        Validator.notNull(orders, RestAPIStatus.NOT_FOUND, APIStatusMessage.ORDER_NOT_FOUND);
        // check TransactionStatus
        if (TransactionStatus.SUCCESS.equals(pointHistory.getTransactionStatus())) {
            // set OrderStatus
            orders.setOrderStatus(OrderStatus.PAID);

        } else {
            // set OrderStatus
            orders.setOrderStatus(OrderStatus.FAIL);
        }
        ordersService.save(orders);
        pointHistoryService.save(pointHistory);

        return responseUtil.successResponse(orders);
    }

    /**
     * Learner Buy Point API
     *
     * @param authUser
     * @param paymentRequest
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PutMapping(value = "/card")
    @Operation(summary = "Buy Point")
    public ResponseEntity<RestAPIResponse> buyPoint(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody PaymentRequest paymentRequest
    ) {
        //check user
        User user = userService.getById(authUser.getId());
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        if (user.getCardToken() == null || user.getCardToken().trim().isEmpty()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CARD_HAS_NOT_BEEN_ADDED);
        }
        //check point package
        PointPackage pointPackage = pointPackageService.getById(paymentRequest.getPointPackageId());
        Validator.notNull(pointPackage, RestAPIStatus.NOT_FOUND, APIStatusMessage.POINT_NOT_FOUND);
        //make payment
        Charge charge;
        try {
            charge = stripePaymentService.payment(user.getStripeCustomerId(), Math.round(pointPackage.getPrice()));
            Validator.notNull(charge, RestAPIStatus.ERR_STRIPE, APIStatusMessage.STRIPE_CANT_CHARGE);
        } catch (Exception e) {
            throw new ApplicationException(RestAPIStatus.ERR_STRIPE, "Stripe exception error " + e.getMessage());
        }

        // check payment success
        if ("succeeded".equals(charge.getStatus())) {
            user.setTotalPoint(user.getTotalPoint() + pointPackage.getPoint());
            userService.save(user);
        }
        // buy point transaction
        Transaction transaction = transactionHelper.createTransaction(charge, user.getId(), pointPackage);
        transactionService.save(transaction);
        //add point history
        PointHistory pointHistory = pointHistoryHelper.createPointHistory(user, transaction);
        pointHistoryService.save(pointHistory);
        return responseUtil.successResponse(new PaymentResponse(user, transaction, pointPackage));
    }

    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Transaction")
    public ResponseEntity<RestAPIResponse> getTransaction(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        // check transaction
        Transaction transaction = transactionService.getById(id);
        Validator.notNull(transaction, RestAPIStatus.NOT_FOUND, APIStatusMessage.TRANSACTION_NOT_FOUND);
        // check permission
        if (authUser.getType().equals(UserType.LEARNER) && !authUser.getId().equals(transaction.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
        }

        return responseUtil.successResponse(transaction);
    }

}
