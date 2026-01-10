package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.config.security.CustomUserDetails;
import com.enotes.Enotes_INDUS.service.impl.NotesAiServiceImpl;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.apache.catalina.security.SecurityUtil;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import static com.enotes.Enotes_INDUS.utils.Constants.ROLE_USER;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/notes/summarize")
public class AiController {

    @Autowired
    private NotesAiServiceImpl notesAiService;



    @PostMapping(value = "/ai/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(ROLE_USER)
    public ResponseEntity summaryAi(@PathVariable Integer id) throws IOException {
     String result=notesAiService.summarizeNote(id);

     if(result.isEmpty()){
         return CommonUtil.createErrorResponseMessage("Summary cant be done", HttpStatus.BAD_REQUEST);
     }
        return CommonUtil.createBuildResponse(result,HttpStatus.OK);
    }



    @PostMapping("/ask")
    @PreAuthorize(ROLE_USER)
    public String ask(@RequestBody String question) {

        String result=notesAiService.googleSearch(question);
        return result;
    }

}
