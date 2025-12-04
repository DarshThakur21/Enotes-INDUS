package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.FavouriteNotesDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.FileDetails;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>  getAllNotes(){
        log.info("NotesController : getAllNotes() : Start");
        List<NotesDto> notesDtoList=notesService.getAllNotes();

        if (CollectionUtils.isEmpty(notesDtoList)){
            log.info("All notes fetched by admin Failed");
            return ResponseEntity.noContent().build();
        }
        log.info("All notes fetched by admin Success");
        log.info("NotesController : getAllNotes() : End");
        return CommonUtil.createBuildResponse( notesDtoList, HttpStatus.OK);

    }



//    going to be with authentication and session
    @GetMapping("/user-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNotesByUser(@RequestParam (name = "pageNo",defaultValue = "0") Integer pageNo,
                                               @RequestParam (name = "pageSize",defaultValue = "5") Integer pageSize){

        log.info("NotesController : getAllNotesByUser() : Start");
        Integer userId=CommonUtil.getLoggedInUser().getId();
        NotesResponseDto notesDtoList=notesService.getAllNotesByUser(userId,pageNo,pageSize);
        log.info("NotesController : getAllNotesByUser() : End");
     return CommonUtil.createBuildResponse(notesDtoList,HttpStatus.OK);
    }

    @GetMapping("/search-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getSearchNotes(@RequestParam (name = "pageNo",defaultValue = "0") Integer pageNo,
                                               @RequestParam (name = "pageSize",defaultValue = "5") Integer pageSize,
                                                @RequestParam(name = "keyword") String keyword){
        log.info("NotesController : getSearchNotes() : Start");
        NotesResponseDto notesDtoList=notesService.getAllNotesBySearch(pageNo,pageSize,keyword);
        log.info("NotesController : getSearchNotes() : End");
        return CommonUtil.createBuildResponse(notesDtoList,HttpStatus.OK);
    }


    @PostMapping("/save-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?>  saveNotes(@RequestParam String notes,@RequestParam (required = false) MultipartFile file) throws Exception {
        log.info("NotesController : saveNotes() : Start");

        Boolean savedSuccessNotes=notesService.saveNotes(notes,file);
        if (!savedSuccessNotes){


            log.info("Save Notes Failed");
            return CommonUtil.createErrorResponseMessage("NotSaved",HttpStatus.INTERNAL_SERVER_ERROR);

        }

        log.info("Save Notes Success");
        log.info("NotesController : saveNotes() : End");
        return CommonUtil.createBuildResponseMessage("Saved Success",HttpStatus.CREATED);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNote(@PathVariable Integer id) throws ResourceNotFound {
        log.info("NotesController : deleteNote() : Start");
        notesService.deleteNotes(id);
        log.info("Delete Note Success");
        log.info("NotesController : deleteNote() : End");
        return CommonUtil.createBuildResponseMessage("Delete Success ",HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> restoreNote(@PathVariable Integer id) throws ResourceNotFound {
        log.info("NotesController : restoreNote() : Start");
        notesService.restoreNote(id);
        log.info("NotesController : restoreNote() : End");
        return CommonUtil.createBuildResponseMessage("restore Success ",HttpStatus.OK);
    }


    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception{
        log.info("NotesController : downloadFile() : Start");

        FileDetails fileDetails=notesService.getFileDetails(id);
        byte[] downloadFile     =  notesService.downloadFile(fileDetails);
        HttpHeaders headers =new HttpHeaders();
        String contentType=CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment",fileDetails.getOriginalFileName());

        log.info("NotesController : downloadFile() : End");
        return   ResponseEntity.ok().headers(headers).body(downloadFile);
    }


    @GetMapping("/recycle")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRecycleBinNotes()  throws  Exception{
        log.info("NotesController : getUserRecycleBinNotes() : Start");

        Integer userId=CommonUtil.getLoggedInUser().getId();
        List<NotesDto> notesDtoList=  notesService.getUserRecycleBinNotes(userId);

        if(notesDtoList.isEmpty()){
            log.info("Empty Bin");
            return CommonUtil.createBuildResponseMessage("no Notes in the recycle bin",HttpStatus.OK);
        }

        log.info("Recycle Bin Fetched");
        log.info("NotesController : getUserRecycleBinNotes() : End");
        return CommonUtil.createBuildResponse(notesDtoList,HttpStatus.OK);

    }

    @DeleteMapping("/recycle/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNoteFromRecycle(@PathVariable Integer id) throws ResourceNotFound {
        log.info("NotesController : deleteNoteFromRecycle() : Start");
        notesService.deleteNotesFromRecycle(id);
        log.info("NotesController : deleteNoteFromRecycle() : End");
        return CommonUtil.createBuildResponseMessage("Hard Delete Success ",HttpStatus.OK);
    }

    @DeleteMapping("/recycle/deleteAll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNoteFromRecycle() throws ResourceNotFound {
        log.info("NotesController : All - deleteNoteFromRecycle() : Start");
        Integer userId=CommonUtil.getLoggedInUser().getId();
        notesService.deleteAllFromRecycle(userId);
        log.info("NotesController : All - deleteNoteFromRecycle() : End");
        return CommonUtil.createBuildResponseMessage("Recycle bin delete Success ",HttpStatus.OK);
    }


    @PostMapping("/fav/{notesId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> favouriteNotes(@PathVariable Integer notesId) throws ResourceNotFound {
        log.info("NotesController : favouriteNotes() : Start");

        notesService.favouriteNotes(notesId);
        log.info("NotesController : favouriteNotes() : End");
        return CommonUtil.createBuildResponseMessage("Favourite note added",HttpStatus.OK);
    }

    @PostMapping("/unfav/{favnotesId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favnotesId) throws ResourceNotFound {
        log.info("NotesController : unFavouriteNotes() : Start");
        notesService.unFavouriteNotes(favnotesId);
        log.info("NotesController : unFavouriteNotes() : End");
        return CommonUtil.createBuildResponseMessage("Favourite note removed",HttpStatus.OK);
    }

    @GetMapping("/favs")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> allFavouriteNotes() throws ResourceNotFound {
        log.info("NotesController : allFavouriteNotes() : Start");

       List<FavouriteNotesDto> favouriteNotesDtoList= notesService.allFavouriteNotes();
       if(CollectionUtils.isEmpty(favouriteNotesDtoList)){
           log.info("Favourite List not found");
        return CommonUtil.createErrorResponseMessage("List not found",HttpStatus.NOT_FOUND);
       }
        log.info("Favourite List found");
        log.info("NotesController : allFavouriteNotes() : End");
        return CommonUtil.createBuildResponse(favouriteNotesDtoList,HttpStatus.OK);
    }


    @PostMapping("/copy/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws ResourceNotFound {
        log.info("NotesController : copyNotes() : Start");

        Boolean status=notesService.copyNotes(id);
        if (!status){
            log.info("Copy of the notes not created");
        return CommonUtil.createErrorResponseMessage("copy notes not created",HttpStatus.NOT_FOUND);
        }
        log.info("Copy notes created");
        log.info("NotesController : copyNotes() : End");
        return CommonUtil.createBuildResponseMessage("copy notes created",HttpStatus.CREATED);


    }


//    excel download
        @GetMapping("/notes-excel")
        @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<?> downloadExcelNotes(){
        try {
            log.info("NotesController : downloadExcelNotes() : Start");
            ByteArrayResource resource = notesService.exportToExcel();

            log.info("NotesController : downloadExcelNotes() : End");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=notes_export.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e) {
            log.info("NotesController : downloadExcelNotes() : Cannot download the excel file");
            return CommonUtil.createBuildResponseMessage(
                    "Failed to export Notes Excel: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
}
