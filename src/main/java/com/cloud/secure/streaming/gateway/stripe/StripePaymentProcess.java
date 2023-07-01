//package com.cloud.secure.streaming.gateway.stripe;
//
//import com.cloud.secure.streaming.common.exceptions.ApplicationException;
//import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
//import com.cloud.secure.streaming.entities.User;
//import com.cloud.secure.streaming.gateway.PaymentBasedProcess;
//import com.cloud.secure.streaming.gateway.model.CustomCreditCard;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.*;
//import com.stripe.param.ChargeCreateParams;
//import com.stripe.param.PaymentSourceCollectionCreateParams;
//import com.stripe.param.RefundCreateParams;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.env.Environment;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Implement service for Stripe
// */

//@Slf4j
//public class StripePaymentProcess extends PaymentBasedProcess {

//    public StripePaymentProcess(Environment env) {
//        this.envMap = env;
//        Stripe.apiKey = env.getProperty("stripe.secretkey");
//    }
//
//    /**
//     * auto charge do not throw exception
//     */
//    @Override
//    public CustomCreditCard getCardInfo(String cardToken, User user) {
//        try {
//            // Retrieve a token https://stripe.com/docs/api/tokens/retrieve
//            Token token = Token.retrieve(cardToken);
//
//            return new CustomCreditCard(token);
//        } catch (StripeException e) {
//            log.error("Get card info error: Card token: {}, customerId: {}, emailAddress: {}",
//                cardToken, user.getStripeCustomerId(), user.getEmail(), e);
//            showErrorStripe(e);
//        }
//        return null;
//    }
//
//    @Override
//    public void deleteCard(String cardToken, String customerId, String email) {
//        try {
//
//
//            Map<String, Object> retrieveParams = new HashMap<>();
//            List<String> expandList = new ArrayList<>();
//            expandList.add("sources");
//            retrieveParams.put("expand", expandList);
//            Customer customer = Customer.retrieve(customerId, retrieveParams, null);
//
//            // Retrieve a token https://stripe.com/docs/api/tokens/retrieve
//            Token token = Token.retrieve(cardToken);
//
//            // delete card https://stripe.com/docs/api/cards/delete
//            Card card = (Card) customer.getSources().retrieve(token.getCard().getId());
//            card.delete();
//
//        } catch (StripeException e) {
//            log.error("Delete card info error: Card token: {}, customerId: {}, emailAddress: {}",
//                cardToken, customerId, email, e);
//            e.printStackTrace();
//            showErrorStripe(e);
//        }
//    }
//
//    @Override
//    public Customer createCustomer(String email) {
//        try {
//            // https://stripe.com/docs/api/customers/create
//            Map<String, Object> params = new HashMap<>();
//            params.put("email", email);
//            return Customer.create(params);
//        } catch (StripeException e) {
//            log.error("Create customer error emailAddress: {}", email, e);
//            showErrorStripe(e);
//        }
//
//        return null;
//    }
//
//    @Override
//    public Customer getCustomer(User user) {
//        Customer customer = null;
//        try {
//            customer = Customer.retrieve(user.getStripeCustomerId());
//        } catch (StripeException e) {
//            log.error("Get customer error: customerId: {}, emailAddress: {}",
//                    user.getStripeCustomerId(), user.getEmail(), e);
//            showErrorStripe(e);
//        }
//        return customer;
//    }
//
//    @Override
//    public void deleteCustomer(User user) {
//        if (user.getStripeCustomerId() != null) {
//            try {
//                // https://stripe.com/docs/api/customers/delete
//                Customer customer = Customer.retrieve(user.getStripeCustomerId());
//
//                customer.delete();
//            } catch (StripeException e) {
//                log.error("Delete customer error: customerId: {}, emailAddress: {}",
//                        user.getStripeCustomerId(), user.getEmail(), e);
//            }
//        }
//    }
//
//    @Override
//    public void activeCardForCustomer(String cardToken, User user) {
//        Customer customer;
//        try {
//            // Retrieve a token https://stripe.com/docs/api/tokens/retrieve
//            Token token = Token.retrieve(cardToken);
//
//            // update customer https://stripe.com/docs/api/customers/update
//            customer = Customer.retrieve(user.getStripeCustomerId());
//
//            Card card = (Card) customer.getSources().retrieve(token.getCard().getId());
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("default_source", card.getId());
//
//            customer.update(params);
//        } catch (StripeException e) {
//            log.error("Active card error: Card token: {}, customerId: {}, emailAddress: {}",
//                cardToken, user.getStripeCustomerId(), user.getStripeCustomerId(), e);
//            showErrorStripe(e);
//        }
//    }
//
//    @Override
//    public Card createCard(String cardToken, String customerId, String emailAddress) {
//        Card card = null;
//        try {
////            // get customer
////            Customer customer = Customer.retrieve(customerId);
////
////            PaymentSourceCollectionCreateParams params = PaymentSourceCollectionCreateParams.builder()
////                .setSource(cardToken)
////                .build();
////
////            // create card by token https://stripe.com/docs/api/cards/create
////            card = (Card) customer.getSources().create(params);
////            System.out.println("createCard: " + card.getId());
//
//            Map<String, Object> retrieveParams =
//                    new HashMap<>();
//            List<String> expandList = new ArrayList<>();
//            expandList.add("sources");
//            retrieveParams.put("expand", expandList);
//            Customer customer =
//                    Customer.retrieve(
//                            customerId,
//                            retrieveParams,
//                            null
//                    );
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("source", cardToken);
//
//            card = (Card) customer.getSources().create(params);
//
//        } catch (StripeException e) {
//            log.error("Create card error: Card token: {}, customerId: {}, mailAddress: {}",
//                cardToken, customerId, emailAddress, e);
//
//            showErrorStripe(e);
//        }
//        return card;
//    }
//
//    @Override
//    public Charge payment(String customerId, long amount) {
//        Charge charge = null;
//        try {
//            ChargeCreateParams params =
//                    ChargeCreateParams.builder()
//                            .setAmount(amount * 100)
//                            .setCurrency("USD")
//                            .setDescription("Payment for $" + amount)
//                            .setCustomer(customerId)
//                            .build();
//
//            charge = Charge.create(params);
//        } catch (StripeException e){
//            log.error("Charge card error: Card token: {}", customerId);
//            showErrorStripe(e);
//        }
//        return charge;
//    }
//
//    private void showErrorStripe(StripeException e) {
//        if ("card_declined".equals(e.getCode())) {
//            throw new ApplicationException(RestAPIStatus.ERR_STRIPE,
//                e.getStripeError().getDeclineCode() + " - " + e.getStripeError().getMessage());
//        }
//        throw new ApplicationException(RestAPIStatus.ERR_STRIPE,
//            e.getStripeError().getCode() + " - " + e.getStripeError().getMessage());
//    }
//
//    @Override
//    public Refund refund(String transactionId) {
//        Refund refund = null;
//        try {
//            RefundCreateParams params = RefundCreateParams.builder().setCharge(transactionId).build();
//
//            refund = Refund.create(params);
//        } catch (StripeException e){
//            log.error("Refund Stripe error: TransactionId: {}", transactionId);
//            log.error(e.getMessage());
//        }
//        return refund;
//    }
//}
