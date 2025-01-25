package com.foxconn.EmployeeManagerment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "department")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class Department {

    @Id
    @Column(name = "dept_id", columnDefinition = "CHAR(128) NOT NULL")
    private String deptId ;

    @Column(name = "dept_name")
    private String deptName;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Users> users;
}
