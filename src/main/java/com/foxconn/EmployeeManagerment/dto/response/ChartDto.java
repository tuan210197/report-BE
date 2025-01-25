package com.foxconn.EmployeeManagerment.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartDto {

    private String categoryName;
    private Long total;
    private Long completed;
    private Long remaining;
    private Long news;

    public ChartDto( String categoryName, Long total, Long completed, Long remaining, Long news) {

        this.categoryName = categoryName;
        this.total = total;
        this.completed = completed;
        this.remaining = remaining;
        this.news = news;

    }
}
