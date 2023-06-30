package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.ExchangeRate;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface ExchangeRateService {

    ExchangeRate save(ExchangeRate exchangeRate);

    void delete(ExchangeRate exchangeRate);

    ExchangeRate getById(String id);

    List<ExchangeRate> getAllByOrderByCreatedDateDesc();
}
