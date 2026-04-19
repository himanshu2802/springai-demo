package com.spring.ai.firstproject.first_project.service.impl;

import com.spring.ai.firstproject.first_project.entity.College;
import com.spring.ai.firstproject.first_project.entity.Tutorial;
import com.spring.ai.firstproject.first_project.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.spring.ai.firstproject.first_project.constants.Constant.LANGUAGE_JAVA;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private ChatClient openAIClient;
    /*private ChatClient azureClient;
    private ChatClient ollamaClient;*/

    public ChatServiceImpl(@Autowired @org.springframework.beans.factory.annotation.Qualifier("openAIClient") ChatClient openAIClient
                          /*  ,@Autowired @org.springframework.beans.factory.annotation.Qualifier("azureAIClient") ChatClient azureClient,
                          @Autowired @org.springframework.beans.factory.annotation.Qualifier("ollamaAIClient") ChatClient ollamaClient*/
    ) {
        this.openAIClient = openAIClient;
       /* this.azureClient = azureClient;
        this.ollamaClient = ollamaClient;*/
    }

    @Override
    public String chat(String query) {
        return openAIClient.prompt(query).call().content();
    }

    @Override
    public String expertChat(String query) {
        ChatResponse chatResponse = openAIClient.prompt()
                .user(query)
                .system("You are an expert in career counseling. Answer the question in detail.")
                .call()
                .chatResponse();


        log.info("Expert Chat Response Metadata: {}", chatResponse.getMetadata());
        return chatResponse
                .getResult()
                .getOutput()
                .getText();
    }

    @Override
    public Tutorial entityChat(String query) {
        Tutorial entity = openAIClient.prompt(query)
                .call()
                .entity(Tutorial.class);

        /* NOTE: If you are expecting a list of entities, you can use .entities(ParameterizedTypeReference<List<Tutorial>>(){}) instead. */

        return entity;
    }

    @Override
    public List<College> getCollegesList(String query) {
        Prompt P = new Prompt(query);
        List<College> entities = openAIClient.prompt(P)
                .call()
                .entity(new ParameterizedTypeReference<List<College>>(){}) ;

        /* NOTE: If you are expecting a list of entities, you can use .entity(ParameterizedTypeReference<List<Tutorial>>(){}) instead. */

        return entities;
    }

    @Override
    public String getPromptResponse(String input) {

        String prompt = "You are an Expert in Programming and coding. Provide your response in coding language :{language}.\n" +
                "Respond to the following query:{query}\n" +
                "\nMake sure to provide code snippets where applicable.";
        return openAIClient.prompt()
                        .user(u -> u.text(prompt).params(Map.of("language", LANGUAGE_JAVA, "query", input)))
                        .call().content();

    }


}
