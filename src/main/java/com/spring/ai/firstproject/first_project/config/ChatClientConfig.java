package com.spring.ai.firstproject.first_project.config;

import com.spring.ai.firstproject.first_project.advisor.TokenCountAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class ChatClientConfig {

    // Spring AI provides auto-configuration for the JdbcChatMemoryRepository, that you can use directly in your application.
    // We have added the necessary dependencies for the JdbcChatMemoryRepository in the pom.xml file and also added the required configurations in the application.properties file.
    /*@Autowired
    JdbcChatMemoryRepository chatMemoryRepository;

     */

    // Here we are creating a bean for the ChatMemory using the MessageWindowChatMemory implementation and providing the JdbcChatMemoryRepository to it.
    // By this we can update the default implementation as per our requirements.
    @Bean
    public ChatMemory chatMemory(@Autowired JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();
    }


    @Bean
    @Qualifier("openAIClient")
    public ChatClient openAIChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {

        log.info("ChatMemory instance being used in ChatClientConfig: {}", chatMemory.getClass().getName());

        // If not provided explicitly, chatMemory uses MessageWindowChatMemory with default window size of 20.
        // In turn the MessageWindowChatMemory uses InMemoryChatMessageRepository to store the messages.
        // So, in this case the messages will be stored in memory and only the last 20 messages will be remembered.
        // Added Note: The above line is true if we do not have provided ChatMemory Implementation. Above statement was true before we have asked to use JdbcChatMemoryRepository.
        // Now since we have provided JdbcChatMemoryRepository, the messages will be stored in the database and the MessageWindowChatMemory will fetch the relevant messages from the database based on the conversation ID and window size.
        // You can customize the chat memory as per your requirements and also create your own custom memory by implementing ChatMemory interface
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(
                        // This advisor will save the conversation in the provided chat memory and also add the relevant conversation history to the prompt for context.
                        //chatMemoryAdvisor,
                        // This is a custom advisor to count the tokens in the request and response and log them.
                        new TokenCountAdvisor(),
                        //default Advisor to log the requests and responses
                        new SimpleLoggerAdvisor(),
                        //default Advisor to safeguard against sensitive content in the request and response. Here we are blocking the requests and responses containing the words "sex" and "nude". You can customize the list of words as per your requirements.
                        new SafeGuardAdvisor(List.of("sex", "nude")))
                // You can also set default options for the chat client here.
                // These options will be applied to all the requests made using this client unless overridden at the request level.
                // Here we are setting the default system prompt and temperature for the OpenAI client.
                .defaultSystem("You are a helpful assistant for my application.")
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.9)
                        .maxTokens(2000)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("geminiAIClient")
    public ChatClient geminiAIClient(GoogleGenAiChatModel genAiChatModel) {
        return ChatClient.builder(genAiChatModel)
                .defaultSystem("You are a helpful assistant for my application.")
                .build();
    }

    //NOTE: In case you do not want to provide default options for chat client in application.properties
    //For any reason like suppose you want to create multiple clients with different options then you can create beans like below

    @Bean
    @Qualifier("highTemperatureClient")
    public ChatClient highTemperatureClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("You are an expert career counselor. Answer the questions as you are talking to a college student.")
                // Here we are setting the default options for this client to have a higher temperature and lower max tokens.
                // You can customize the options as per your requirements.
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.9)
                        .maxTokens(100)
                        .build())
                .build();
    }

    /*@Bean
    @Qualifier("azureAIClient")
    public ChatClient azureAIChatClient(AzureOpenAiChatModel azureOpenAiChatModel) {
        return ChatClient.builder(azureOpenAiChatModel)
                .defaultSystem("You are a helpful assistant for my application.")
                .build();
    }

    @Bean
    @Qualifier("ollamaAIClient")
    public ChatClient ollamaAIChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("You are a helpful assistant for my application.")
                .build();
    }*/
}
