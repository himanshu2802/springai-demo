package com.spring.ai.firstproject.first_project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tutorial {

    private String title;
    private String content;
    private Integer createdYear;
}
