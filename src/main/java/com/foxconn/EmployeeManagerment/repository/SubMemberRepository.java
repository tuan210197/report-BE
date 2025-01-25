package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.dto.response.SubMemberDto;
import com.foxconn.EmployeeManagerment.entity.SubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubMemberRepository extends JpaRepository<SubMember,Long> {


    @Query(value = "select case when " +
            "(select count(*) from " +
            "(select p.project_id, p.project_name, p.pic, u1.full_name , s.user_id , u2.full_name " +
            "from emt.project p join emt.sub_member s " +
            "on p.project_id = s.project_id " +
            "join emt.users u1 " +
            "on u1.uid = p.pic " +
            "join emt.users u2 " +
            "on u2.uid = s.user_id " +
            "where p.project_id = :projectId " +
            "and (p.pic = :userId or s.user_id = :userId )))  > 0 " +
            "then true else false end as check ", nativeQuery = true)
    Boolean check (@Param("projectId") Long projectId, @Param("userId") String userId);

    List<SubMember> getProjectByUser(String user);
}
