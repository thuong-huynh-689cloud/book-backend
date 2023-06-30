package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseReview;
import com.cloud.secure.streaming.repositories.CourseReviewRepository;
import org.springframework.stereotype.Service;

/**
 * @author 689Cloud
 */
@Service
public class CourseReviewServiceImpl implements CourseReviewService {

    final CourseReviewRepository courseReviewRepository;

    public CourseReviewServiceImpl(CourseReviewRepository courseReviewRepository) {
        this.courseReviewRepository = courseReviewRepository;
    }

    @Override
    public CourseReview save(CourseReview courseReview) {
        return courseReviewRepository.save(courseReview);
    }

    @Override
    public void delete(CourseReview courseReview) {
        courseReviewRepository.delete(courseReview);
    }

    @Override
    public CourseReview getById(String id) {
        return courseReviewRepository.findById(id).orElse(null);
    }
}
