package com.foxconn.EmployeeManagerment.projection;

import java.time.LocalDate;

public interface CalendarProjection {

    Long getId();
    String getTitle();
    LocalDate getDate();
}
