package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.ImplementDto;
import com.foxconn.EmployeeManagerment.entity.Implement;
import com.foxconn.EmployeeManagerment.projection.ImplementProjection;

import java.util.List;

public interface ImplementService {
    Boolean createImplement(ImplementDto implement, String userImplement);

    Implement updateImplement(Long id, Implement implement);

    void deleteImplement(Long id);

    List<ImplementProjection> getImplementById(Long id);

    List<Implement> getAllImplements();

    List<Implement> getImplementsByUserImplement(String userImplement);

    String getImplementByProject(Long projectId);
//    List<Implement> getImplementsByProjectId(Long projectId);
}
