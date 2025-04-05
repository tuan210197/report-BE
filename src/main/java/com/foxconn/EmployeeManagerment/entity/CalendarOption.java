package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calendar_option")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarOption {
    @Id
    @Column
    private Long id;

    @Column(name="value")
    private String value;
}
