package com.spring.ai.firstproject.first_project.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ai.firstproject.first_project.service.DataLoader;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DataLoaderImpl implements DataLoader {

    private final Resource resource;
    private final Resource pdfResource;

    DataLoaderImpl(@Value("classpath:sample_data.json") Resource resource,
                   @Value("classpath:cricket_rules.json") Resource pdfResource) {
        this.resource = resource;
        this.pdfResource = pdfResource;
    }

    @Override
    public List<Document> loadDocumentsFromJson() {
        return List.of();
    }

    @Override
    public List<Document> loadDocumentsFromPdf() {
        return List.of();
    }

    /*@Override
    public List<Document> loadDocumentsFromJson() {
        return new JsonReader(resource, "name").read();
    }

    @Override
    public List<Document> loadDocumentsFromPdf() {
        var pdfReader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .
                        .build())
    }*/
}
