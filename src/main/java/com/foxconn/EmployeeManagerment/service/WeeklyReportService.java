package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.WeeklyReport;

import java.util.List;

public interface WeeklyReportService {

    WeeklyReport createWeeklyReport(WeeklyReport weeklyReport);
    WeeklyReport updateWeeklyReport(Long id, WeeklyReport weeklyReport);
    void deleteWeeklyReport(Long id);
    WeeklyReport getWeeklyReportById(Long id);
    List<WeeklyReport> getAllWeeklyReports();
}
