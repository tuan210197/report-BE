package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "status_update_his")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusUpdateHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "create_at")
    private OffsetDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_update")
    private Users user;


}
