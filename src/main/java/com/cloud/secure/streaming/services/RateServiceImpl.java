package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Rate;
import com.cloud.secure.streaming.repositories.RateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class RateServiceImpl implements RateService {
    final RateRepository rateRepository;

    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Override
    public Rate save(Rate rate) {
        return rateRepository.save(rate);
    }

    @Override
    public void delete(Rate rate) {
        rateRepository.delete(rate);
    }

    @Override
    public Rate getById(String id) {
        return rateRepository.findById(id).orElse(null);
    }

    @Override
    public List<Rate> getAllByOrderByCreatedDateDesc() {
        return rateRepository.findAllByOrderByCreatedDateDesc();
    }
}
