package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.dto.request.DailyReportSearchDTO;
import com.foxconn.EmployeeManagerment.dto.request.DateDTO;
import com.foxconn.EmployeeManagerment.entity.DailyReport;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.projection.DailyReportProjection;
import com.foxconn.EmployeeManagerment.repository.DailyReportRepository;
import com.foxconn.EmployeeManagerment.repository.ProjectRepository;
import com.foxconn.EmployeeManagerment.service.DailyReportService;
import com.foxconn.EmployeeManagerment.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/daily-report")
public class DailyReportController extends BaseController {

    private final DailyReportService dailyReportService;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final DailyReportRepository dailyReportRepository;

    public DailyReportController(DailyReportService dailyReportService, ProjectService projectService, ProjectRepository projectRepository, DailyReportRepository dailyReportRepository) {
        this.dailyReportService = dailyReportService;
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.dailyReportRepository = dailyReportRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createDailyReport(@RequestBody DailyReportDTO dailyReport) {

        String userId = this.getCurrentUser().getUid().trim();

        if (dailyReportService.createDailyReport(dailyReport, userId)) {
            return toSuccessResult(null, "TẠO BÁO CÁO THÀNH CÔNG");
        } else {
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
        if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))) {
            List<DailyReport> dailyReports = dailyReportService.getAllDailyReports();
            return ResponseEntity.ok(dailyReports);
        } else {
            String uid = this.getCurrentUser().getUid().trim();
            List<DailyReport> dailyReports = dailyReportService.getDailyReportsByUserImplement(uid);
            return ResponseEntity.ok(dailyReports);
        }
    }

    @PostMapping("/get-by-project")
    public ResponseEntity<?> getDailyReportByProject(HttpServletRequest request, @RequestBody DailyReportDTO dailyReportDTO) {
        String userId = this.getCurrentUser().getUid().trim();
        DailyReport dailyReports = dailyReportService.getByProjectId(userId, dailyReportDTO.getProjectId());
        Project project = projectRepository.findByProjectId(dailyReportDTO.getProjectId());
        if (dailyReports != null && project != null) {
            return toSuccessResult(dailyReports, "SUCCESS");
        }
        if (dailyReports == null && project != null) {
            return toSuccessResult(project, "SUCCESS");
        } else {
            return toExceptionResult("PROJECT NOT FOUND", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportDailyReport(HttpServletRequest request, @RequestBody DateDTO dto) {
        return ResponseEntity.ok(dailyReportRepository.export(dto.getDate()));

    }

    @GetMapping("/get-max-export-date")
    public ResponseEntity<?> exportDailyReport2(HttpServletRequest request) {
        try {
            String date = dailyReportRepository.getMaxDate();
            if (date != null) {
                return toSuccessResult(date, "SUCCESS");
            } else {
                return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PostMapping("/search-report")
    public ResponseEntity<?> searchDailyReport(@RequestBody DailyReportSearchDTO dto) {
        List<DailyReport> list = dailyReportService.search(dto.getKeyword());
        if (list != null) {
            return toSuccessResult(list, "SUCCESS");
        }else{
            return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }
}
