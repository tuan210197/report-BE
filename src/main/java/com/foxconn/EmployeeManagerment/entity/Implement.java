package com.foxconn.EmployeeManagerment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table( name = "implement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Implement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "implement")
    private String implement;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project projects;

    @ManyToOne
    @JoinColumn(name = "user_implement", nullable = false)
    private Users users;
}
