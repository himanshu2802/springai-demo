package com.spring.ai.firstproject.first_project.service.impl;

import com.spring.ai.firstproject.first_project.service.RagService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RagServiceImpl implements RagService {

    private static final Logger log = LoggerFactory.getLogger(RagServiceImpl.class);
    @Value("classpath:prompts/system-message.st")
    private Resource systemMessageResource;

    @Value("classpath:prompts/user-message.st")
    private Resource userMessageResource;

    private ChatClient openAIClient;
    private VectorStore vectorStore;
    private DataLoaderImpl dataLoader;

    public RagServiceImpl(@Autowired @org.springframework.beans.factory.annotation.Qualifier("openAIClient") ChatClient openAIClient,
                            @Autowired @org.springframework.beans.factory.annotation.Qualifier("vectorStore") VectorStore vectorStore,
                          DataLoaderImpl dataLoader
    ) {
        this.openAIClient = openAIClient;
        this.vectorStore = vectorStore;
        this.dataLoader = dataLoader;
    }

    @Override
    public void saveRagData(List<String> data) {
        List<Document> list = data == null ? java.util.Collections.emptyList() : data.stream().map(Document::new).toList();
        vectorStore.add(list);
    }

    @Override
    public String getDataFromRAG(String query) {
        SearchRequest searchRequest = SearchRequest.builder()
                        .topK(5) // Specify the number of similar documents to retrieve
                        .similarityThreshold(0.6) // Specify the similarity threshold for filtering results
                        .query(query)
                        .build();

        List<String> list = vectorStore.similaritySearch(searchRequest).stream().map(Document::getText).toList();
        String context = String.join("\n", list);
        log.info("Retrieved context from vector store: {}", context);
        String response = openAIClient.prompt()
                .system(system -> system.text(systemMessageResource).param("documents", context))
                .user(user -> user.text(userMessageResource).param("query", query))
                .call()
                .content();

        return response;
    }

    @Override
    public String getDataFromRagAdvisor(String query) {
        String response = openAIClient.prompt()
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .user(user -> user.text(userMessageResource).param("query", query))
                .call()
                .content();
        return response;
    }

    @Override
    public String getDataFromRagPipeline(String query) {

        var advisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(
                        RewriteQueryTransformer.builder()
                                .chatClientBuilder(openAIClient.mutate().clone()) // Use the same OpenAI client for query transformation
                                .build(),
                        TranslationQueryTransformer.builder()
                                .chatClientBuilder(openAIClient.mutate().clone())
                                .targetLanguage("english")
                                .build()
                )
                .queryExpander(MultiQueryExpander.builder().chatClientBuilder(openAIClient.mutate().clone()).build())
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .topK(4)
                                .build()
                )
                .documentJoiner(new ConcatenationDocumentJoiner())
                .queryAugmenter(ContextualQueryAugmenter.builder().build())
                .build();

        String response = openAIClient.prompt()
                .advisors(advisor)
                .user(query)
                .call()
                .content();
        return response;
    }

    @Override
    public String readFiles() {
        List<Document> data = dataLoader.loadDocumentsFromJson();
        return "Files read and data loaded into vector store successfully.";
    }
}
