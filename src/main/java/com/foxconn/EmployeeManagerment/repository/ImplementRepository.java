package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.DailyReport;
import com.foxconn.EmployeeManagerment.entity.Implement;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.projection.ImplementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ImplementRepository extends JpaRepository<Implement, Long> {
    @Query("SELECT i FROM Implement i where i.users.uid = :userImplement")
    List<Implement> findByUsers(@Param("userImplement") String userImplement);

    @Query("SELECT d.createAt as createAt, " +
            "d.implement as implement, " +
            "d.project.projectName as projectName, " +
            "d.category.categoryName as category,  " +
            "d.user.fullName as fullName  " +
            "FROM DailyReport d where d.project.projectId = :id order by d.createAt desc")
    List<ImplementProjection> findByProjectId(@Param("id") Long id);

    @Query("SELECT i FROM Implement i " +
            "WHERE trim(i.users.uid) = trim(:userId) " +
            "AND i.projects.projectId = :projectId " +
            "AND i.createAt BETWEEN :startOfDay AND :endOfDay")
    List<Implement>  findImplement(@Param("projectId") Long projectId,
                                        @Param("userId") String userId,
                                        @Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);

    @Query("select i from DailyReport i where i.project.projectId =:projectId " +
            "order by i.reportId desc")
    Page<DailyReport> getImplementByProjects(@Param("projectId") Long projectId, Pageable pageable);

//    List<Implement> findByProjectId(Long projectId);

}
