package com.foxconn.EmployeeManagerment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

//    @Column(name = "reporter_id")
//    private String reporterId;

    @Column(name = "createAt")
    private LocalDateTime create_at;

//    @Column(name = "project_id")
//    private Long projectId;

    // add report field 25/11
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

//    @Column(name = "category", nullable = false)
//    private String categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // Định nghĩa khóa ngoại
    private Category category;

    @Column(name = "quantity_completed", nullable = false)
    private Long quantityCompleted;

    @Column(name = "quantity_remain", nullable = false)
    private Long quantityRemain;

    @Column(name = "contractor", nullable = false)
    private String contractor;

    @Column(name = "number_worker", nullable = false)
    private Long numberWorker;

    @Column(name = "requester", nullable = false)
    private String requester;

    @Column(name = "progress", nullable = false)
    private Long progress;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) // Định nghĩa khóa ngoại
    private Project project;

    @ManyToOne
    @JoinColumn(name = "reporter_id",  updatable = false)
    private Users user;
}
