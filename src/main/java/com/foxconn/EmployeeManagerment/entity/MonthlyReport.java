package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "title")
    private String title;

    @Column(name = "createAt")
    private LocalDateTime create_at;

    @Column(name = "description")
    private String description;

    @Column(name = "project_id")
    private Long projectId;
}
