package com.cloud.secure.streaming.gateway;

import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.gateway.model.CustomCreditCard;
import com.stripe.model.*;
import org.springframework.core.env.Environment;

/**
 * Abstract PaymentBasedProcess use to manage from Factory level
 * Other service implement should extend from this based class
 */
public abstract class PaymentBasedProcess {

    public Environment envMap;

    public void setEnv(Environment env) {
        this.envMap = env;
    }

    public abstract CustomCreditCard getCardInfo(String cardToken, User user);

    public abstract void deleteCard(String cardToken, String customerId, String email);

    public abstract Customer createCustomer(String email);

    public abstract Customer getCustomer(User user);

    public abstract void deleteCustomer(User user);

    public abstract void activeCardForCustomer(String cardToken, User user);

    public abstract Card createCard(String cardToken, String customerId, String emailAddress);

    public abstract Charge payment(String customerId, long amount);

    public abstract Refund refund(String transactionId);




}
