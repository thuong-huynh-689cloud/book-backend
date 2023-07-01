package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl  implements PaymentService{
    final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getId(String id) {
        return paymentRepository.findById(id).orElse(null);
    }
}
