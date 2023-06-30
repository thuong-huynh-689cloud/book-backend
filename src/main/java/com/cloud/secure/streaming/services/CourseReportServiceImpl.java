package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.CourseReport;
import com.cloud.secure.streaming.repositories.CourseReportRepository;
import org.springframework.stereotype.Service;

/**
 * @author 689Cloud
 */
@Service
public class CourseReportServiceImpl implements CourseReportService {

    final CourseReportRepository courseReportRepository;

    public CourseReportServiceImpl(CourseReportRepository courseReportRepository) {
        this.courseReportRepository = courseReportRepository;
    }

    @Override
    public CourseReport save(CourseReport courseReport) {
        return courseReportRepository.save(courseReport);
    }

    @Override
    public void delete(CourseReport courseReport) {
        courseReportRepository.delete(courseReport);
    }

    @Override
    public CourseReport getById(String id) {
        return courseReportRepository.findById(id).orElse(null);
    }
}
