package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.WeeklyReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeeklyReportService {

    WeeklyReport createWeeklyReport(WeeklyReport weeklyReport);
    WeeklyReport updateWeeklyReport(Long id, WeeklyReport weeklyReport);
    void deleteWeeklyReport(Long id);
    WeeklyReport getWeeklyReportById(Long id);
    List<WeeklyReport> getAllWeeklyReports();
}
