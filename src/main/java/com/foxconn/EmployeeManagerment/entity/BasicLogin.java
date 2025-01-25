package com.foxconn.EmployeeManagerment.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name="basic_login")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasicLogin {
    @Id
    @Column(name="user_uid")
    private String userUid;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="token_code")
    private String tokenCode;
    @Column(name="role")
    private String role;
    @Column(name="expire_date")
    private LocalDateTime expireDate;
    @Column(name="is_verified")
    private Integer isVerified;
    @Column(name="retry_count")
    private Integer retryCount;
    @Column(name = "is_forgot")
    private Boolean isForgot;


}