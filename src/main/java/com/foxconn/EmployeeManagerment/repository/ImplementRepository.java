package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.Implement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface ImplementRepository extends JpaRepository<Implement, Long> {
    @Query("SELECT i FROM Implement i where i.users.uid = :userImplement")
    List<Implement> findByUsers(@Param("userImplement") String userImplement);

    @Query("SELECT i FROM Implement i where i.projects.projectId = :id")
    List<Implement> findByProjectId(@Param("id") Long id);

//    List<Implement> findByProjectId(Long projectId);

}
