package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.entity.DailyReport;
import com.foxconn.EmployeeManagerment.service.DailyReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/daily-report")
public class DailyReportController extends  BaseController{

    private final DailyReportService dailyReportService;

    public DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createDailyReport(@RequestBody DailyReportDTO dailyReport) {
        String userId = this.getCurrentUser().getUid().trim();

        if(dailyReportService.createDailyReport(dailyReport,  userId)){
            return toSuccessResult(null, "TẠO BÁO CÁO THÀNH CÔNG");
        }else {
            return toExceptionResult("TẠO BÁO CÁO LỖI", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }

    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<DailyReport> updateDailyReport(@PathVariable Long id, @RequestBody DailyReport dailyReport) {
//        DailyReport updatedDailyReport = dailyReportService.updateDailyReport(id, dailyReport);
//        return ResponseEntity.ok(updatedDailyReport);
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDailyReport(@PathVariable Long id) {
        dailyReportService.deleteDailyReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DailyReport> getDailyReportById(@PathVariable Long id) {
        DailyReport dailyReport = dailyReportService.getDailyReportById(id);
        return ResponseEntity.ok(dailyReport);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<DailyReport>> getAllDailyReports() {
        List<DailyReport> dailyReportsList = dailyReportService.getAllDailyReports();
        return ResponseEntity.ok(dailyReportsList);
    }

//    @GetMapping("/get-by-project/{projectId}")
//    public ResponseEntity<List<DailyReport>> getDailyReportsByProjectId(@PathVariable Long projectId) {
//        List<DailyReport> dailyReportsList = dailyReportService.getDailyReportsByProjectId(projectId);
//        return ResponseEntity.ok(dailyReportsList);
//    }

//    @GetMapping("/get-by-user/{userImplement}")
//    public ResponseEntity<List<DailyReport>> getDailyReportsByUserImplement(@PathVariable String userImplement) {
//        List<DailyReport> dailyReportsList = dailyReportService.getDailyReportsByUserImplement(userImplement);
//        return ResponseEntity.ok(dailyReportsList);
//    }

    @GetMapping("/find-by-uuid")
    public ResponseEntity<?> findDailyReportByUuid(HttpServletRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))){
            List<DailyReport> dailyReports = dailyReportService.getAllDailyReports();
            return ResponseEntity.ok(dailyReports);
        }else{
            String uid = this.getCurrentUser().getUid().trim();
            List<DailyReport> dailyReports = dailyReportService.getDailyReportsByUserImplement(uid);
            return ResponseEntity.ok(dailyReports);
        }
    }
}
