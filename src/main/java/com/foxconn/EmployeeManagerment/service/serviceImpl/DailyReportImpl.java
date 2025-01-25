package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.entity.*;
import com.foxconn.EmployeeManagerment.repository.*;
import com.foxconn.EmployeeManagerment.service.DailyReportService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class DailyReportImpl implements DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final ProjectRepository projectRepository;
    private final ImplementRepository implementRepository;
    private final CateRepository cateRepository;
    private final UserRepository userRepository;

    public DailyReportImpl(DailyReportRepository dailyReportRepository, ProjectRepository projectRepository, ImplementRepository implementRepository, CateRepository cateRepository, UserRepository userRepository) {
        this.dailyReportRepository = dailyReportRepository;
        this.projectRepository = projectRepository;
        this.implementRepository = implementRepository;
        this.cateRepository = cateRepository;
        this.userRepository = userRepository;
    }


    @Override
    public boolean createDailyReport(DailyReportDTO dailyReport, String userId) {

        Project project = projectRepository.findByProjectId(dailyReport.getProjectId());
        Category category = cateRepository.findByCategory(dailyReport.getCategoryId());
        Users users =userRepository.findByUid(userId);
        Assert.notNull(project,"PROJECT NOT FOUND");
        Assert.notNull(category,"CATEGORY NOT FOUND");

        DailyReport report = DailyReport.builder()
          .create_at(LocalDateTime.now())
          .endDate(dailyReport.getEndDate())
          .startDate(dailyReport.getStartDate())
          .progress(dailyReport.getProgress())
          .address(dailyReport.getAddress())
          .reporterId(userId)
          .category(category)
          .contractor(dailyReport.getContractor())
          .numberWorker(dailyReport.getNumberWorker())
          .quantity(dailyReport.getQuantity())
          .quantityCompleted(dailyReport.getQuantityCompleted())
          .quantityRemain(dailyReport.getQuantityRemain())
          .requester(dailyReport.getRequester())
          .project(project)
          .build();

        Implement implement = Implement.builder()
                .createAt(LocalDateTime.now())
                .implement(dailyReport.getImplement())
                .users(users)
                .projects(project)
                .build();

         report = dailyReportRepository.save(report);

         if(Objects.nonNull(report.getReportId())) {
             implement=  implementRepository.save(implement);
             return true;
         }else {
             return false;
         }
//return true;

    }

//    @Override
//    public DailyReport updateDailyReport(Long id, DailyReportDTO dailyReport) {
//        DailyReport existingDailyReport = dailyReportRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("DailyReport not found"));
//        Category category = cateRepository.FindByCategoryId(dailyReport.getCategoryId());
//        DailyReport updatedDailyReport = DailyReport.builder()
//                .reportId(existingDailyReport.getReportId())
//                .create_at(dailyReport.getCreate_at())
//                .projectId(dailyReport.getProjectId())
//                .address(dailyReport.getAddress())
//                .quantity(dailyReport.getQuantity())
//                .categoryId(category)
//                .quantityCompleted(dailyReport.getQuantityCompleted())
//                .quantityRemain(dailyReport.getQuantityRemain())
//                .contractor(dailyReport.getContractor())
//                .numberWorker(dailyReport.getNumberWorker())
//                .requester(dailyReport.getRequester())
//                .build();
//
//        return dailyReportRepository.save(updatedDailyReport);
//    }

    @Override
    public void deleteDailyReport(Long id) {
        dailyReportRepository.deleteById(id);
    }

    @Override
    public DailyReport getDailyReportById(Long id) {
        return dailyReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DailyReport not found"));
    }

    @Override
    public List<DailyReport> getAllDailyReports() {
        return dailyReportRepository.findAllDailyReport();
    }

//    @Override
//    public List<DailyReport> getDailyReportsByProjectId(Long projectId) {
//        return dailyReportRepository.findByProjectId(projectId);
//    }

    @Override
    public List<DailyReport> getDailyReportsByUserImplement(String userDetails) {
        return dailyReportRepository.findByUerDetail(userDetails);
    }

}
