package com.foxconn.EmployeeManagerment.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartDto {

    private String categoryName;
    private Long total;
    private Long completed;
    private Long constructed;
    private Long equip;
    private Long remaining;
    private Long cancelled;

    public ChartDto( String categoryName, Long total,Long completed, Long constructed, Long equip, Long remaining, Long cancelled) {

        this.categoryName = categoryName;
        this.total = total;
        this.completed = completed;
        this.constructed = constructed;
        this.equip = equip;
        this.remaining = remaining;
        this.cancelled = cancelled;

    }
}
