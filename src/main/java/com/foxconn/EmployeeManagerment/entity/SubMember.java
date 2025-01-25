package com.foxconn.EmployeeManagerment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "sub_member")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String user;

    @Column(name = "project_name", nullable = false)
    private  String projectName;

//    @Column(name = "project_id", nullable = false)
//    private Long projectId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;
}
