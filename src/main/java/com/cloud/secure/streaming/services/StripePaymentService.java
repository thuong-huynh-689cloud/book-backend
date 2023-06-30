package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.gateway.model.CustomCreditCard;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

import javax.validation.constraints.NotNull;

public interface StripePaymentService {


    // get card info by token
    CustomCreditCard getCardInfoByToken(String cardToken, User user);

    // delete card
    void deleteCard(String cardToken, String customerId, String email);

    // create customer
    Customer createCustomer(String email);

    // get customer
    Customer getCustomer(User user);

    // delete customer
    void deleteCustomer(User user);

    // active card for customer
    void activeCardForCustomer(String cardToken, User user);

    // create card
    Card createCard(@NotNull String cardToken, @NotNull String customerId, String emailAddress);
    // payment
    Charge payment(String customerId, long amount);

}
