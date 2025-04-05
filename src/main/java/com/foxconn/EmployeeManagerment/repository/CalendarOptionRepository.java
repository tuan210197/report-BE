package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.CalendarOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarOptionRepository extends JpaRepository<CalendarOption, Long> {
}
