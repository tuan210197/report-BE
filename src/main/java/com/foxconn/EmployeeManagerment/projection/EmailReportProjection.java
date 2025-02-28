package com.foxconn.EmployeeManagerment.projection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;


public interface EmailReportProjection {
    Long getUid();
    String getFullName();
    String getReport();
}
