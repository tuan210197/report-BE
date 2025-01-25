package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
//    List<DailyReport> findByProjectId(Long projectId);

    @Query("SELECT  d FROM DailyReport d  WHERE d.user.uid = :uid order by d.create_at desc")
    List<DailyReport> findByUerDetail (@Param("uid") String uid);

    @Query("select d from DailyReport d order by d.create_at desc")
    List<DailyReport> findAllDailyReport();
}
