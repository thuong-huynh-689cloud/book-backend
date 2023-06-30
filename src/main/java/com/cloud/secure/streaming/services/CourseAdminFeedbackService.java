package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseAdminFeedback;

public interface CourseAdminFeedbackService {

    CourseAdminFeedback save(CourseAdminFeedback courseAdminFeedback);

    void delete(CourseAdminFeedback courseAdminFeedback);

    CourseAdminFeedback getById(String id);

    void deleteAllByCourseId(String courseId);
}
