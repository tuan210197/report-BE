package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.controller.BaseController;
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
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProjectImpl extends BaseController implements ProjectService {
    private final CategoryRepository categoryRepository;


    private final ProjectRepository projectRepository;
    private final SubMemberRepository subMemberRepository;
    private final CateRepository cateRepository;
    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;

    public ProjectImpl(ProjectRepository projectRepository, SubMemberRepository subMemberRepository, CateRepository cateRepository, UserRepository userRepository, MailSenderService mailSenderService,
                       CategoryRepository categoryRepository) {
        this.projectRepository = projectRepository;
        this.subMemberRepository = subMemberRepository;
        this.cateRepository = cateRepository;
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
        this.categoryRepository = categoryRepository;
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
    public List<ChartDto> getDashboard(int year) {
        return projectRepository.getCharts(year);
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
    public List<ProjectCompleted2> getCompleted2(int year) {
        return projectRepository.getCompleted2(year);
    }

    @Override
    public boolean updateStatus(ProjectUpdateDTO projectDTO) {
        Project project = projectRepository.findByProjectId(projectDTO.getProjectId());
        Assert.notNull(project, "PROJECT_NOT_FOUND");


        if (Objects.equals(projectDTO.getStatus(), "completed")) {
            project.setCompleted(true);
            project.setProgress(100);
            project.setCanceled(false);
        }
        if (Objects.equals(projectDTO.getStatus(), "remain")) {
            project.setCompleted(false);
            project.setProgress(99);
            project.setCanceled(false);
        }
        if (Objects.equals(projectDTO.getStatus(), "canceled")) {
            project.setCanceled(true);
        }

        this.projectRepository.save(project);

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
        Boolean completed = projectDTO.getCompleted();
        Boolean cancelled = projectDTO.getCancelled();
        int year = projectDTO.getYear();
        return projectRepository.getByChart(categoryId, completed, cancelled, year);

    }

    @Override
    public boolean deleteProject(Long projectId, String userId) {
        Users user = userRepository.findByUid(userId);

        Project project = projectRepository.findByProjectId(projectId);
        Assert.notNull(project, "PROJECT_NOT_FOUND");
        if(project.getIsDeleted()){
            throw new RuntimeException("PROJECT_DELETED");
        }else {
            projectRepository.deleteProject(projectId, user.getUid());

            return true;
        }
    }

}
