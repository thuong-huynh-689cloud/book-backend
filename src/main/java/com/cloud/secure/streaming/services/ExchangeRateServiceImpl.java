package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.ExchangeRate;
import com.cloud.secure.streaming.repositories.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    @Override
    public void delete(ExchangeRate exchangeRate) {
        exchangeRateRepository.delete(exchangeRate);
    }

    @Override
    public ExchangeRate getById(String id) {
        return exchangeRateRepository.findById(id).orElse(null);
    }

    @Override
    public List<ExchangeRate> getAllByOrderByCreatedDateDesc() {
        return exchangeRateRepository.findAllByOrderByCreatedDateDesc();
    }
}
