package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.model.FileDetails;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @GetMapping("/")
    public ResponseEntity<?>  getAllNotes(){
        List<NotesDto> notesDtoList=notesService.getAllNotes();

        if (CollectionUtils.isEmpty(notesDtoList)){
            return ResponseEntity.noContent().build();
        }

//        return new ResponseEntity<>(notesDtoList, HttpStatus.OK);
        return CommonUtil.createBuildResponse( notesDtoList, HttpStatus.OK);

    }



//    going to be with authentication and session
    @GetMapping("/user-notes")
    public ResponseEntity<?> getAllNotesByUser(@RequestParam (name = "pageNo",defaultValue = "0") Integer pageNo,
                                               @RequestParam (name = "pageSize",defaultValue = "5") Integer pageSize){
        Integer userId=1;
        NotesResponseDto notesDtoList=notesService.getAllNotesByUser(userId,pageNo,pageSize);
     return CommonUtil.createBuildResponse(notesDtoList,HttpStatus.OK);
    }




    @PostMapping("/save-notes")
    public ResponseEntity<?>  saveNotes(@RequestParam String notes,@RequestParam (required = false) MultipartFile file) throws Exception {
        Boolean savedSuccessNotes=notesService.saveNotes(notes,file);
        if (!savedSuccessNotes){


            return CommonUtil.createErrorResponseMessage("NotSaved",HttpStatus.INTERNAL_SERVER_ERROR);

        }




        return CommonUtil.createBuildResponseMessage("Saved Success",HttpStatus.CREATED);
    }






    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception{

        FileDetails fileDetails=notesService.getFileDetails(id);
        byte[] downloadFile     =  notesService.downloadFile(fileDetails);

        HttpHeaders headers =new HttpHeaders();
        String contentType=CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment",fileDetails.getOriginalFileName());
        return   ResponseEntity.ok().headers(headers).body(downloadFile);
    }








}
