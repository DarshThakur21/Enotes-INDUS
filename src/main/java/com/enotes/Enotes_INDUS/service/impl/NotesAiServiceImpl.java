package com.enotes.Enotes_INDUS.service.impl;


import com.enotes.Enotes_INDUS.service.impl.feature.ConversationMemory;
import com.enotes.Enotes_INDUS.service.impl.feature.GeminiClient;
import com.enotes.Enotes_INDUS.service.impl.feature.PdfTextExtractor;
import com.enotes.Enotes_INDUS.service.impl.feature.PromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NotesAiServiceImpl {

    @Autowired
    private GeminiClient client;

    @Autowired
    private PdfTextExtractor pdfTextExtractor;

    @Autowired
    private ConversationMemory conversationMemory;

    @Autowired
    private PromptBuilder promptBuilder;


    public String summarize(String conversationId, String title, String desc, MultipartFile file){
        String pdfText= file !=null && !file.isEmpty()?pdfTextExtractor.extract(file) : null;
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+pdfText);
        System.out.println("PDF TEXT LENGTH = " + (pdfText == null ? 0 : pdfText.length()));
        System.out.println("PDF TEXT PREVIEW >>>\n" +
                (pdfText == null ? "null" : pdfText.substring(0, Math.min(300, pdfText.length()))));


        String prompt=promptBuilder.build(title,desc,pdfText);
        String finalPrompt=conversationMemory.withHistory(conversationId,prompt);
        String response=client.generate(finalPrompt);

        conversationMemory.addResponse(conversationId,response);
        return response;
    }

}
