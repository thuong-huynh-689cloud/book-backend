package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Rate;

import java.util.List;


/**
 * @author 689Cloud
 */
public interface RateService {

    Rate save(Rate rate);

    void delete(Rate rate);

    Rate getById(String id);

    List<Rate> getAllByOrderByCreatedDateDesc();
}
