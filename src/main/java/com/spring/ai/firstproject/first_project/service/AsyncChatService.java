package com.spring.ai.firstproject.first_project.service;


import reactor.core.publisher.Flux;

public interface AsyncChatService {

    Flux<String> getAsyncChatResponse(String query);
}
