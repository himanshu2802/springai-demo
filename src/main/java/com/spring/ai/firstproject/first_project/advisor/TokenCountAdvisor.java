package com.spring.ai.firstproject.first_project.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

@Slf4j
public class TokenCountAdvisor implements CallAdvisor, StreamAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        log.info("Request: {}", chatClientRequest.prompt().getContents());

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        log.info("Response: {}", chatClientResponse.chatResponse().getResult().getOutput().getText());
        log.info("Response: {}", chatClientResponse.chatResponse().getMetadata().getUsage().getTotalTokens());
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        log.info("Request: {}", chatClientRequest.prompt().getContents());
        Flux<ChatClientResponse> chatClientResponseFlux = streamAdvisorChain.nextStream(chatClientRequest);
        /*chatClientResponseFlux.toStream().forEach(response -> {
            log.info("Response Chunk: {}", response.chatResponse().getResult().getOutput().getText());
            log.info("Response Chunk Tokens: {}", response.chatResponse().getMetadata().getUsage().getTotalTokens());
        });*/
        return chatClientResponseFlux;

    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
