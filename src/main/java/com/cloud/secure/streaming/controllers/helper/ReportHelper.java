package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateRatingRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateReportRequest;
import com.cloud.secure.streaming.entities.Rating;
import com.cloud.secure.streaming.entities.Report;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ReportHelper {

    public Report createReport(CreateReportRequest createReportRequest){
        Report report = new Report();
        report.setId(UniqueID.getUUID());
        report.setCourseId(createReportRequest.getCourseId());
        report.setDescription(createReportRequest.getDescription());
        report.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return report;
    }
}
