package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.model.request.AddCardRequest;
import com.cloud.secure.streaming.controllers.model.response.CardResponse;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.gateway.model.CustomCreditCard;
import com.cloud.secure.streaming.services.StripePaymentService;
import com.cloud.secure.streaming.services.UserService;
import com.stripe.model.Card;
import com.stripe.model.Customer;
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
@RequestMapping(ApiPath.CARD_API)
@Slf4j
public class CardManagementController extends AbstractBaseController {

    final UserService userService;

    final StripePaymentService stripePaymentService;

    public CardManagementController(UserService userService, StripePaymentService stripePaymentService) {
        this.userService = userService;
        this.stripePaymentService = stripePaymentService;
    }

    /**
     * Add Card
     *
     * @param authUser
     * @param addCardRequest
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @PostMapping()
    @Operation(summary = "Add Card")
    public ResponseEntity<RestAPIResponse> addCard(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody AddCardRequest addCardRequest
    ) {
        User user = userService.getById(authUser.getId());
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        // get customer by customerId
        Customer customer = null;
        if (user.getStripeCustomerId() != null && !"".equals(user.getStripeCustomerId())) {
            customer = stripePaymentService.getCustomer(user);
        }
        if (customer == null) {
            // create customer
            customer = stripePaymentService.createCustomer(user.getEmail());
            // add customerId for user info
            user.setStripeCustomerId(customer.getId());
            userService.save(user);
        }
        // add card
        Card card = stripePaymentService.createCard(addCardRequest.getCardToken(), customer.getId(),
                authUser.getUsername());
        Validator.notNull(card, RestAPIStatus.ERR_CREATE_CARD, APIStatusMessage.CANNOT_ADD_CARD);

        // A failed CVC check can be an indication of fraud
        // https://stripe.com/docs/radar/rules#traditional-bank-checks
        // validate cvc number check
        if ("fail".equals(card.getCvcCheck())) {
            // delete card token from stripe
            stripePaymentService.deleteCard(addCardRequest.getCardToken(), user.getStripeCustomerId(), user.getEmail());
            throw new ApplicationException(RestAPIStatus.ERR_CARD_CVC);
        }

        // delete old card
        if (user.getCardToken() != null && !"".equals(user.getCardToken())) {
            stripePaymentService.deleteCard(user.getCardToken(), user.getStripeCustomerId(), user.getEmail());
        }

        String expireDate = card.getExpMonth() + "/" + card.getExpYear();

        // get card type
        String cardUserType = card.getBrand();

        // create new card user info
        user.setCardHolder(addCardRequest.getCardHolder());
        user.setCardToken(addCardRequest.getCardToken());
        user.setCardNumber(card.getLast4());
        user.setCardExpDate(expireDate);
        user.setCardType(cardUserType != null ? CardType.valueOf(card.getBrand().toUpperCase().replaceAll(" ", "_")) : CardType.UNKNOWN);

        userService.save(user);

        return responseUtil.successResponse(new CardResponse(user));
    }

    /**
     * Delete Card API
     *
     * @param authUser
     * @return
     */
    @AuthorizeValidator(UserType.LEARNER)
    @DeleteMapping
    @Operation(summary = "Delete Card")
    public ResponseEntity<RestAPIResponse> deleteCard(
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        User user = userService.getById(authUser.getId());
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);

        // get customer by customerId
        Customer customer = null;
        if (user.getStripeCustomerId() != null && !"".equals(user.getStripeCustomerId())) {
            customer = stripePaymentService.getCustomer(user);
        }
        if (customer == null || user.getCardToken() == null || user.getCardToken().trim().isEmpty()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CARD_HAS_NOT_BEEN_ADDED);
        }

        // delete old card
        if (user.getCardToken() != null && !"".equals(user.getCardToken())) {
            stripePaymentService.deleteCard(user.getCardToken(), user.getStripeCustomerId(), user.getEmail());
        }
        // create new card user info
        user.setCardHolder(null);
        user.setCardToken(null);
        user.setCardNumber(null);
        user.setCardExpDate(null);
        user.setCardType(null);

        userService.save(user);

        return responseUtil.successResponse(user);
    }

    @AuthorizeValidator(UserType.LEARNER)
    @GetMapping
    @Operation(summary = "Get Card")
    public ResponseEntity<RestAPIResponse> getCard(
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        User user = userService.getById(authUser.getId());
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        // get customer by customerId
        Customer customer = null;
        if (user.getStripeCustomerId() != null && !"".equals(user.getStripeCustomerId())) {
            customer = stripePaymentService.getCustomer(user);
        }
//        if (customer == null || user.getCardToken() == null || user.getCardToken().trim().isEmpty()) {
//            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.CARD_HAS_NOT_BEEN_ADDED);
//        }
        CustomCreditCard card = null;
        if (user.getCardToken() != null && !user.getCardToken().isEmpty()) {
            //get card info
            card = stripePaymentService.getCardInfoByToken(user.getCardToken(), user);

        }
        return responseUtil.successResponse(card);
    }
}
