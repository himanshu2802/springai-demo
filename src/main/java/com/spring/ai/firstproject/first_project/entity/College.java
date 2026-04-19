package com.spring.ai.firstproject.first_project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class College {

    private Integer serialNumber;
    private String collegeName;
    private String CourseName;
    private String city;
    private Integer closingRank;
    private String reasonForRecommendation;
}
