package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.dto.request.OptionDTO;
import com.foxconn.EmployeeManagerment.entity.Calendar;
import com.foxconn.EmployeeManagerment.projection.CalendarProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Calendar findByDate(LocalDate date);

    @Query("select c.id as id, c.title as title, c.date as date from Calendar c where cast(c.date as DATE ) BETWEEN :from and :to")
    List<CalendarProjection> getCalendarByMonth(@Param("from") LocalDate from, @Param("to") LocalDate to);
}