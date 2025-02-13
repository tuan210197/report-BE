package com.foxconn.EmployeeManagerment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateDTO {
    private String status;
    private Long projectId;
}
