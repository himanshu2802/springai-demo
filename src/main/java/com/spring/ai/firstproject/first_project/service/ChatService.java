package com.spring.ai.firstproject.first_project.service;

import com.spring.ai.firstproject.first_project.entity.College;
import com.spring.ai.firstproject.first_project.entity.Tutorial;

import java.util.List;

public interface ChatService {

    public String chat(String query);

    public String expertChat(String query);

    public Tutorial entityChat(String query);

    public List<College> getCollegesList(String query);

    public String getPromptResponse(String input);
}
