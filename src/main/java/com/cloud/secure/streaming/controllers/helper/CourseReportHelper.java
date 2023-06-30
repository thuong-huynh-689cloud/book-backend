package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateCourseReportRequest;
import com.cloud.secure.streaming.entities.CourseReport;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class CourseReportHelper {

    /**
     *  Create Course Report
     *
     * @param createCourseReportRequest
     * @param userId
     * @return
     */
    public CourseReport createCourseReport(CreateCourseReportRequest createCourseReportRequest, String userId) {
        CourseReport courseReport = new CourseReport();
        courseReport.setId(UniqueID.getUUID());
        courseReport.setCourseId(createCourseReportRequest.getCourseId());
        courseReport.setUserId(userId);
        courseReport.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return courseReport;
    }
}
