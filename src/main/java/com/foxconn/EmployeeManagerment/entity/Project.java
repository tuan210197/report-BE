package com.foxconn.EmployeeManagerment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import java.time.LocalDate;
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

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "description")
    private String description;


    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "progress" )
    private int progress;

    @Column(name = "start_receive_request")
    private LocalDate startReceiveRequest;

    @Column(name = "end_receive_request")
    private LocalDate endReceiveRequest;

    @Column(name = "start_estimate")
    private LocalDate startEstimate;

    @Column(name = "end_estimate")
    private LocalDate endEstimate;

    @Column(name = "start_request_purchase")
    private LocalDate startRequestPurchase;

    @Column(name = "end_request_purchase")
    private LocalDate endRequestPurchase;

    @Column(name = "start_quotation")
    private LocalDate startQuotation;

    @Column(name = "end_quotation")
    private LocalDate endQuotation;

    @Column(name = "start_submit_budget")
    private LocalDate startSubmitBudget;

    @Column(name = "end_submit_budget")
    private LocalDate endSubmitBudget;

    @Column(name = "start_pr")
    private LocalDate startPR;

    @Column(name = "end_pr")
    private LocalDate endPR;

    @Column(name = "start_po")
    private LocalDate startPO;

    @Column(name = "end_po")
    private LocalDate endPO;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "canceled ")
    private Boolean canceled;

    @Column(name = "year")
    private Integer year;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @JsonIgnore
    @OneToMany(mappedBy = "projects", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Implement> implement;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyReport> dailyReports;

    @JsonIgnore
    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileUpload> project;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pic")
    private Users pic;

    //    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_delete")
    private Users userDeleted;

    @JsonIgnore
    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubMember> subMembers;



}
