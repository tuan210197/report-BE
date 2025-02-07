package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.ImplementDto;
import com.foxconn.EmployeeManagerment.entity.Implement;

import java.util.List;

public interface ImplementService {
    Boolean createImplement(ImplementDto implement, String userImplement);
    Implement updateImplement(Long id, Implement implement);
    void deleteImplement(Long id);
    List<Implement> getImplementById(Long id);
    List<Implement> getAllImplements();
    List<Implement> getImplementsByUserImplement(String userImplement);
//    List<Implement> getImplementsByProjectId(Long projectId);
}
