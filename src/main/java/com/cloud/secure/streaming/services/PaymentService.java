package com.cloud.secure.streaming.services;


import com.cloud.secure.streaming.entities.Payment;

public interface PaymentService {

    Payment savePayment(Payment payment);

    Payment getId( String id);
}
