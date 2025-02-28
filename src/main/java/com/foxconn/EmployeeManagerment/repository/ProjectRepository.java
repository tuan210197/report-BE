package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO;
import com.foxconn.EmployeeManagerment.dto.response.ChartDto;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.projection.ProjectProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {



    @Query(value = "Select p from Project p where p.projectId = :projectId AND p.isDeleted = false ")
    Project findByProjectId(@Param( "projectId") Long projectId);

    @Query(value = "SELECT p FROM Project p " +
            "WHERE p.isDeleted = false " +
            "ORDER BY CASE WHEN p.completed = true THEN 1 ELSE 0 END, p.projectId ASC")
    List<Project> checkProjectByUserId(@Param("userId") String userId);


    @Query(value = "SELECT " +
            "    CASE " +
            "        WHEN EXISTS ( " +
            "            SELECT 1 " +
            "            FROM emt.project p " +
            "            WHERE p.pic = :uid AND p.project_id = :projectId " +
            "        ) THEN 'PIC' " +
            "        WHEN EXISTS ( " +
            "            SELECT 1 " +
            "            FROM emt.sub_member s" +
            "            WHERE s.user_id = :uid AND s.project_id = :projectId " +
            "        ) THEN 'SUB' " +
            "        ELSE 'NO' " +
            "    END AS role", nativeQuery = true)
    String checkOwnerProject( @Param("uid") String uid, @Param("projectId") Long projectId);


    @Query("SELECT new com.foxconn.EmployeeManagerment.dto.response.ChartDto( " +
            "c.categoryName, " +
            "COUNT(p.projectId), " +
            "SUM(CASE WHEN p.completed = true and p.canceled = false  THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.completed = false and p.progress >= 0 and p.canceled = false THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.canceled = true THEN 1 ELSE 0 END)) " +
            "FROM Category c " +
            "LEFT JOIN c.projects p " +
            "on p.isDeleted = false "+
            "and p.year =:year "+
            "GROUP BY c.categoryId, c.categoryName " +
            "ORDER BY c.categoryName")
    List<ChartDto> getCharts(@Param("year") int year);

    @Query("SELECT new com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO(p.category.categoryName, COUNT(p)) " +
            "FROM Project p GROUP BY p.category.categoryName")
    List<CategoryCountDTO> getTotal();

    @Query( "select new com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted(CAST(p.completed AS string) , count(p.completed)) " +
            "FROM Project p where p.isDeleted = false  " +
            "group by p.completed")
    List<ProjectCompleted> getCompleted();

    @Query( "select new com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2("
            +"COUNT(p) ,"+
            "COUNT(CASE WHEN p.canceled = true THEN 1 END) ," +
            "COUNT(CASE WHEN p.progress >= 0 AND p.progress < 100 AND p.completed = false and p.canceled = false THEN 1 END) ," +
            "COUNT(CASE WHEN p.progress = 100 AND p.completed = true and p.canceled = false  THEN 1 END))" + " FROM Project p where " +
            "p.year =:year and  p.isDeleted = false ")
    List<ProjectCompleted2> getCompleted2(@Param("year") int year);

    @Query("SELECT p.projectId AS projectId, p.projectName AS projectName FROM Project p WHERE p.completed = FALSE  and p.isDeleted = false ")
    List<ProjectProjection>  getProjectName();


    @Query("SELECT p FROM Project p " +
            "WHERE upper(COALESCE(p.projectName, '')) LIKE %:value% " +
            "OR upper(COALESCE(p.category.categoryName, '')) LIKE %:value% " +
            "OR upper(COALESCE(p.pic.fullName, '')) LIKE %:value% " +
            "and p.isDeleted = false " +
            "order by p.completed asc")
    List<Project> searchProject(@Param("value") String value);

    @Query("SELECT p FROM Project p WHERE UPPER(COALESCE(p.projectName, '')) LIKE UPPER(CONCAT('%', :projectName, '%')) and p.isDeleted = false ")
    List<Project> findByName(@Param("projectName") String projectName);
//
//    @Query("SELECT p FROM Project p where UPPER(COALESCE(p.category, '')) = UPPER(:projectName) and p.completed =:completed and p.canceled =:cancelled")
//    List<Project> getByChart(@Param("categoryName") String categoryName,@Param("completed") Boolean completed,@Param("cancelled") Boolean cancelled);

    @Query("SELECT p FROM Project p WHERE  p.category.categoryId = :categoryId AND p.completed = :completed AND p.canceled = :canceled and p.isDeleted = false and p.year =:year ")
    List<Project> getByChart(@Param("categoryId") String categoryId, @Param("completed") Boolean completed, @Param("canceled") Boolean canceled, @Param("year") int year);

    @Query("select p from Project p where p.isDeleted = false ")
    List<Project> findAllProject();

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.isDeleted = true, p.userDeleted.uid = :userId WHERE p.projectId = :projectId")
    void deleteProject(@Param("projectId") Long projectId, @Param("userId") String userId);

}
