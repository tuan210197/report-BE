package com.foxconn.EmployeeManagerment.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    private String uid;
    private String employeeCode;
    private String fullName;
    private Integer isActive;
    private Integer isDeleted;
    private String avatar;
    private Integer gender;
    private LocalDate birthday;
    private String mobile;
    private String projectId;
    private Long role;
    private String department;



}