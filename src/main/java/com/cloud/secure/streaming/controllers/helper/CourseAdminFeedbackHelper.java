package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.FeedbackType;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.UpdateCourseStatusRequest;
import com.cloud.secure.streaming.entities.CourseAdminFeedback;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class CourseAdminFeedbackHelper {

    /**
     *  Create Course Admin Feedback
     *
     * @param courseId
     * @param feedbackType
     * @param feedback
     * @return
     */
    public CourseAdminFeedback createCourseAdminFeedback(String courseId, FeedbackType feedbackType,String feedback){
        CourseAdminFeedback courseAdminFeedback = new CourseAdminFeedback();
        //add id to data
        courseAdminFeedback.setId(UniqueID.getUUID());
        // add courseId to data
        courseAdminFeedback.setCourseId(courseId);

        courseAdminFeedback.setFeedbackType(feedbackType);
        // add title to data
        courseAdminFeedback.setFeedback(feedback);
        // add createDate to date
        courseAdminFeedback.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return courseAdminFeedback;
    }
}
