package com.foxconn.EmployeeManagerment.projection;

import java.time.LocalDate;

public interface ProjectProjection {
    Long getProjectId();
    String getProjectName();
    String getProjectDescription();
    LocalDate getStartDate();
}
