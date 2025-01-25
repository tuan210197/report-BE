package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.BasicLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicLoginRepository extends JpaRepository<BasicLogin, String> {
    @Query("select a from BasicLogin a where a.email = :email")
    BasicLogin findByEmail(String email);

    @Query("select a from BasicLogin a where a.userUid = :userUid")
    BasicLogin findByUserUid(String userUid);

    void deleteByEmail(String email);
}
