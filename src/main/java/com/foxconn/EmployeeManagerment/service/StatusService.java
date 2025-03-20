package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatusService {
    List<Status> getStatus();
}
