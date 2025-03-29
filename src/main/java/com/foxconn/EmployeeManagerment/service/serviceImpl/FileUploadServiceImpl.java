package com.foxconn.EmployeeManagerment.service.serviceImpl;


import com.foxconn.EmployeeManagerment.entity.FileUpload;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.repository.FileRepository;
import com.foxconn.EmployeeManagerment.repository.ProjectRepository;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import com.foxconn.EmployeeManagerment.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service

public class FileUploadServiceImpl implements FileUploadService {

    private final UserRepository userRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;

    public FileUploadServiceImpl(FileRepository fileRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean uploadFile(MultipartFile file, String uid, Long projectId) {

        FileUpload fileUpload = fileRepository.findByFileName(file.getOriginalFilename());
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        if (Objects.isNull(fileUpload)) {
            try {
                Users userUpload = userRepository.findByUid(uid);
                Path filePath = Paths.get(uploadDir).resolve(Objects.requireNonNull(file.getOriginalFilename())).normalize();


                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                FileUpload fileEntity = FileUpload.builder()
                        .fileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .filePath(filePath.toString())
                        .uploadTime(LocalDateTime.now())
                        .projectId(project)
                        .userUploads(userUpload)
                        .build();

                fileRepository.save(fileEntity);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("CAN NOT SAVE FILE: " + e.getMessage(), e);
            }
        } else {
            return false;
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Xác định kiểu MIME của file
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Kiểu mặc định nếu không xác định được
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // Set kiểu file đúng
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"") // Bắt buộc tải về
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public List<FileUpload> findByProjectId(Long projectId) {
        return fileRepository.findByProjectId(projectId);
    }
}
