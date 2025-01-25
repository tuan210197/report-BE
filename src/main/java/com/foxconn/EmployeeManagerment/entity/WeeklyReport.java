package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "weekly_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class WeeklyReport {

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
    // add report field 25/11
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "category", nullable = false)
    private Long category;

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
}
