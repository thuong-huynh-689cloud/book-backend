package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseAdminFeedback;
import com.cloud.secure.streaming.repositories.CourseAdminFeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseAdminFeedbackServiceImpl implements CourseAdminFeedbackService{

    final CourseAdminFeedbackRepository courseAdminFeedbackRepository;

    public CourseAdminFeedbackServiceImpl(CourseAdminFeedbackRepository courseAdminFeedbackRepository) {
        this.courseAdminFeedbackRepository = courseAdminFeedbackRepository;
    }

    @Override
    public CourseAdminFeedback save(CourseAdminFeedback courseAdminFeedback) {
        return courseAdminFeedbackRepository.save(courseAdminFeedback);
    }

    @Override
    public void delete(CourseAdminFeedback courseAdminFeedback) {
        courseAdminFeedbackRepository.delete(courseAdminFeedback);
    }

    @Override
    public CourseAdminFeedback getById(String id) {
        return courseAdminFeedbackRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAllByCourseId(String courseId) {
        courseAdminFeedbackRepository.deleteCourseAdminFeedbackById(courseId);
    }
}
