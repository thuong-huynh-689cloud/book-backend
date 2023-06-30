package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Rating;
import com.cloud.secure.streaming.repositories.RatingRepository;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {
    final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public void delete(Rating rating) {
        ratingRepository.delete(rating);
    }

    @Override
    public Rating getById(String id) {
        return ratingRepository.findById(id).orElse(null);
    }
}
