package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateCourseReviewRequest;
import com.cloud.secure.streaming.entities.CourseReview;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class CourseReviewHelper {

    public CourseReview createReviewCourse(CreateCourseReviewRequest createCourseReviewRequest,String userId){

        CourseReview courseReview = new CourseReview();
        courseReview.setId(UniqueID.getUUID());
        courseReview.setRating(createCourseReviewRequest.getRating());
        courseReview.setDescription(createCourseReviewRequest.getDescription());
        courseReview.setUserId(userId);
        courseReview.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return courseReview;
    }
}
