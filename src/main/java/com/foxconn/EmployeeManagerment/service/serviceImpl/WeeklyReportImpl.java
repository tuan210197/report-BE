package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.entity.WeeklyReport;
import com.foxconn.EmployeeManagerment.repository.WeeklyReportRepository;
import com.foxconn.EmployeeManagerment.service.WeeklyReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeeklyReportImpl implements WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    public WeeklyReportImpl(WeeklyReportRepository weeklyReportRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
    }


    @Override
    public WeeklyReport createWeeklyReport(WeeklyReport weeklyReport) {
        return weeklyReportRepository.save(weeklyReport);
    }

    @Override
    public WeeklyReport updateWeeklyReport(Long id, WeeklyReport weeklyReport) {
        WeeklyReport existingWeeklyReport = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklyReport not found"));

        WeeklyReport updatedWeeklyReport = WeeklyReport.builder()
                .title(weeklyReport.getTitle())
                .create_at(weeklyReport.getCreate_at())
                .description(weeklyReport.getDescription())
                .projectId(weeklyReport.getProjectId())
                .address(weeklyReport.getAddress())
                .quantity(weeklyReport.getQuantity())
                .category(weeklyReport.getCategory())
                .quantityCompleted(weeklyReport.getQuantityCompleted())
                .quantityRemain(weeklyReport.getQuantityRemain())
                .contractor(weeklyReport.getContractor())
                .numberWorker(weeklyReport.getNumberWorker())
                .requester(weeklyReport.getRequester())
                .build();

        return weeklyReportRepository.save(updatedWeeklyReport);
    }

    @Override
    public void deleteWeeklyReport(Long id) {
        weeklyReportRepository.deleteById(id);
    }

    @Override
    public WeeklyReport getWeeklyReportById(Long id) {
        return weeklyReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklyReport not found"));
    }

    @Override
    public List<WeeklyReport> getAllWeeklyReports() {
        return weeklyReportRepository.findAll();
    }
}
