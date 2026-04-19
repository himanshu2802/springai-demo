package com.spring.ai.firstproject.first_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollegeResponse {
    private String collegeName;
    private String location;
    private String course;
    private int cutOff;
    private int likelihood;
    private String remarks;
}
