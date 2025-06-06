package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.FromDateToDateDTO;
import com.foxconn.EmployeeManagerment.dto.request.FromToDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectUpdateDTO;
import com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectDTO;
import com.foxconn.EmployeeManagerment.dto.response.ChartDto;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.projection.ProjectProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Project getProjectByUser(String userId);

    Project getAllproject();

    Long createProject(ProjectDTO project) throws Exception;

    Boolean updateProject(String userId, ProjectDTO project);

    List<Project> getAllProject();


    List<Project> findProjectByUserId();

    Boolean checkOwnerProject(String uid, Long projectId);

    List<ChartDto> getDashboard();

    List<CategoryCountDTO> getTotal();

    List<ProjectCompleted> getCompleted();

    List<ProjectProjection> getProjectName();

    List<ProjectCompleted2> getCompleted2();

    boolean updateStatus(ProjectUpdateDTO projectDTO, String userId);

    List<Project> search(String projectName);

    List<Project> getProjectByName(String projectName);

    List<Project> searchChart(ProjectDTO projectDTO);

    boolean deleteProject(Long projectId, String userId);

    List<ProjectCompleted2> getTotalFromTo(int from, int to);

    List<ProjectCompleted2> getTotalFromTo(String from, String to);


    List<ChartDto> getDashboardFromTo(int from, int to);

    List<ChartDto> getDashboardFromDateToDate(String from, String to);

    List<Project> searchChartFromTo(FromToDTO dto);

    List<Project> searchChartFromTo(FromDateToDateDTO dto);

}
