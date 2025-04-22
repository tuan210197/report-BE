package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.entity.DailyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DailyReportService {
    boolean createDailyReport(DailyReportDTO dailyReport, String userId);
//    DailyReport updateDailyReport(Long id, DailyReport dailyReport);
    void deleteDailyReport(Long id);
    DailyReport getDailyReportById(Long id);
    Page<DailyReport> getAllDailyReports(Pageable pageable);
//    List<DailyReport> getDailyReportsByProjectId(Long projectId);
Page<DailyReport> getDailyReportsByUserImplement(String user, Pageable pageable);

    DailyReport getByProjectId(String userId, Long projectId);

    List<DailyReport> search(String keyword);
}
