package com.foxconn.EmployeeManagerment.dto.request;

import com.foxconn.EmployeeManagerment.entity.Category;
import com.foxconn.EmployeeManagerment.entity.Project;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int progress;

    private LocalDateTime startReceiveRequest;

    private LocalDateTime endReceiveRequest;

    private LocalDateTime startEstimate;

    private LocalDateTime endEstimate;

    private LocalDateTime startRequestPurchase;

    private LocalDateTime endRequestPurchase;

    private LocalDateTime startQuotation;

    private LocalDateTime endQuotation;

    private LocalDateTime startSubmitBudget;

    private LocalDateTime endSubmitBudget;

    private LocalDateTime startPR;

    private LocalDateTime endPR;

    private LocalDateTime startPO;

    private LocalDateTime endPO;

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
