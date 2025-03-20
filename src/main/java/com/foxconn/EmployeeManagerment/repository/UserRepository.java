package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.projection.EmailReportProjection;
import com.foxconn.EmployeeManagerment.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("ALL")
@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    Users findByUid(String uid);

    @Query(value = "SELECT u FROM Users u WHERE u.fullName like %:keyword%"
            + " OR u.employeeCode like %:keyword%"
            + " OR u.mobile like %:keyword%"
    )
    List<Users> search(@Param("keyword") String keyword);

    @Query("SELECT trim(u.uid) as uid, u.fullName as fullName FROM Users u where trim(u.uid) not in :uid")
    List<UserProjection> getUser(@Param("uid") String uid);

    @Query("SELECT u FROM Users u where u.employeeCode = :employeeCode ")
    Users findByEmployeeCode(@Param("employeeCode") String employeeCode);

    @Query("SELECT trim(u.uid) as uid, u.fullName as fullName FROM Users u ")
    List<UserProjection> getAllUser();

    @Query("select u.email from Users u where u.isReceive = true order by u.role.role_id")
    List<String> getUserEmail();

    @Query(value = """
            SELECT u.uid, u.full_name,
                  CASE
                      WHEN r.reporter_id IS NOT NULL THEN '已報告'
                      ELSE '未報告'
                      END AS report
                      FROM emt.users u
                      LEFT JOIN emt.daily_report r
                      ON u.uid = r.reporter_id
                      AND r.create_at::DATE between CURRENT_DATE   and now()
                      where u.is_report = true
                      GROUP BY u.uid, u.full_name, r.reporter_id """, nativeQuery = true)

    List<EmailReportProjection> getUserReport();
}
