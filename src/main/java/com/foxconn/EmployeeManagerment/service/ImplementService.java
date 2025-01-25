package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.ImplementDto;
import com.foxconn.EmployeeManagerment.entity.Implement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImplementService {
    Boolean createImplement(ImplementDto implement, String userImplement);
    Implement updateImplement(Long id, Implement implement);
    void deleteImplement(Long id);
    List<Implement> getImplementById(Long id);
    List<Implement> getAllImplements();
    List<Implement> getImplementsByUserImplement(String userImplement);
//    List<Implement> getImplementsByProjectId(Long projectId);
}
