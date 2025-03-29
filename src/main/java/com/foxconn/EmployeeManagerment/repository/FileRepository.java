package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.FileUpload;
import com.foxconn.EmployeeManagerment.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileUpload, Long> {
    @Query("SELECT f FROM FileUpload f where f.fileName =:originalFilename")
    FileUpload findByFileName(@Param("originalFilename") String originalFilename);


    @Query("SELECT f FROM FileUpload f WHERE f.projectId.projectId = :projectId")
    List<FileUpload> findByProjectId(@Param("projectId") Long projectId);

//    Page<FileUpload> findByProjectId(Long projectId, Pageable pageable);
}
