package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.dto.request.DailyReportDTO;
import com.foxconn.EmployeeManagerment.entity.*;
import com.foxconn.EmployeeManagerment.repository.*;
import com.foxconn.EmployeeManagerment.service.DailyReportService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
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
    @Transactional
    public boolean createDailyReport(DailyReportDTO dailyReport, String userId) {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = today.atTime(23, 59, 59); // 23:59:59
        Project project = projectRepository.findByProjectId(dailyReport.getProjectId());
        Category category = cateRepository.findByCategory(dailyReport.getCategoryId());
        Users users = userRepository.findByUid(userId);
        Assert.notNull(project, "PROJECT NOT FOUND");
        Assert.notNull(category, "CATEGORY NOT FOUND");


        List<DailyReport> check = dailyReportRepository.checkDailyReport(dailyReport.getProjectId(), userId, startOfDay, endOfDay);
        if (check.isEmpty()) {
            log.info("empty daily report");
            DailyReport report = DailyReport.builder()
                    .create_at(LocalDateTime.now())
                    .endDate(dailyReport.getEndDate())
                    .startDate(dailyReport.getStartDate())
                    .progress(dailyReport.getProgress())
                    .address(dailyReport.getAddress())
//                    .reporterId(userId)
                    .user(users)
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

            project.setProgress(dailyReport.getProgress().intValue());
            projectRepository.save(project);
            if (Objects.nonNull(report.getReportId())) {
                implement = implementRepository.save(implement);
                return true;
            } else {
                return false;
            }
        } else if (check.size() == 1) {
            log.info("single daily report");
            DailyReport report = check.get(0);
            List<Implement> implement = implementRepository.findImplement(dailyReport.getProjectId(), userId, startOfDay, endOfDay);
            Implement firstImplement = implement.get(0);

            report.setCreate_at(LocalDateTime.now());
            report.setEndDate(dailyReport.getEndDate());
            report.setStartDate(dailyReport.getStartDate());
//            report.setUser(users);
//            report.setCategory(category);
            report.setContractor(dailyReport.getContractor());
            report.setNumberWorker(dailyReport.getNumberWorker());
            report.setQuantity(dailyReport.getQuantity());
            report.setQuantityCompleted(dailyReport.getQuantityCompleted());
            report.setQuantityRemain(dailyReport.getQuantityRemain());
            report.setRequester(dailyReport.getRequester());
//            report.setProject(project);
            report.setProgress(dailyReport.getProgress());
            report.setAddress(report.getAddress());

            dailyReportRepository.save(report);
            firstImplement.setCreateAt(LocalDateTime.now());
            firstImplement.setImplement(dailyReport.getImplement());
            implementRepository.save(firstImplement);
            return true;
        } else {
            log.info("multiple daily report");
            return false;
        }
    }

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

    @Override
    public DailyReport getByProjectId(String userId, Long projectId) {

        Pageable pageable = PageRequest.of(0, 1);
        return dailyReportRepository.findByProjectId(userId, projectId, pageable).stream().findFirst().orElse(null);

    }

}
