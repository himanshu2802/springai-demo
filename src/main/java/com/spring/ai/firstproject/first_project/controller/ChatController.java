package com.spring.ai.firstproject.first_project.controller;

import com.spring.ai.firstproject.first_project.entity.College;
import com.spring.ai.firstproject.first_project.entity.Tutorial;
import com.spring.ai.firstproject.first_project.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    
    @GetMapping ("/simpleChat")
    public String helloChat(@RequestParam (value = "input") String input) {

        String response= chatService.chat(input);
        return response;
    }

    @GetMapping ("/expertChat")
    public String expertChant(@RequestParam (value = "input") String input) {

        String response= chatService.expertChat(input);
        return response;
    }

    @GetMapping ("/getEntityChat")
    public Tutorial getEntityChat(@RequestParam (value = "input") String input) {

        Tutorial response= chatService.entityChat(input);
        return response;
    }

    @GetMapping ("/getCollegeList")
    public List<College> getCollegeList(@RequestParam (value = "input") String input) {

        List<College> response= chatService.getCollegesList(input);
        return response;
    }

    @GetMapping ("/getPromptResponse")
    public String getPromptResponse(@RequestParam (value = "input") String input) {

        String response= chatService.getPromptResponse(input);
        return response;
    }

    /*@GetMapping ("/helloOllamaAI")
    public String helloOllamaChat(@RequestParam (value = "input") String input) {

        return ollamaClient.prompt(input).call().content();
    }*/
}
