package com.foxconn.EmployeeManagerment.dto.request;

import com.foxconn.EmployeeManagerment.entity.Category;
import com.foxconn.EmployeeManagerment.entity.Project;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Long projectId;

    private String projectName;

    private String description;

    private String pic;

    private String categoryId;

    private String CategoryName;

    private LocalDateTime createAt;

    private LocalDate startDate;

    private LocalDate endDate;

    private int progress;

    private LocalDate startReceiveRequest;

    private LocalDate endReceiveRequest;

    private LocalDate startEstimate;

    private LocalDate endEstimate;

    private LocalDate startRequestPurchase;

    private LocalDate endRequestPurchase;

    private LocalDate startQuotation;

    private LocalDate endQuotation;

    private LocalDate startSubmitBudget;

    private LocalDate endSubmitBudget;

    private LocalDate startPR;

    private LocalDate endPR;

    private LocalDate startPO;

    private LocalDate endPO;

    private Boolean completed;

//    public static ProjectDTO projectResponseDto (Project project){
//        ProjectDTO projectDTO = new ProjectDTO();
//        projectDTO.setProjectId(project.getProjectId());
//        projectDTO.setProjectName(project.getProjectName());
//        projectDTO.setCompleted(project.getCompleted());
//        projectDTO.setDescription(project.getDescription());
//        projectDTO.setStartDate(project.getStartDate());
//        projectDTO.setStartPO(project.getStartPO());
//        projectDTO.setStartEstimate(project.getStartEstimate());
//        projectDTO.setStartPR(project.getStartPR());
//        projectDTO.setStartQuotation(project.getStartQuotation());
//        projectDTO.setStartReceiveRequest(project.getStartReceiveRequest());
//        projectDTO.setStartRequestPurchase(project.getStartRequestPurchase());
//        projectDTO.setStartSubmitBudget(project.getStartSubmitBudget());
//        projectDTO.setEndDate(project.getEndDate());
//        projectDTO.setEndPO(project.getEndPO());
//        projectDTO.setEndEstimate(project.getEndEstimate());
//        projectDTO.setEndPR(project.getEndPR());
//        projectDTO.setEndReceiveRequest(project.getEndReceiveRequest());
//        projectDTO.setEndRequestPurchase(project.getEndRequestPurchase());
//        projectDTO.setEndQuotation(project.getEndQuotation());
//        projectDTO.setCategoryId(projectDTO.getCategoryId());
//
//        return projectDTO;
//    }
}
