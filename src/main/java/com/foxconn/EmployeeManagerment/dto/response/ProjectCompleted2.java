package com.foxconn.EmployeeManagerment.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectCompleted2 {

    private Long total;
    private Long cancelled;
    private Long remain;
    private Long completed;


}

