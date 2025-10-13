package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/save-notes")
    public ResponseEntity<?>  saveNotes(@Valid @RequestBody NotesDto notesDto) throws Exception {
        Boolean savedSuccessNotes=notesService.saveNotes(notesDto);
        if (!savedSuccessNotes){
//            return   new ResponseEntity<>("your notes is not saved", HttpStatus.INTERNAL_SERVER_ERROR);

            return CommonUtil.createErrorResponseMessage("NotSaved",HttpStatus.INTERNAL_SERVER_ERROR);

        }


//        return  new ResponseEntity<>("your notes is saved", HttpStatus.CREATED);

        return CommonUtil.createBuildResponseMessage("Saved Success",HttpStatus.CREATED);
    }



}
