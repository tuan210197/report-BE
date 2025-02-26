package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileUpload, Long> {
    @Query("SELECT f FROM FileUpload f where f.fileName =:originalFilename")
    FileUpload findByFileName(@Param("originalFilename") String originalFilename);
}
