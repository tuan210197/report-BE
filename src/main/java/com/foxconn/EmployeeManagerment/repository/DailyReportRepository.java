package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.DailyReport;
import com.foxconn.EmployeeManagerment.projection.DailyReportProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
//    List<DailyReport> findByProjectId(Long projectId);

    @Query("SELECT  d FROM DailyReport d  WHERE d.user.uid = :uid order by d.create_at desc")
    List<DailyReport> findByUerDetail(@Param("uid") String uid);

    @Query("select d from DailyReport d order by d.create_at desc")
    List<DailyReport> findAllDailyReport();

    @Query("SELECT d FROM  DailyReport d where d.project.projectId =:projectId and trim(d.user.uid) = :userId order by d.create_at desc")
    List<DailyReport> findByProjectId(@Param("userId") String userId, @Param("projectId") Long projectId, Pageable pageable);

    @Query(value = """
                WITH latest_report AS (
                    SELECT *, ROW_NUMBER() OVER (PARTITION BY reporter_id, project_id ORDER BY create_at DESC) AS rn
                    FROM emt.daily_report WHERE to_char(create_at,'YYYY-MM-DD' ) = :date
                ),
                latest_implement AS (
                    SELECT *, ROW_NUMBER() OVER (PARTITION BY user_implement, project_id ORDER BY create_at DESC) AS rn
                    FROM emt.implement WHERE to_char(create_at,'YYYY-MM-DD' ) = :date
                )
                SELECT 
                    u.full_name AS fullName,
                    r.requester AS requester,
                    p.project_name AS projectName,
                    c.category_name AS categoryName,
                    r.address AS address, 
                    r.contractor AS contractor,
                    r.number_worker AS numberWorker,
                    r.progress AS progress,
                    r.quantity AS quantity,
                    r.quantity_completed AS quantityCompleted,
                    r.quantity_remain AS quantityRemain,
                    r.start_date AS startDate,
                    r.end_date AS endDate,
                    i.implement AS implement,
                    r.create_at AS createAt
                FROM latest_report r
                JOIN latest_implement i ON r.project_id = i.project_id
                    AND trim(r.reporter_id) = trim(i.user_implement)
                JOIN emt.users u ON trim(r.reporter_id) = trim(u.uid)
                JOIN emt.project p ON r.project_id = p.project_id
                JOIN emt.category c ON r.category_id = c.category_id
                WHERE r.rn = 1 AND i.rn = 1
            """, nativeQuery = true)
    List<DailyReportProjection> export(@Param("date") String date);

    @Query(value = "SELECT TO_CHAR(create_at, 'YYYY-MM-DD') AS formatted_date " +
            "FROM emt.daily_report " +
            "ORDER BY create_at DESC " +
            "LIMIT 1", nativeQuery = true)
    String getMaxDate();

    @Query("SELECT d FROM DailyReport d " +
            "WHERE d.user.uid = :userId " +
            "AND d.project.projectId = :projectId " +
            "AND d.create_at BETWEEN :startOfDay AND :endOfDay")
   List<DailyReport>  checkDailyReport(@Param("projectId") Long projectId,
                                 @Param("userId") String userId,
                                 @Param("startOfDay") LocalDateTime startOfDay,
                                 @Param("endOfDay") LocalDateTime endOfDay);

}
