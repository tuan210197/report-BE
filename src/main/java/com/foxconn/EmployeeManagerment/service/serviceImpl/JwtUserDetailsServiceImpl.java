package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.controller.BaseController;
import com.foxconn.EmployeeManagerment.dto.request.UserInfoDTO;
import com.foxconn.EmployeeManagerment.dto.request.UserLoginDTO;
import com.foxconn.EmployeeManagerment.entity.BasicLogin;
import com.foxconn.EmployeeManagerment.entity.Department;
import com.foxconn.EmployeeManagerment.entity.Role;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.exception.ValidateUtil;
import com.foxconn.EmployeeManagerment.repository.BasicLoginRepository;
import com.foxconn.EmployeeManagerment.repository.DepartmentRepository;
import com.foxconn.EmployeeManagerment.repository.RoleRepository;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import com.foxconn.EmployeeManagerment.service.JwtUserDetailsService;
import com.foxconn.EmployeeManagerment.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional
//@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl extends BaseController implements JwtUserDetailsService {

    @Lazy
    private final BasicLoginRepository basicLoginRepo;

    private final UserRepository userRepo;

    private final PasswordEncoder bcryptEncoder;

    private final MailSenderService mailSenderService;

    private final DepartmentRepository departmentRepository;

    private final RoleRepository roleRepository;

    public JwtUserDetailsServiceImpl(BasicLoginRepository basicLoginRepo, UserRepository userRepo, PasswordEncoder bcryptEncoder, MailSenderService mailSenderService, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        this.basicLoginRepo = basicLoginRepo;
        this.userRepo = userRepo;
        this.bcryptEncoder = bcryptEncoder;
        this.mailSenderService = mailSenderService;

        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BasicLogin basicLogin = validateUserAuthen(email);
        String uid = basicLogin.getUserUid();
        Users users = userRepo.findByUid(uid);
        Role role = roleRepository.search(users.getRole().getRole_id());
        String roles = role.getName();

        return User.withUsername(basicLogin.getEmail()).password(basicLogin.getPassword()).authorities(roles).build();

    }

    @Override
    public UserLoginDTO getBasicAuthByEmail(String email, boolean forceClear) {
        BasicLogin basicLogin = validateUserAuthen(email);
        if (basicLogin.getIsVerified().equals(Const.COMMON_CONST_VALUE.NOT_VERIFIED)) {
            log.info("User not verify. Resend OTP for verifying");
            UserLoginDTO userLoginDTO = UserLoginDTO.builder().email(email).build();
            resendOTP(userLoginDTO);
            return null;
        }
        Users user = userRepo.findByUid(basicLogin.getUserUid());
        Assert.isTrue(user.getIsActive().equals(Const.COMMON_CONST_VALUE.ACTIVE), "USER_NOT_ACTIVE");
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        BeanUtils.copyProperties(basicLogin, userLoginDTO);
        return userLoginDTO;
    }


    private BasicLogin validateUserAuthen(String email) {

        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        Assert.isTrue(basicLogin.getRetryCount() < Const.RETRY_TIMES, "USER_LOCKED");
        return basicLogin;
    }

    @Override
    public boolean checkEmail(UserLoginDTO user) {
        Assert.hasText(user.getEmail(), "EMAIL_EMPTY");
        Assert.isTrue(ValidateUtil.regexValidation(user.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
        BasicLogin checkBasicLogin = basicLoginRepo.findByEmail(user.getEmail());
        if (checkBasicLogin != null) {

            int check = checkBasicLogin.getIsVerified();
            return check == 1;
        } else {
            return false;
        }
    }



    @Override
    public boolean banUser(UserLoginDTO userLoginDTO) {

        String email = userLoginDTO.getEmail();
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        String uid = basicLogin.getUserUid();

        if (uid != null) {
            Users users = userRepo.findByUid(uid);
            users.setIsActive(0);
            userRepo.save(users);
            return true;

        } else {
            return false;
        }

    }

    @Override
    public boolean unlockUser(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        String uid = basicLogin.getUserUid();

        if (uid != null) {
            Users users = userRepo.findByUid(uid);
            users.setIsActive(1);
            basicLogin.setRetryCount(0);
            basicLoginRepo.save(basicLogin);
            userRepo.save(users);
            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean checkPassword(UserLoginDTO userLoginDTO) {
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        return bcryptEncoder.matches(userLoginDTO.getPassword(), basicLogin.getPassword());
    }


    @Override
    public boolean registerUser(UserLoginDTO user)  {
        Assert.hasText(user.getEmail(), "EMAIL_EMPTY");
        Assert.isTrue(ValidateUtil.regexValidation(user.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
        BasicLogin checkBasicLogin = basicLoginRepo.findByEmail(user.getEmail());
        Assert.isNull(checkBasicLogin, "EMAIL_REGISTERED");
        String deptId = user.getDeptId();

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Role role = roleRepository.search(user.getRoles());

        Users users = Users.builder()
                .avatar(user.getAvatar())
                .birthday(user.getBirthday())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .isActive(Const.COMMON_CONST_VALUE.ACTIVE)
                .isDeleted(Const.COMMON_CONST_VALUE.NOT_DELETED)
                .mobile(user.getMobile())
                .role(role)
                .department(department)
                .email(user.getEmail())
                .employeeCode(user.getEmployeeCode())
                .build();

        users = userRepo.save(users);

//        String pass = generatePassword(10);
        BasicLogin basicLogin = BasicLogin.builder()
                .email(user.getEmail())
                .password(bcryptEncoder.encode(generatePassword(10)))
                .isVerified(Const.COMMON_CONST_VALUE.NOT_VERIFIED)
                .userUid(users.getUid())
                .role(users.getRole().getName())
                .isForgot(false)
                .build();
        basicLogin = basicLoginRepo.save(basicLogin);

        sendOTP(basicLogin, "OTP Token for Register Account");
        sendPassword(basicLogin, "PASSWORD FOR YOUR EMAIL");

        return Objects.nonNull(users.getUid());
    }

    //send OTP to mail when change password or forgot password
    @Override
    public void forgetPassword(UserLoginDTO userLoginDTO) {
        Assert.hasText(userLoginDTO.getEmail(), "EMAIL_EMPTY");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        Assert.notNull(basicLogin, "EMAIL_NOT_EXIST");
        basicLogin.setIsForgot(true);
        basicLoginRepo.save(basicLogin);
        sendOTP(basicLogin, "OTP Token to Forget Password");

    }

    public void sendOTP(BasicLogin basicLogin, String subject) {
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        String otpToken = generateOTPToken();
        String password = generatePassword(10);
//        basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.NOT_VERIFIED);
        basicLogin.setTokenCode(otpToken);
//        basicLogin.setPassword(bcryptEncoder.encode(password));
        basicLogin.setExpireDate(LocalDateTime.now().plusMinutes(15));
        basicLogin.setRetryCount(0);
        basicLoginRepo.save(basicLogin);
        mailSenderService.sendSimpleMessage(basicLogin.getEmail(), subject, "Your OTP Reset Password is: " + otpToken );
    }

    public void sendPassword(BasicLogin basicLogin, String subject) {
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        String password = generatePassword(10);
        basicLogin.setPassword(bcryptEncoder.encode(password));
        basicLoginRepo.save(basicLogin);
        mailSenderService.sendSimpleMessage(basicLogin.getEmail(), subject, "Your Password is: " + password);
    }

    // update password when change password or forgot password
    @Override
    public boolean updatePassword(UserLoginDTO userLoginDTO) {

        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        Assert.notNull(basicLogin, "EMAIL_NOT_FOUND");
        Assert.hasText(userLoginDTO.getNewPassword(), "NEW_PASSWORD_NOT_FOUND");
        Assert.hasText(userLoginDTO.getReNewPassword(), "CONFIRM_PASSWORD_NOT_FOUND");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getNewPassword(), Const.VALIDATE_INPUT.regexPass), "NEW_PASS_WRONG_FORMAT");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getReNewPassword(), Const.VALIDATE_INPUT.regexPass), "CONFIRM_PASS_WRONG_FORMAT");

        if (basicLogin.getIsForgot()) {
            Assert.hasText(userLoginDTO.getTokenCode(), "OTP_NOT_FOUND");
            Assert.isTrue(basicLogin.getExpireDate().isAfter(LocalDateTime.now()), "TOKEN_EXPIRED");
            Assert.isTrue(basicLogin.getRetryCount() < Const.RETRY_TIMES, "TRIED_TOO_MANY_TIMES");

            if (userLoginDTO.getTokenCode().equals(basicLogin.getTokenCode())) {
                basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.VERIFIED);
                basicLogin.setIsForgot(false);
                basicLogin.setPassword(bcryptEncoder.encode(userLoginDTO.getNewPassword()));
                basicLoginRepo.save(basicLogin);
            } else {
                int retry = basicLogin.getRetryCount();
                basicLogin.setRetryCount(++retry);

                return false;
            }
        } else {
            Assert.hasText(userLoginDTO.getPassword(), "PASSWORD_NOT_FOUND");
            Assert.isTrue(bcryptEncoder.matches(userLoginDTO.getPassword(), basicLogin.getPassword()), "PASSWORD_NOT_MATCH");
            Assert.isTrue(userLoginDTO.getNewPassword().equals(userLoginDTO.getReNewPassword()), "NEW_RETYPE_PASSWORD_NOT_MATCH");
            basicLogin.setPassword(bcryptEncoder.encode(userLoginDTO.getNewPassword()));
            basicLoginRepo.save(basicLogin);
            return true;
        }
        Assert.isTrue(userLoginDTO.getNewPassword().equals(userLoginDTO.getReNewPassword()), "NEW_RETYPE_PASSWORD_NOT_MATCH");
        basicLogin.setPassword(bcryptEncoder.encode(userLoginDTO.getNewPassword()));
        basicLoginRepo.save(basicLogin);
        return true;
    }

    //Update profile user
    @Override
    public boolean updateUser(String userId, UserLoginDTO userLoginDTO) {
        Users userCheck = userRepo.findByUid(userId);
        Assert.notNull(userCheck, "USER_NOT_FOUND");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getMobile(), Const.VALIDATE_INPUT.regexPhone), "PHONE_WRONG_FORMAT");
        Assert.isTrue(Const.VALIDATE_INPUT.phoneNum.contains(userLoginDTO.getMobile().substring(0, 3)), "PHONE_NOT_VALID");
        Assert.isTrue(userCheck.getIsActive().equals(1), "USER_NOT_ACTIVE");
        userCheck.setBirthday(userLoginDTO.getBirthday());
        userCheck.setFullName(userLoginDTO.getFullName());
        userCheck.setGender(userLoginDTO.getGender());
        userCheck.setMobile(userLoginDTO.getMobile());
        userCheck.setEmployeeCode(userLoginDTO.getEmployeeCode());

        userRepo.save(userCheck);
        return true;
    }

    public String generateOTPToken() {
        String otpToken;
        int minNum = 100000;
        int maxNum = 999999;
        int randomNumber = (int) (Math.random() * (maxNum - minNum + 1) + minNum);
        otpToken = randomNumber + "";
        return otpToken;
    }

    public static String generatePassword(int len) {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }


    @Override
    public boolean checkTokenForUser(UserLoginDTO userLoginDTO) {
        Assert.hasText(userLoginDTO.getEmail(), "EMAIL_EMPTY");
        Assert.hasText(userLoginDTO.getTokenCode(), "OTP_NOT_FOUND");
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        Assert.isTrue(basicLogin.getIsVerified().equals(0), "USER_VERIFIED");
        Assert.isTrue(LocalDateTime.now().isBefore(basicLogin.getExpireDate()), "TOKEN_EXPIRED");
        Assert.isTrue(basicLogin.getRetryCount() < Const.RETRY_TIMES, "TRIED_TOO_MANY_TIMES");
        if (userLoginDTO.getTokenCode().equals(basicLogin.getTokenCode())) {
            basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.VERIFIED);
            basicLogin.setRetryCount(0);
            basicLoginRepo.save(basicLogin);
            return true;
        } else {
            int retry = basicLogin.getRetryCount();
            basicLogin.setRetryCount(++retry);
            basicLoginRepo.save(basicLogin);
            return false;
        }
    }

    @Override
    public boolean isLocked(String email) {
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        return basicLogin.getRetryCount() < Const.RETRY_TIMES;
    }

    @Override
    public void resendOTP(UserLoginDTO userLoginDTO) {
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        Assert.isTrue(basicLogin.getIsVerified().equals(0), "USER_VERIFIED");
        sendOTP(basicLogin, "OTP Token for Register Account");
    }

    @Override
    public void loginFailRetryCount(String email, boolean loginFailed) {
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        if (Objects.nonNull(basicLogin)) {
            int retryCount = loginFailed ? basicLogin.getRetryCount() + 1 : 0;
            basicLogin.setRetryCount(retryCount);
            basicLoginRepo.save(basicLogin);
        }
    }

    @Override
    public UserInfoDTO getUserInfo(String userUid) {
        Users user = userRepo.findByUid(userUid);
        Assert.notNull(user, "USER_NOT_FOUND");
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        BasicLogin basicLogin = basicLoginRepo.findByUserUid(userUid);
        Optional<Department> dept = departmentRepository.findById(user.getDepartment().getDeptId());

        userInfoDTO.setEmail(basicLogin.getEmail());
        userInfoDTO.setRoles(basicLogin.getRole());
        userInfoDTO.setUid(basicLogin.getUserUid());
        userInfoDTO.setDeptCode(dept.get().getDeptName());
        return userInfoDTO;
    }

    @Override
    public boolean logout(String token) {
        return true;
    }


}