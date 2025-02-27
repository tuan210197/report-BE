package com.foxconn.EmployeeManagerment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.UserInfoDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@SqlResultSetMapping(
        name = Const.ResultSetMapping.USER_INFO_DTO,
        classes = {
                @ConstructorResult(
                        targetClass = UserInfoDTO.class,
                        columns = {
                                @ColumnResult(name = "uid", type = String.class),
                                @ColumnResult(name = "isActive", type = Integer.class),
                                @ColumnResult(name = "isDeleted", type = Integer.class),
                                @ColumnResult(name = "email", type = String.class),
                                @ColumnResult(name = "avatar", type = String.class),
                                @ColumnResult(name = "birthday", type = LocalDate.class),
                                @ColumnResult(name = "fullName", type = String.class),
                                @ColumnResult(name = "gender", type = Integer.class),
                                @ColumnResult(name = "mobile", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                        }
                )
        }
)
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class Users {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uid", columnDefinition = "UUID")
    private String uid;
    @Column(name = "employee_code")
    private String employeeCode;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "is_deleted")
    private Integer isDeleted;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "status_update")
    private Integer statusUpdate;
    @Column(name = "email")
    private String email;
    @Column(name = "is_report")
    private boolean isReport;
    @Column(name = "is_receive")
    private boolean isReceive;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DailyReport> reports;

    @JsonIgnore
    @OneToMany(mappedBy = "pic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

//    @JsonIgnore
//    @OneToMany(mappedBy = "project_delete", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Project> project;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Implement> userImplements;

    @JsonIgnore
    @OneToMany(mappedBy = "userUploads", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileUpload> userUploads;

}
