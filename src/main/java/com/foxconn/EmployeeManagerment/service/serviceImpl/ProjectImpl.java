package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.controller.BaseController;
import com.foxconn.EmployeeManagerment.dto.request.FromToDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectUpdateDTO;
import com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectDTO;
import com.foxconn.EmployeeManagerment.dto.response.ChartDto;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2;
import com.foxconn.EmployeeManagerment.entity.*;
import com.foxconn.EmployeeManagerment.projection.ProjectProjection;
import com.foxconn.EmployeeManagerment.repository.*;
import com.foxconn.EmployeeManagerment.service.MailSenderService;
import com.foxconn.EmployeeManagerment.service.ProjectService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProjectImpl extends BaseController implements ProjectService {
    private final StatusUpdateHisRepository statusUpdateHisRepository;
    private final StatusRepository statusRepository;
    private final CategoryRepository categoryRepository;


    private final ProjectRepository projectRepository;
    private final CateRepository cateRepository;
    private final UserRepository userRepository;

    public ProjectImpl(ProjectRepository projectRepository,  CateRepository cateRepository, UserRepository userRepository,
                       CategoryRepository categoryRepository,
                       StatusRepository statusRepository,
                       StatusUpdateHisRepository statusUpdateHisRepository) {
        this.projectRepository = projectRepository;
        this.cateRepository = cateRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.statusRepository = statusRepository;
        this.statusUpdateHisRepository = statusUpdateHisRepository;
    }


    @Override
    public Project getProjectByUser(String userId) {
        return null;
    }

    @Override
    public Project getAllproject() {
        return null;
    }

    @Override
    public Long createProject(ProjectDTO projectDto, String userId) throws Exception {
        Users user = userRepository.findByUid(userId);
        Category category = cateRepository.getOneCategory(projectDto.getCategoryId());
        Status status = statusRepository.findById("new").orElseThrow(
                () -> new RuntimeException("Status not found")
        );
        Assert.notNull(category, "CATEGORY_NOT_FOUND");
        Assert.notNull(user, "USER NOT FOUND");

        Project project = Project.builder()
                .projectName(projectDto.getProjectName())
                .description(projectDto.getDescription())
                .pic(user)
                .createAt(LocalDateTime.now())
                .endPO(LocalDate.now())
                .category(category)
                .completed(false)
                .startDate(projectDto.getStartDate())
                .startEstimate(projectDto.getStartEstimate())
                .startPO(projectDto.getStartPO())
                .startPR(projectDto.getStartPR())
                .startReceiveRequest(projectDto.getStartReceiveRequest())
                .startRequestPurchase(projectDto.getStartRequestPurchase())
                .startSubmitBudget(projectDto.getStartSubmitBudget())
                .startQuotation(projectDto.getStartQuotation())
                .endPR(projectDto.getEndPR())
                .endEstimate(projectDto.getEndEstimate())
                .endQuotation(projectDto.getEndQuotation())
                .endReceiveRequest(projectDto.getEndReceiveRequest())
                .endSubmitBudget(projectDto.getEndSubmitBudget())
                .endRequestPurchase(projectDto.getEndRequestPurchase())
                .endPO(projectDto.getEndPO())
                .endDate(projectDto.getEndDate())
                .progress(0)
                .status(status)
                .year(projectDto.getYear())
                .canceled(false)
                .isDeleted(false)
                .build();
        project = projectRepository.save(project);

        return project.getProjectId();
    }

    public Integer getYearFromDateTime(LocalDate dateTime) {
        return dateTime.getYear();
    }

    @Override
    @Transactional
    public Boolean updateProject(String userId, ProjectDTO project) {

        Users user = userRepository.findByUid(project.getPic());
        Project projectCheck = projectRepository.findByProjectId(project.getProjectId());
        Assert.notNull(projectCheck, "PROJECT_NOT_FOUND");

        LocalDate dateTime = project.getStartDate();
        Integer year = getYearFromDateTime(dateTime);

        Assert.notNull(dateTime, "START DATE CANNOT BE NULL");
        projectCheck.setStartDate(project.getStartDate());
        projectCheck.setStartPO(project.getStartPO());
        projectCheck.setStartEstimate(project.getStartEstimate());
        projectCheck.setStartPR(project.getStartPR());
        projectCheck.setStartQuotation(project.getStartQuotation());
        projectCheck.setStartReceiveRequest(project.getStartReceiveRequest());
        projectCheck.setStartRequestPurchase(project.getStartRequestPurchase());
        projectCheck.setStartSubmitBudget(project.getStartSubmitBudget());

        projectCheck.setEndDate(project.getEndDate());
        projectCheck.setEndPO(project.getEndPO());
        projectCheck.setEndEstimate(project.getEndEstimate());
        projectCheck.setEndPR(project.getEndPR());
        projectCheck.setEndReceiveRequest(project.getEndReceiveRequest());
        projectCheck.setEndRequestPurchase(project.getEndRequestPurchase());
        projectCheck.setEndQuotation(project.getEndQuotation());
        projectCheck.setEndSubmitBudget(project.getEndSubmitBudget());
        projectCheck.setYear(year);

        projectCheck.setPic(user);
        projectRepository.save(projectCheck);

        return true;
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAllProject();
    }

    @Override
    public List<Project> findProjectByUserId(String uid) {

        return projectRepository.checkProjectByUserId(uid);
//    return projectRepository.findAll();
    }


    @Override
    public Boolean checkOwnerProject(String uid, Long projectId) {

        return !Objects.equals(projectRepository.checkOwnerProject(uid, projectId), "NO");

    }

    @Override
    public List<ChartDto> getDashboard() {
        return projectRepository.getCharts();
    }

    @Override
    public List<ChartDto> getDashboardFromTo(int from, int to) {
        return projectRepository.getChartFromTo(from, to);
    }


    @Override
    public List<CategoryCountDTO> getTotal() {
        return projectRepository.getTotal();
    }

    @Override
    public List<ProjectCompleted> getCompleted() {
        return projectRepository.getCompleted();
    }

    @Override
    public List<ProjectProjection> getProjectName() {
        return projectRepository.getProjectName();
    }

    @Override
    public List<ProjectCompleted2> getCompleted2() {
        return projectRepository.getCompleted2();
    }

    @Override
    public List<ProjectCompleted2> getTotalFromTo(int fromYear, int toYear) {
        return projectRepository.getTotalFromTo(fromYear, toYear);
    }

    @Override
    @Transactional
    public boolean updateStatus(ProjectUpdateDTO projectDTO, String uid) {
        Project project = projectRepository.findByProjectId(projectDTO.getProjectId());
        Status status = statusRepository.findById(projectDTO.getStatus()).orElseThrow(
                () -> new RuntimeException("Status not found"));
        Assert.notNull(project, "PROJECT_NOT_FOUND");
        project.setStatus(status);
        switch (status.getStatusId()) {
            case Const.STATUS_PROJECT.COMPLETED -> {
                project.setCompleted(true);
                project.setCanceled(false);
                project.setAccepted(false);
            }
            case Const.STATUS_PROJECT.CANCELLED -> {
                project.setCompleted(false);
                project.setCanceled(true);
                project.setAccepted(false);
            }
            case Const.STATUS_PROJECT.ACCEPTANCE, Const.STATUS_PROJECT.CONSTRUCTION -> {
                project.setCompleted(false);
                project.setCanceled(false);
                project.setAccepted(true);
            }
            default -> {
                project.setCompleted(false);
                project.setCanceled(false);
                project.setAccepted(false);
            }
        }
        project.setStatus(status);
        this.projectRepository.save(project);

        StatusUpdateHis statusUpdateHis = StatusUpdateHis.builder()
                .project(project)
                .status(status)
                .user(userRepository.findByUid(uid))
                .createAt(OffsetDateTime.now())
                .build();
        statusUpdateHisRepository.save(statusUpdateHis);
        return true; // Không cập nhật (do không tìm thấy hoặc trạng thái đã là true)
    }



    @Override
    public List<Project> search(String value) {
        return projectRepository.searchProject(value);
    }

    @Override
    public List<Project> getProjectByName(String projectName) {

        return projectRepository.findByName(projectName);

    }

    @Override
    public List<Project> searchChart(ProjectDTO projectDTO) {
        Category category = categoryRepository.findByCategoryName(projectDTO.getCategoryName());
        Assert.notNull(category, "CATEGORY_NOT_FOUND");

        String categoryId = category.getCategoryId();
        if (projectDTO.getType().contains("Total")) {
            if (projectDTO.getType().equals("acceptanceTotal")) {
                return projectRepository.getAcceptanceDataTotal(categoryId);
            } else if (projectDTO.getType().equals("remainTotal")) {
                return projectRepository.getInProgressDataTotal(categoryId);
            } else {
                return projectRepository.getByChart(categoryId, projectDTO.getStatus());

            }
        } else {
            if (projectDTO.getType().equals("acceptance")) {
                return projectRepository.getAcceptanceData(categoryId, projectDTO.getStatus());
            } else if (projectDTO.getType().equals("remain")) {
                return projectRepository.getInProgressData(categoryId);
            } else {
                return projectRepository.getByChart(categoryId, projectDTO.getStatus());
            }
        }
    }

    @Override
    public List<Project> searchChartFromTo(FromToDTO dto) {
        Category category = categoryRepository.findByCategoryName(dto.getCategoryName());
        Assert.notNull(category, "CATEGORY_NOT_FOUND");
        String categoryId = category.getCategoryId();
//        Boolean completed = dto.getCompleted();
//        Boolean cancelled = dto.getCancelled();
        int from = dto.getFrom();
        int to = dto.getTo();
        if (dto.getType().contains("Total")) {
            if (dto.getType().equals("acceptanceTotal")) {
                return projectRepository.getAcceptanceDataTotalFromTo(categoryId, from, to);
            } else if (dto.getType().equals("remainTotal")) {
                return projectRepository.getInProgressDataTotalFromTo(categoryId, from, to);
            } else {
                return projectRepository.getByChartFromTo(categoryId, dto.getStatus(), from, to);
            }
        } else {
            if (dto.getType().equals("acceptance")) {
                return projectRepository.getAcceptanceDataFromTo(categoryId, dto.getStatus(), from, to);
            } else if (dto.getType().equals("remain")) {
                return projectRepository.getInProgressDataFromTo(categoryId, from, to);
            } else {
                return projectRepository.getByChartFromTo(categoryId, dto.getStatus(), from, to);
            }
        }
//        return projectRepository.getByChartFromTo(categoryId, completed, cancelled, from, to);
    }

    @Override
    public boolean deleteProject(Long projectId, String userId) {
        Users user = userRepository.findByUid(userId);

        Project project = projectRepository.findByProjectId(projectId);
        Assert.notNull(project, "PROJECT_NOT_FOUND");
        if (project.getIsDeleted()) {
            throw new RuntimeException("PROJECT_DELETED");
        } else {
            projectRepository.deleteProject(projectId, user.getUid());

            return true;
        }
    }

}
