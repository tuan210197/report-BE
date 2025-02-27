package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.DailyReport;
import com.foxconn.EmployeeManagerment.entity.Implement;
import com.foxconn.EmployeeManagerment.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ImplementRepository extends JpaRepository<Implement, Long> {
    @Query("SELECT i FROM Implement i where i.users.uid = :userImplement")
    List<Implement> findByUsers(@Param("userImplement") String userImplement);

    @Query("SELECT i FROM Implement i where i.projects.projectId = :id order by i.createAt desc")
    List<Implement> findByProjectId(@Param("id") Long id);

    @Query("SELECT i FROM Implement i " +
            "WHERE trim(i.users.uid) = trim(:userId) " +
            "AND i.projects.projectId = :projectId " +
            "AND i.createAt BETWEEN :startOfDay AND :endOfDay")
    List<Implement>  findImplement(@Param("projectId") Long projectId,
                                        @Param("userId") String userId,
                                        @Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);

    @Query("select i from Implement i where i.projects.projectId =:projectId " +
            "and i.createAt in (SELECT MAX(i2.createAt) FROM Implement i2 " +
            "                          WHERE i2.projects.projectId = :projectId  " +
            "                          GROUP BY i2.users.uid)")
    List<Implement> getImplementByProjects(@Param("projectId") Long projectId);

//    List<Implement> findByProjectId(Long projectId);

}
