package com.cloud.secure.streaming.controllers.helper;


import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentHelper {
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) {
        Payment payment = new Payment();
        payment.setId(UniqueID.getUUID());
        payment.setCardId(createPaymentRequest.getCardId());
        payment.setOderId(createPaymentRequest.getOderId());

        return payment;
    }
}
