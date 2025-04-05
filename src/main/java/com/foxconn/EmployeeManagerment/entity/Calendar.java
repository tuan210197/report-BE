package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Table(name = "calendar")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Calendar {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    String title;
    @Column(name = "date")
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "uid")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "task")
    private CalendarOption task;


    public Calendar(Users user ,String title, LocalDate date, CalendarOption task) {
        this.user = user;
        this.title = title;
        this.date = date;
        this.task = task;
    }
}
