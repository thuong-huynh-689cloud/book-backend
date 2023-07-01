package com.cloud.secure.streaming.gateway;

import org.springframework.core.env.Environment;

/**
 * PaymentProcessFactory create Payment process service base on each type
 */
public class PaymentProcessFactory {

    public static PaymentBasedProcess createPaymentProcess(Environment envMap) {

        String GATEWAY_TYPE = envMap.getProperty("payment.gateway.active");
        GatewayType gatewayType = GatewayType.valueOf(GATEWAY_TYPE);

        if (gatewayType == GatewayType.STRIPE) {
            return new StripePaymentProcess(envMap);
        }
        throw new IllegalArgumentException("Unknown GatewayType");
    }

}
