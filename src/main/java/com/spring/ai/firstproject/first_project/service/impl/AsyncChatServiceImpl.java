package com.spring.ai.firstproject.first_project.service.impl;

import com.spring.ai.firstproject.first_project.service.AsyncChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AsyncChatServiceImpl implements AsyncChatService {

    private ChatClient openAIClient;
    private ChatClient genAIClient;

    public AsyncChatServiceImpl(@Autowired @Qualifier("openAIClient") ChatClient openAIClient,
                                   @Autowired @Qualifier("geminiAIClient") ChatClient genAIClient) {
        this.openAIClient = openAIClient;
        this.genAIClient = genAIClient;
    }

    @Override
    public Flux<String> getAsyncChatResponse(String query) {

        return openAIClient.prompt()
                .system("You are an expert in career counseling. Answer the question in detail.")
                .user(query)
                .stream().content();
    }
}
