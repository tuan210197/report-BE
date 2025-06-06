package com.foxconn.EmployeeManagerment.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DailyReportProjection {
    String getFullName();
    String getRequester();
    String getProjectName();
    String getCategoryName();
    String getAddress();
    String getContractor();
    Integer getNumberWorker();
//    Integer getProgress();
    Integer getQuantity();
    Integer getQuantityCompleted();
    Integer getQuantityRemain();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getImplement();
    LocalDateTime getCreateAt();


}
