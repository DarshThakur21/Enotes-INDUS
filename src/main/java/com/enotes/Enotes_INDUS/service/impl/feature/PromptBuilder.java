package com.enotes.Enotes_INDUS.service.impl.feature;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder
{
    public String build(String title, String description, String pdfText) {

        return """
            You are a smart notes assistant.
    
            Note Title:
            %s
    
            Notes Description:
            %s
        
            Notes if pdf is present:
            %s
    
            Rules:
            - Summarize clearly
            - Use bullet points
            - Keep concise
        """
                .formatted(
                title,
                description,
                pdfText != null ? "PDF Content:\n" + pdfText : "No PDF attached"
                );
    }

    public String builderPromptGoogle(String question){
        return """
            You are an intelligent AI assistant similar to Google Search.
            
            Instructions:
            - Answer any question clearly
            - Be factual and concise
            - Explain step-by-step if technical
            - If unsure, say you are unsure
            
            User Question:
            %s
        """
                .formatted(question);
    }

}
