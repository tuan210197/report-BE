package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.entity.DailyReport;

import java.util.List;


public interface DailyReportService {
    boolean createDailyReport(DailyReportDTO dailyReport, String userId);
//    DailyReport updateDailyReport(Long id, DailyReport dailyReport);
    void deleteDailyReport(Long id);
    DailyReport getDailyReportById(Long id);
    List<DailyReport> getAllDailyReports();
//    List<DailyReport> getDailyReportsByProjectId(Long projectId);
    List<DailyReport> getDailyReportsByUserImplement(String user);

    DailyReport getByProjectId(String userId, Long projectId);

    List<DailyReport> search(String keyword);
}
