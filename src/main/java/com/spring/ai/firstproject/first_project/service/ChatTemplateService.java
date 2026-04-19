package com.spring.ai.firstproject.first_project.service;

import com.spring.ai.firstproject.first_project.model.StudentDetailModel;

public interface ChatTemplateService {

    public String getChatTemplate(String query);

    String getRolePromptResponse(String query);

    byte[] getFromResource(StudentDetailModel model);
}
