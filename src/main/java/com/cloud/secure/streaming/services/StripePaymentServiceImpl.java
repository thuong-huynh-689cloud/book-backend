package com.cloud.secure.streaming.services;


import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.gateway.PaymentBasedProcess;
import com.cloud.secure.streaming.gateway.PaymentProcessFactory;
import com.cloud.secure.streaming.gateway.model.CustomCreditCard;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
/**
 * PaymentServiceImpl
 */
@Slf4j
@Service
public class StripePaymentServiceImpl implements StripePaymentService {

    final Environment env;

    public StripePaymentServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public CustomCreditCard getCardInfoByToken(String cardToken, User user) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        return paymentProcess.getCardInfo(cardToken, user);
    }

    @Override
    public void deleteCard(String cardToken, String customerId, String email) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        paymentProcess.deleteCard(cardToken, customerId, email);
    }

    @Override
    public Customer createCustomer(String email) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        return paymentProcess.createCustomer(email);
    }

    @Override
    public Customer getCustomer(User user) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        return paymentProcess.getCustomer(user);
    }

    @Override
    public void deleteCustomer(User user) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        paymentProcess.deleteCustomer(user);
    }

    @Override
    public void activeCardForCustomer(String cardToken, User user) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        paymentProcess.activeCardForCustomer(cardToken, user);
    }

    @Override
    public Card createCard(String cardToken, String customerId, String emailAddress) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        return paymentProcess.createCard(cardToken, customerId, emailAddress);
    }

    @Override
    public Charge payment(String customerId, long amount) {
        PaymentBasedProcess paymentProcess = PaymentProcessFactory.createPaymentProcess(env);

        return paymentProcess.payment(customerId, amount);
    }

}
