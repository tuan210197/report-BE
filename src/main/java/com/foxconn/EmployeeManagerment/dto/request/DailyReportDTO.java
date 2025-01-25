package com.foxconn.EmployeeManagerment.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DailyReportDTO {
    private Long reportId;
    private String reporterId;
    private LocalDateTime create_at;
    private Long projectId;
    private String address;
    private Long quantity;
    private String categoryId;
    private Long quantityCompleted;
    private Long quantityRemain;
    private String contractor;
    private Long numberWorker;
    private String requester;
    private Long progress;
    private LocalDate startDate;
    private LocalDate endDate;
    private String implement;
}
