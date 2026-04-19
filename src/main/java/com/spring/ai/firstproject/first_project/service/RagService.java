package com.spring.ai.firstproject.first_project.service;


import java.util.List;

public interface RagService {

    void saveRagData(List<String> data);

    String getDataFromRAG(String query);

    String getDataFromRagAdvisor(String query);

    String getDataFromRagPipeline(String query);

    String readFiles();
}
