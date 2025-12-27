package com.enotes.Enotes_INDUS.service.impl;


import com.enotes.Enotes_INDUS.model.FileDetails;
import com.enotes.Enotes_INDUS.model.Notes;
import com.enotes.Enotes_INDUS.repository.NotesRepository;
import com.enotes.Enotes_INDUS.service.impl.feature.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @Autowired
    private NotesRepository notesRepository;

public String summarizeNote(Integer id) throws IOException {
    Notes notes=notesRepository.getById(id);
    String conversationId=notes.getCreatedBy()+":"+notes.getId();

    MultipartFile multipartFile = null;

    if (notes.getFileDetails() != null) {
        FileDetails fd = notes.getFileDetails();
        byte[] content = Files.readAllBytes(Paths.get(fd.getFilePath()));

        multipartFile = new ByteArrayMultipartFile(
                content,
                fd.getUploadFileName(),      // name
                fd.getOriginalFileName(),    // original file name
                "application/pdf"            // content type
        );
    }

    String result = summarize(conversationId,notes.getTitle(),notes.getDescription(),  multipartFile    );
    return result;
}


    private  String summarize(String conversationId, String title, String desc, MultipartFile file){
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
