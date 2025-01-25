package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.entity.WeeklyReport;
import com.foxconn.EmployeeManagerment.service.WeeklyReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/weekly-report")
public class WeeklyReportController {
    private final WeeklyReportService weeklyReportService;

    public WeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @PostMapping("/add")
    public ResponseEntity<WeeklyReport> createWeeklyReport(@RequestBody WeeklyReport weeklyReport) {
        WeeklyReport createdWeeklyReport = weeklyReportService.createWeeklyReport(weeklyReport);
        return ResponseEntity.ok(createdWeeklyReport);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WeeklyReport> updateWeeklyReport(@PathVariable Long id, @RequestBody WeeklyReport weeklyReport) {
        WeeklyReport updatedWeeklyReport = weeklyReportService.updateWeeklyReport(id, weeklyReport);
        return ResponseEntity.ok(updatedWeeklyReport);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWeeklyReport(@PathVariable Long id) {
        weeklyReportService.deleteWeeklyReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<WeeklyReport> getWeeklyReportById(@PathVariable Long id) {
        WeeklyReport weeklyReport = weeklyReportService.getWeeklyReportById(id);
        return ResponseEntity.ok(weeklyReport);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<WeeklyReport>> getAllWeeklyReports() {
        List<WeeklyReport> weeklyReportsList = weeklyReportService.getAllWeeklyReports();
        return ResponseEntity.ok(weeklyReportsList);
    }
}
