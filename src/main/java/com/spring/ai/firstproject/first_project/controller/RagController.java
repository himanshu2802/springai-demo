package com.spring.ai.firstproject.first_project.controller;

import com.spring.ai.firstproject.first_project.helper.Helper;
import com.spring.ai.firstproject.first_project.service.MemoryLLMService;
import com.spring.ai.firstproject.first_project.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/rag")
public class RagController {

    @Autowired
    public RagService ragService;

    @GetMapping
    public void saveRagData() {
        ragService.saveRagData(Helper.getDate());
    }

    @GetMapping ("/rag-data")
    public ResponseEntity<String> getDataFromRAG(@RequestParam String query) {
        ResponseEntity response = ResponseEntity.ok(ragService.getDataFromRAG(query));
         return response;
    }

    @GetMapping ("/advisor-rag-data")
    public ResponseEntity<String> getDataFromRagAdvisor(@RequestParam String query) {
        ResponseEntity response = ResponseEntity.ok(ragService.getDataFromRagAdvisor(query));
        return response;
    }

    @GetMapping ("/rag-pipeline")
    public ResponseEntity<String> getDataFromRagPipeline(@RequestParam String query) {
        ResponseEntity response = ResponseEntity.ok(ragService.getDataFromRagPipeline(query));
        return response;
    }

    @GetMapping ("/rag-etl")
    public ResponseEntity<String> readFiles() {
        ResponseEntity response = ResponseEntity.ok(ragService.readFiles());
        return response;
    }
}
