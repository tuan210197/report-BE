package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}
