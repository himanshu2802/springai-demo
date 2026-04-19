package com.spring.ai.firstproject.first_project.controller;

import com.spring.ai.firstproject.first_project.service.AsyncChatService;
import com.spring.ai.firstproject.first_project.service.ChatTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping ("/asyncChat")
public class AsyncChatController {

    @Autowired
    public AsyncChatService asyncChatService;

    @GetMapping("stream-chat")
    public ResponseEntity<Flux<String>> getAsyncChatResponse(String query) {
        Flux<String> res = asyncChatService.getAsyncChatResponse(query);
        return ResponseEntity.ok().body(res);
    }
}
