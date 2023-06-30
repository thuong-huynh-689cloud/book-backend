package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.ExchangeRate;
import com.cloud.secure.streaming.entities.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface ExchangeRateRepository  extends JpaRepository<ExchangeRate, String> {

    List<ExchangeRate> findAllByOrderByCreatedDateDesc();
}
