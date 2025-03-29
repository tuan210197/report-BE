package com.foxconn.EmployeeManagerment.controller;
import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.FileUploadDTO;
import com.foxconn.EmployeeManagerment.dto.request.FilesDTO;
import com.foxconn.EmployeeManagerment.entity.FileUpload;
import com.foxconn.EmployeeManagerment.repository.FileRepository;
import com.foxconn.EmployeeManagerment.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




import java.nio.file.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController extends BaseController {



    private final FileRepository fileRepository;
    private final FileUploadService fileUploadService;

    public FileController(FileRepository fileRepository, FileUploadService fileUploadService) {
        this.fileRepository = fileRepository;
        this.fileUploadService = fileUploadService;
    }

    // ✅ API UPLOAD FILE
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestPart("fileUploadDTO") FileUploadDTO fileUploadDTO) {
        try {
            String uid = this.getCurrentUser().getUid().trim();
            if (fileUploadService.uploadFile(file, uid, fileUploadDTO.getProjectId())) {
                return toSuccessResult(null, "SUCCESS");
            } else {
                return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        }catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    // ✅ API LẤY DANH SÁCH FILE TỪ DATABASE
    @GetMapping("/list")
    public ResponseEntity<List<Map<String, String>>> listFiles() {
        List<FileUpload> fileEntities = fileRepository.findAll();
        List<Map<String, String>> files = new ArrayList<>();

        for (FileUpload file : fileEntities) {
            Map<String, String> fileData = new HashMap<>();
            fileData.put("name", file.getFileName());
            fileData.put("downloadUrl", "http://localhost:8080/api/files/download/" + file.getFileName());
            files.add(fileData);
        }
        return ResponseEntity.ok(files);
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok(fileRepository.findAll());
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource>  downloadFile(@PathVariable String fileName) {
        return fileUploadService.downloadFile(fileName);
    }

    @GetMapping("/get-by-project/{projectId}")
    public ResponseEntity<?> getFileByProject( @PathVariable Long projectId) {

        List<FileUpload> file = fileUploadService.findByProjectId(projectId);
        if (file.isEmpty()) {
            return toSuccessResult(null, "NO DATA");
        }else {
            return toSuccessResult(file, "SUCCESS");
        }
    }


}
