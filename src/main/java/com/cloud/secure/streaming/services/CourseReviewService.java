package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseReview;
/**
 * @author 689Cloud
 */
public interface CourseReviewService {

    CourseReview save(CourseReview courseReview);

    void delete(CourseReview courseReview);

    CourseReview getById(String id);
}
