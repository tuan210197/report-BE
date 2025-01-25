package com.foxconn.EmployeeManagerment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "description")
    private String description;

//    @Column(name = "pic", nullable = false)
//    private String pic;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "progress" )
    private int progress;

    @Column(name = "start_receive_request")
    private LocalDateTime startReceiveRequest;

    @Column(name = "end_receive_request")
    private LocalDateTime endReceiveRequest;

    @Column(name = "start_estimate")
    private LocalDateTime startEstimate;

    @Column(name = "end_estimate")
    private LocalDateTime endEstimate;

    @Column(name = "start_request_purchase")
    private LocalDateTime startRequestPurchase;

    @Column(name = "end_request_purchase")
    private LocalDateTime endRequestPurchase;

    @Column(name = "start_quotation")
    private LocalDateTime startQuotation;

    @Column(name = "end_quotation")
    private LocalDateTime endQuotation;

    @Column(name = "start_submit_budget")
    private LocalDateTime startSubmitBudget;

    @Column(name = "end_submit_budget")
    private LocalDateTime endSubmitBudget;

    @Column(name = "start_pr")
    private LocalDateTime startPR;

    @Column(name = "end_pr")
    private LocalDateTime endPR;

    @Column(name = "start_po")
    private LocalDateTime startPO;

    @Column(name = "end_po")
    private LocalDateTime endPO;

    @Column(name = "completed")
    private Boolean completed;

    @JsonIgnore
    @OneToMany(mappedBy = "projects", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Implement> implement;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyReport> dailyReports;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pic", nullable = false)
    private Users pic;

    @JsonIgnore
    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubMember> subMembers;
}
