package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Report;
import com.cloud.secure.streaming.repositories.ReportRepository;
import org.springframework.stereotype.Service;


@Service
public class ReportServiceImpl implements ReportService {

    final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public void delete(Report report) {
        reportRepository.delete(report);
    }

    @Override
    public Report getById(String id) {
        return reportRepository.findById(id).orElse(null);
    }
}
