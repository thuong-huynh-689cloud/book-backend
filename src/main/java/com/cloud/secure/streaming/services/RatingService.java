package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Rating;

/**
 * @author 689Cloud
 */
public interface RatingService {

    Rating save(Rating rating);

    void delete(Rating rating);

    Rating getById(String id);
}
