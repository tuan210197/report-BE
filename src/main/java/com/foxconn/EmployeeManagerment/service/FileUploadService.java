package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.FileUpload;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FileUploadService {
    boolean uploadFile(MultipartFile file, String uid, Long projectId);

    ResponseEntity<Resource> downloadFile(String fileName);

    List<FileUpload> findByProjectId(Long projectId);
}
