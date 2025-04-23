package com.foxconn.EmployeeManagerment.projection;

import java.time.LocalDateTime;

public interface ImplementProjection {

    LocalDateTime getCreateAt();

    String getImplement();

    String getProjectName();

    String getCategory();

    String getFullName();
}
