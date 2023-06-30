package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Report;

/**
 * @author 689Cloud
 */
public interface ReportService {

    Report save(Report report);

    void delete(Report report);

    Report getById(String id);
}
