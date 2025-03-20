package com.foxconn.EmployeeManagerment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FromToDTO {
    private int from;
    private int to;
    private String type;
    private String status;
    private String categoryName;
}
