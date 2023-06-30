package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseReport;

/**
 * @author 689Cloud
 */
public interface CourseReportService {

    CourseReport save(CourseReport courseReport);

    void delete(CourseReport courseReport);

    CourseReport getById(String id);
}
