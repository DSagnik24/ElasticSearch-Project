package com.Project.New_Proj.dto;

import lombok.Data;

@Data
public class CourseSearchRequest {
    private String keyword;
    private String category;
    private String type;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private int page = 0;
    private int size = 10;
}
