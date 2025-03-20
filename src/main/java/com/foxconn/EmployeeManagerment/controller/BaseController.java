package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.entity.BasicLogin;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.exception.ApplicationException;
import com.foxconn.EmployeeManagerment.message.ResponseMessage;
import com.foxconn.EmployeeManagerment.repository.BasicLoginRepository;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;


/**
 * CommonController
 *
 * @author tuandv
 * @version 1.0
 * @since 02/10/2021
 */
public abstract class BaseController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    BasicLoginRepository basicLoginRepo;

    protected <T> ResponseEntity<?> toSuccessResult(T data, String successMessage) {
        ResponseMessage<T> message = new ResponseMessage<>();

        message.setCode(Const.API_RESPONSE.RETURN_CODE_SUCCESS + "");
        message.setSuccess(Const.API_RESPONSE.RESPONSE_STATUS_TRUE);
        message.setMessage(successMessage);
        message.setData(data);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> toExceptionResult(String errorMessage, int code) {
        ResponseMessage<T> message = new ResponseMessage<>();
        message.setSuccess(Const.API_RESPONSE.RESPONSE_STATUS_FALSE);
        message.setCode(code + "");
        message.setMessage(errorMessage);

        return new ResponseEntity<>(message, HttpStatus.valueOf(code));
    }

    public Users getCurrentUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        Users users = userRepo.findByUid(basicLogin.getUserUid());
        if (users == null) {
            throw new ApplicationException("Users is null");
        }
        return users;
    }
}