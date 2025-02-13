package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.ProjectUpdateDTO;
import com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectDTO;
import com.foxconn.EmployeeManagerment.dto.response.ChartDto;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.projection.ProjectProjection;

import java.util.List;

public interface ProjectService {

    Project getProjectByUser(String userId);
    Project getAllproject();
    Long createProject(ProjectDTO project, String userId) throws  Exception;
    Boolean updateProject(String userId, ProjectDTO project);

    List<Project> getAllProject();


    List<Project> findProjectByUserId(String uid);


    Boolean checkOwnerProject(String uid, Long projectId);


    List<ChartDto> getDashboard();

    List<CategoryCountDTO> getTotal();

    List<ProjectCompleted> getCompleted();

    List<ProjectProjection> getProjectName();

    List<ProjectCompleted2> getCompleted2();

    boolean updateStatus(ProjectUpdateDTO projectDTO);

    List<Project>  search(String projectName);
}
