package com.foxconn.EmployeeManagerment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;
    @Column(name = "file_name", unique = true, nullable = false)
    private String fileName;
    @Column(name = "file_patt")
    private String filePath;
    @Column(name = "file_size")
    private long fileSize;
    @Column(name = "upload_at")
    private LocalDateTime uploadTime;

    @ManyToOne
    @JoinColumn(name = "user_upload")
    private Users userUploads;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projectId;

}
