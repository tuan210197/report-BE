package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    Users findByUid(String uid);

    @Query(value = "SELECT u FROM Users u WHERE u.fullName like %:keyword%"
            + " OR u.employeeCode like %:keyword%"
            + " OR u.mobile like %:keyword%"
    )
    List<Users> search(@Param("keyword") String keyword);

    @Query("SELECT trim(u.uid) as uid, u.fullName as fullName FROM Users u")
    List<UserProjection> getUser();
}
