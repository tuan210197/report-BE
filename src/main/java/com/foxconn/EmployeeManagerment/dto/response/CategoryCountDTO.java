package com.foxconn.EmployeeManagerment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCountDTO {
    private String category;
    private Long count;

    public CategoryCountDTO(String category, Long count) {
        this.category = category;
        this.count = count;
    }
}