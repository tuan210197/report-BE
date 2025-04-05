package com.foxconn.EmployeeManagerment.projection;

import java.time.LocalDateTime;

public interface DetailReportProjection {
    String getReporterName();
    String getProjectName();
    String getCategoryName();
    String getAddress();
    String getContractor();
    String getRequester();
    String getImplement();
    LocalDateTime getCreatedAt();
}
