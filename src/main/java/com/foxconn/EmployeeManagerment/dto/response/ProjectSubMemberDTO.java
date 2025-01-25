package com.foxconn.EmployeeManagerment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSubMemberDTO {
    private Long projectId;
    private String projectName;
    private String userId;
}
