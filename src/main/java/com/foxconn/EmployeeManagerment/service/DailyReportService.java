package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.dto.request.UserDTO;
import com.foxconn.EmployeeManagerment.entity.DailyReport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DailyReportService {
    boolean createDailyReport(DailyReportDTO dailyReport, String userId);
//    DailyReport updateDailyReport(Long id, DailyReport dailyReport);
    void deleteDailyReport(Long id);
    DailyReport getDailyReportById(Long id);
    List<DailyReport> getAllDailyReports();
//    List<DailyReport> getDailyReportsByProjectId(Long projectId);
    List<DailyReport> getDailyReportsByUserImplement(String user);

}
