package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.service.impl.NotesAiServiceImpl;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/notes/summarize")
public class AiController {

    @Autowired
    private NotesAiServiceImpl notesAiService;

    @PostMapping(
            value = "/ai/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity summaryAi(@PathVariable Integer id) throws IOException {
//@RequestPart(value = "file",required = false) MultipartFile file
     String result=notesAiService.summarizeNote(id);

     if(result.isEmpty()){
         return CommonUtil.createErrorResponseMessage("Summary cant be done", HttpStatus.BAD_REQUEST);
     }
        return CommonUtil.createBuildResponse(result,HttpStatus.OK);
    }

}
