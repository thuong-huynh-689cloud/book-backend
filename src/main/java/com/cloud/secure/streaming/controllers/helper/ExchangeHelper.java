package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.controllers.model.request.UpdateExchangeRateRequest;
import com.cloud.secure.streaming.entities.ExchangeRate;
import org.springframework.stereotype.Component;


/**
 * @author 689Cloud
 */
@Component
public class ExchangeHelper {

    /**
     * Update ExchangeRate APi
     *
     * @param exchangeRate
     * @param updateExchangeRateRequest
     * @return
     */
    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate, UpdateExchangeRateRequest updateExchangeRateRequest) {
        //check name
        if (updateExchangeRateRequest.getName() != null && !updateExchangeRateRequest.getName().trim().isEmpty() &&
                !updateExchangeRateRequest.getName().trim().equals(exchangeRate.getName())) {
            exchangeRate.setName(updateExchangeRateRequest.getName().trim());
        }
        //check point
        if (updateExchangeRateRequest.getPoint() != null &&
                !updateExchangeRateRequest.getPoint().equals(exchangeRate.getPoint())) {
            exchangeRate.setPoint(updateExchangeRateRequest.getPoint());
        }
        //check name
        if (updateExchangeRateRequest.getExchangedPrice() != null &&
                !updateExchangeRateRequest.getExchangedPrice().equals(exchangeRate.getExchangedPrice())) {
            exchangeRate.setExchangedPrice(updateExchangeRateRequest.getExchangedPrice());
        }
        return exchangeRate;
    }
}
