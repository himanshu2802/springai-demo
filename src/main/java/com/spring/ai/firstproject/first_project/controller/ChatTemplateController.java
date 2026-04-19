package com.spring.ai.firstproject.first_project.controller;

import com.spring.ai.firstproject.first_project.model.StudentDetailModel;
import com.spring.ai.firstproject.first_project.service.ChatTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatTemplate")
public class ChatTemplateController {

    @Autowired
    public ChatTemplateService chatTemplateService;

    @GetMapping
    public String getChatTemplate(String query) {
        return chatTemplateService.getChatTemplate(query);
    }

    @GetMapping("/useRolePrompt")
    public String getRolePromptResponse(String query) {
        return chatTemplateService.getRolePromptResponse(query);
    }

    @PostMapping("/getFromResource")
    public ResponseEntity<byte[]> getFromResource(@RequestBody StudentDetailModel model) {

        byte[] docBytes = chatTemplateService.getFromResource(model);

        String fileName = "Candidate_College_List_"
                + model.getStudentName().replaceAll("\\s+", "_")
                + ".docx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(docBytes.length)
                .body(docBytes);
    }
}
