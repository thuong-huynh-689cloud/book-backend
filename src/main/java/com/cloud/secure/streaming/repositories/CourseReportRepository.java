package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.CourseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface CourseReportRepository extends JpaRepository<CourseReport, String> {
}
