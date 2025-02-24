package com.foxconn.EmployeeManagerment.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUploadService {
    boolean uploadFile(MultipartFile file, String uid, Long projectId);

    ResponseEntity<Resource> downloadFile(String fileName);
}
