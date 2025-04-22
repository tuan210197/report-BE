package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO;
import com.foxconn.EmployeeManagerment.dto.response.ChartDto;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted;
import com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.projection.MinMaxYearProjection;
import com.foxconn.EmployeeManagerment.projection.ProjectProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Project findByProjectId(@Param("projectId") Long projectId);

    @Query(value = "SELECT p FROM Project p " +
            "WHERE p.isDeleted = false " +
            "ORDER BY CASE WHEN p.completed = true THEN 1 ELSE 0 END, p.projectId ASC")
    List<Project> checkProjectByUserId();


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
    String checkOwnerProject(@Param("uid") String uid, @Param("projectId") Long projectId);


    @Query("SELECT new com.foxconn.EmployeeManagerment.dto.response.ChartDto( " +
            "c.categoryName, " +
            "COUNT(p.projectId), " +
            "SUM(CASE WHEN p.status.statusId = 'completed' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.statusId ='construction' THEN 1 ELSE 0 END), " +
            "Sum(CASE WHEN p.status.statusId ='acceptance' THEN 1 ELSE 0 END)," +
            "SUM(CASE WHEN p.status.statusId not in  ('completed','acceptance','construction', 'cancelled') THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.statusId = 'cancelled' THEN 1 ELSE 0 END)) " +
            "FROM Category c " +
            "LEFT JOIN c.projects p " +
            "on p.isDeleted = false " +
            "GROUP BY c.categoryId, c.categoryName " +
            "ORDER BY c.categoryName")
    List<ChartDto> getCharts();

    @Query("SELECT new com.foxconn.EmployeeManagerment.dto.response.ChartDto( " +
            "c.categoryName, " +
            "COUNT(p.projectId), " +
            "SUM(CASE WHEN p.status.statusId = 'completed' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.statusId ='construction' THEN 1 ELSE 0 END), " +
            "Sum(CASE WHEN p.status.statusId ='acceptance' THEN 1 ELSE 0 END)," +
            "SUM(CASE WHEN p.status.statusId not in  ('completed','acceptance','construction', 'cancelled') THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.statusId = 'cancelled' THEN 1 ELSE 0 END)) " +
            "FROM Category c " +
            "LEFT JOIN c.projects p " +
            "on p.isDeleted = false " +
            "and p.year  between  :from and :to " +
            "GROUP BY c.categoryId, c.categoryName " +
            "ORDER BY c.categoryName")
    List<ChartDto> getChartFromTo(@Param("from") int from, @Param("to") int to);

    @Query("SELECT new com.foxconn.EmployeeManagerment.dto.response.CategoryCountDTO(p.category.categoryName, COUNT(p)) " +
            "FROM Project p GROUP BY p.category.categoryName")
    List<CategoryCountDTO> getTotal();

    @Query("select new com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted(CAST(p.completed AS string) , count(p.completed)) " +
            "FROM Project p where p.isDeleted = false  " +
            "group by p.completed")
    List<ProjectCompleted> getCompleted();

    @Query("select new com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2("
            + "COUNT(p) ," +
            "COUNT(CASE WHEN p.canceled = true THEN 1 END) ," +
            "COUNT(CASE WHEN p.status.statusId not in ('completed','acceptance','construction', 'cancelled' ) THEN 1 END) ," +
            "COUNT(CASE WHEN p.status.statusId = 'completed' THEN 1 END))" +
            " FROM Project p where " +
            "p.isDeleted = false ")
    List<ProjectCompleted2> getCompleted2();

    @Query("select new com.foxconn.EmployeeManagerment.dto.response.ProjectCompleted2("
            + "COUNT(p) ," +
            "COUNT(CASE WHEN p.canceled = true THEN 1 END) ," +
            "COUNT(CASE WHEN p.status.statusId not in ('completed','acceptance','construction', 'cancelled' ) THEN 1 END) ," +
            "COUNT(CASE WHEN p.status.statusId = 'completed' THEN 1 END))" +
            " FROM Project p where p.year between :from and :to and  p.isDeleted = false ")
    List<ProjectCompleted2> getTotalFromTo(@Param("from") int from, @Param("to") int to);


    @Query("SELECT p.projectId AS projectId, p.projectName AS projectName " +
            "FROM Project p WHERE p.completed = FALSE  " +
            "and p.isDeleted = false ")
    List<ProjectProjection> getProjectName();


    @Query("SELECT p FROM Project p " +
            "WHERE (upper(COALESCE(p.projectName, '')) LIKE %:value% " +
            "OR upper(COALESCE(p.category.categoryName, '')) LIKE %:value% " +
            "OR upper(COALESCE(p.pic.fullName, '')) LIKE %:value% )" +
            "and p.isDeleted = false " +
            "order by p.completed asc")
    List<Project> searchProject(@Param("value") String value);

    @Query("SELECT p FROM Project p WHERE UPPER(COALESCE(p.projectName, '')) LIKE UPPER(CONCAT('%', :projectName, '%')) " +
            "and p.isDeleted = false ")
    List<Project> findByName(@Param("projectName") String projectName);

    @Query("SELECT p FROM Project p WHERE  p.category.categoryId = :categoryId " +
            "AND p.status.statusId = :status " +
            "and p.isDeleted = false ")
    List<Project> getByChart(@Param("categoryId") String categoryId, @Param("status") String status);


    @Query("select p from Project p where p.isDeleted = false ")
    List<Project> findAllProject( );


    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.isDeleted = true, p.userDeleted.uid = :userId WHERE p.projectId = :projectId")
    void deleteProject(@Param("projectId") Long projectId,
                       @Param("userId") String userId);


    @Query("SELECT p FROM Project p WHERE  p.category.categoryId = :categoryId " +
            "AND p.status.statusId = :status " +
            "and p.isDeleted = false and p.year  between  :from and :to ")
    List<Project> getByChartFromTo(@Param("categoryId") String categoryId,
                                   @Param("status") String status,
                                   @Param("from") int from,
                                   @Param("to") int to);

    @Query("select p from Project  p where p.isDeleted = false " +
            "and p.completed = false and p.accepted = true " +
            "and p.status.statusId =:status " +
            "and p.category.categoryId =:categoryId")
    List<Project> getAcceptanceData(@Param("categoryId") String categoryId,
                                    @Param("status") String status);

    @Query("select p from Project  p where p.isDeleted = false " +
            "and p.completed = false and p.accepted = FALSE " +
//            "and p.status.statusId =:status " +
            "and p.category.categoryId =:categoryId")
    List<Project> getInProgressData(@Param("categoryId") String categoryId);

    @Query("select p from Project p where p.accepted = true  " +
            "and p.category.categoryId =:categoryId " +
            "and p.isDeleted = false ")
    List<Project> getAcceptanceDataTotal(@Param("categoryId") String categoryId);

    @Query("select p from Project p where p.accepted = false  " +
            "and p.completed = false  " +
            "and p.isDeleted = false  " +
            "and p.category.categoryId =:categoryId")
    List<Project> getInProgressDataTotal(@Param("categoryId") String categoryId);

    @Query("select p from Project  p where p.isDeleted = false " +
            "and p.completed = false and p.accepted = true " +
            "and p.status.statusId =:status " +
            "and p.category.categoryId =:categoryId " +
            "and p.year  between  :from and :to ")
    List<Project> getAcceptanceDataFromTo(@Param("categoryId") String categoryId,
                                          @Param("status") String status,
                                          @Param("from") int from,
                                          @Param("to") int to);

    @Query("select p from Project  p where p.isDeleted = false " +
            "and p.completed = false and p.accepted = FALSE " +
//            "and p.status.statusId =:status " +
            "and p.category.categoryId =:categoryId " +
            "and p.year  between  :from and :to ")
    List<Project> getInProgressDataFromTo(@Param("categoryId") String categoryId,
//                                          @Param("status") String status,
                                          @Param("from") int from,
                                          @Param("to") int to);

    @Query("select p from Project p where p.accepted = true  " +
            "and p.category.categoryId =:categoryId " +
            "and p.isDeleted = false " +
            "and p.year  between  :from and :to ")
    List<Project> getAcceptanceDataTotalFromTo(@Param("categoryId") String categoryId,
                                               @Param("from") int from,
                                               @Param("to") int to);

    @Query("select p from Project p where p.accepted = false  " +
            "and p.completed = false  " +
            "and p.isDeleted = false  " +
            "and p.category.categoryId =:categoryId " +
            "and p.year  between  :from and :to ")
    List<Project> getInProgressDataTotalFromTo(@Param("categoryId") String categoryId,
                                               @Param("from") int from,
                                               @Param("to") int to);

    @Query(value = "SELECT " +
            "    MIN(EXTRACT(YEAR FROM start_date)) AS minYear," +
            "    MAX(EXTRACT(YEAR FROM start_date)) AS maxYear " +
            "FROM emt.project", nativeQuery = true)
    MinMaxYearProjection getMinMaxYear();
}
