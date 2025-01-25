package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<Users> getAll();
}
