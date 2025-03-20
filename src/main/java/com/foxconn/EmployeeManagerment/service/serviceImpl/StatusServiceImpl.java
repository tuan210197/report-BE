package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.entity.Status;
import com.foxconn.EmployeeManagerment.repository.StatusRepository;
import com.foxconn.EmployeeManagerment.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl  implements StatusService {
    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public List<Status> getStatus() {
        return statusRepository.getAll();
    }
}
