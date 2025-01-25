package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport,Long> {
}
