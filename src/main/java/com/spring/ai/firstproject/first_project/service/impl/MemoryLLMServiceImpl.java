package com.spring.ai.firstproject.first_project.service.impl;

import com.spring.ai.firstproject.first_project.service.MemoryLLMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemoryLLMServiceImpl implements MemoryLLMService {

    private ChatClient openAIClient;

    public MemoryLLMServiceImpl(@Autowired @org.springframework.beans.factory.annotation.Qualifier("openAIClient") ChatClient openAIClient
                          /*  ,@Autowired @org.springframework.beans.factory.annotation.Qualifier("azureAIClient") ChatClient azureClient,
                          @Autowired @org.springframework.beans.factory.annotation.Qualifier("ollamaAIClient") ChatClient ollamaClient*/
    ) {
        this.openAIClient = openAIClient;
       /* this.azureClient = azureClient;
        this.ollamaClient = ollamaClient;*/
    }


    @Override
    public String getMemoryLLMResponse(String query, String conversationId) {
        return openAIClient.prompt(query)
                // Pass the conversation ID to the advisor so that it can use it to store and retrieve conversation history
                // The advisor can use the conversation ID to associate the conversation history with the correct user or session
                // This allows the application to maintain context across multiple users , so that each user can have their own conversation history without interference from other users
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
