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

    private Boolean cancelled;

}
