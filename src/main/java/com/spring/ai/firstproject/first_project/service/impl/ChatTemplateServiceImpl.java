package com.spring.ai.firstproject.first_project.service.impl;

import com.spring.ai.firstproject.first_project.model.CollegeResponse;
import com.spring.ai.firstproject.first_project.model.StudentDetailModel;
import com.spring.ai.firstproject.first_project.service.ChatTemplateService;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ChatTemplateServiceImpl implements ChatTemplateService {
    @Value("classpath:prompts/system-message.st")
    private Resource systemMessageResource;

    @Value("classpath:prompts/user-message.st")
    private Resource userMessageResource;


    private ChatClient openAIClient;
    private ChatClient genAIClient;

    public ChatTemplateServiceImpl(@Autowired @Qualifier("openAIClient") ChatClient openAIClient,
                                   @Autowired @Qualifier("geminiAIClient") ChatClient genAIClient) {
        this.openAIClient = openAIClient;
        this.genAIClient = genAIClient;
    }

    @Override
    public String getChatTemplate(String query) {

        // Step-1: Creating a prompt template
        PromptTemplate template = PromptTemplate.builder()
                .template("Provide a detailed explanation about the following topic: {topic}")
                .build();

        //Step-2: Rendering the template with actual values
        String renderedPrompt = template.render(Map.of("topic", query));

        // Step-3: Using the rendered, prompt to get a response from the ChatClient
        Prompt pmt = new Prompt(renderedPrompt);
        String response = openAIClient.prompt(pmt).call().content();

        return response;
    }

    @Override
    public String getRolePromptResponse(String query) {

        //Template Creation
        SystemPromptTemplate getRolePromptResponse = SystemPromptTemplate.builder()
                .template("You are a helpful assistant that provides concise answers.")
                .build();
        //Creating Messages - Renderer
        var sysMessage = getRolePromptResponse.createMessage();

        //User Template
        PromptTemplate userTemplate = PromptTemplate.builder()
                .template("Answer the following question concisely: {question}")
                .build();
        //Creating Messages - Renderer
        var userMessage = userTemplate.createMessage(Map.of("question", query));

        //Creating Prompt with System and User Messages
        Prompt prompt = new Prompt(sysMessage, userMessage);

        //Getting Response from ChatClient
        String response = openAIClient.prompt(prompt).call().content();
        return response;
    }

    @Override
    public byte[] getFromResource(StudentDetailModel model) {

        List<CollegeResponse> response = genAIClient.prompt()
                .system(systemMessageResource)
                .user(u -> u.text(userMessageResource).params(Map.of("marks", model.getMarks(),
                        "exam", model.getExamName(),
                        "year", model.getYear(),
                        //"city", model.getCity(),
                        "location_preference", model.getLocationPreference(),
                        "preferred_course", model.getPreferredCourse(),
                        "gender", model.getGender(),
                        "category", model.getCatagory())))
                .call()
                .entity(new ParameterizedTypeReference<List<CollegeResponse>>() {});




        return generateCandidateCollegeDoc(model, response);
    }

    public byte[] generateCandidateCollegeDoc(StudentDetailModel candidate, List<CollegeResponse> colleges) {

        try (InputStream is = new ClassPathResource("templates/Candidate_College_list.docx").getInputStream();
             XWPFDocument document = new XWPFDocument(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            replaceCandidateDetails(document, candidate);
            populateCollegeTable(document, colleges);

            document.write(out);
            return out.toByteArray();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void replaceCandidateDetails(XWPFDocument document, StudentDetailModel c) {
        Map<String, String> values = Map.of(
                "{{studentName}}", c.getStudentName(),
                "{{marks}}", String.valueOf(c.getMarks()),
                "{{city}}", c.getCity(),
                "{{category}}", c.getCatagory(),
                "{{locationPreference}}", c.getLocationPreference()
        );

        for (XWPFParagraph paragraph : document.getParagraphs()) {

            String paragraphText = paragraph.getText();
            boolean hasPlaceholder = values.keySet()
                    .stream()
                    .anyMatch(paragraphText::contains);

            if (!hasPlaceholder) {
                continue;
            }

            for (Map.Entry<String, String> entry : values.entrySet()) {
                paragraphText = paragraphText.replace(entry.getKey(), entry.getValue());
            }

            // Remove existing runs
            int runs = paragraph.getRuns().size();
            for (int i = runs - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }

            // Create single run with replaced text
            XWPFRun run = paragraph.createRun();
            run.setText(paragraphText);
        }
    }

    private void populateCollegeTable(
            XWPFDocument document,
            List<CollegeResponse> colleges
    ) {
        XWPFTable table = document.getTables().get(0); // first table

        int serialNo = 1;
        for (CollegeResponse college : colleges) {

            XWPFTableRow row = table.createRow();

            row.getCell(0).setText(String.valueOf(serialNo++));
            row.getCell(1).setText(college.getCollegeName());
            row.getCell(2).setText(college.getLocation());
            row.getCell(3).setText(college.getCourse());
            row.getCell(4).setText(String.valueOf(college.getCutOff()));
            row.getCell(5).setText(college.getLikelihood() + "%");
            row.getCell(6).setText(college.getRemarks());
        }
    }


}
