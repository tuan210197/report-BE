package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import javax.print.attribute.standard.MediaSize;

@Entity
@Table(name = "status")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Status {

    @Id
    @Column(name = "status_id")
    private String statusId;

    @Column(name ="status_name")
    private String statusName;
}
