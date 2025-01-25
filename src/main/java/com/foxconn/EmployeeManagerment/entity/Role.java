package com.foxconn.EmployeeManagerment.entity;



import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Role implements Serializable{
    private static final long serialVersionUID = -5204391003825277886L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long role_id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<Users> users;


}