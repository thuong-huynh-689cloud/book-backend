package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.controllers.model.request.UpdateRateRequest;
import com.cloud.secure.streaming.entities.Rate;
import org.springframework.stereotype.Component;

@Component
public class RateHelper {

    /**
     *  Update Rate API
     *
     * @param rate
     * @param updateRateRequest
     * @return
     */
    public Rate updateRate(Rate rate, UpdateRateRequest updateRateRequest) {
        //check name
        if (updateRateRequest.getName() != null && !updateRateRequest.getName().trim().isEmpty() &&
                !updateRateRequest.getName().trim().equals(rate.getName())) {
            rate.setName(updateRateRequest.getName().trim());
        }
        //check point
        if (updateRateRequest.getCommissionPercentage() != null &&
                !updateRateRequest.getCommissionPercentage().equals(updateRateRequest.getCommissionPercentage())) {
            rate.setCommissionPercentage(updateRateRequest.getCommissionPercentage());
        }
        return rate;
    }
}
