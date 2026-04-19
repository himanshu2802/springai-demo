package com.spring.ai.firstproject.first_project.controller;

import com.spring.ai.firstproject.first_project.service.MemoryLLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/rememberLLM")
public class MemoryLLMController {

    @Autowired
    public MemoryLLMService memoryLLMService;

    @GetMapping
    public String getMemoryLLMResponse(@RequestParam String query,
                                       @RequestHeader (value = "Conversation-ID", required = false) String conversationId) {
        return memoryLLMService.getMemoryLLMResponse(query, conversationId);
    }
}
