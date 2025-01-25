package com.foxconn.EmployeeManagerment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubMemberDto {

    private Long projectId;
    private String pic;
    private String fullName;
    private String userId;


}
