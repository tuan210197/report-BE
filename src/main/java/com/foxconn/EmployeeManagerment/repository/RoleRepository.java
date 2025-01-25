package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT r FROM Role r WHERE r.role_id =:keyword")

    Role search(@Param("keyword") Long keyword);
}
