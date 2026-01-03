package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.FavouriteNotesDto;
import com.enotes.Enotes_INDUS.dto.FileDownloadDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.endpoints.NotesEndpoint;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.FileDetails;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class NotesController implements NotesEndpoint {

    @Autowired
    private NotesService notesService;

    @Override
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
    @Override
        public ResponseEntity<?> getAllNotesByUser( Integer pageNo,Integer pageSize){

        log.info("NotesController : getAllNotesByUser() : Start");
        Integer userId=CommonUtil.getLoggedInUser().getId();
        NotesResponseDto notesDtoList=notesService.getAllNotesByUser(userId,pageNo,pageSize);
        log.info("NotesController : getAllNotesByUser() : End");
     return CommonUtil.createBuildResponse(notesDtoList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getSearchNotes( Integer pageNo,Integer pageSize,String keyword){
        log.info("NotesController : getSearchNotes() : Start");
        NotesResponseDto notesDtoList=notesService.getAllNotesBySearch(pageNo,pageSize,keyword);
        log.info("NotesController : getSearchNotes() : End");
        return CommonUtil.createBuildResponse(notesDtoList,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?>  saveNotes(String notes,MultipartFile file) throws Exception {
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

    @Override
    public ResponseEntity<?> deleteNote( Integer id) throws ResourceNotFound {
        log.info("NotesController : deleteNote() : Start");
        notesService.deleteNotes(id);
        log.info("Delete Note Success");
        log.info("NotesController : deleteNote() : End");
        return CommonUtil.createBuildResponseMessage("Delete Success ",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> restoreNote( Integer id) throws ResourceNotFound {
        log.info("NotesController : restoreNote() : Start");
        notesService.restoreNote(id);
        log.info("NotesController : restoreNote() : End");
        return CommonUtil.createBuildResponseMessage("restore Success ",HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> downloadFile( Integer id) throws Exception{
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

    @Override
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

    @Override
    public ResponseEntity<?> deleteNoteFromRecycle( Integer id) throws ResourceNotFound {
        log.info("NotesController : deleteNoteFromRecycle() : Start");
        notesService.deleteNotesFromRecycle(id);
        log.info("NotesController : deleteNoteFromRecycle() : End");
        return CommonUtil.createBuildResponseMessage("Hard Delete Success ",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteNoteFromRecycle() throws ResourceNotFound {
        log.info("NotesController : All - deleteNoteFromRecycle() : Start");
        Integer userId=CommonUtil.getLoggedInUser().getId();
        notesService.deleteAllFromRecycle(userId);
        log.info("NotesController : All - deleteNoteFromRecycle() : End");
        return CommonUtil.createBuildResponseMessage("Recycle bin delete Success ",HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> favouriteNotes( Integer notesId) throws ResourceNotFound {
        log.info("NotesController : favouriteNotes() : Start");

        notesService.favouriteNotes(notesId);
        log.info("NotesController : favouriteNotes() : End");
        return CommonUtil.createBuildResponseMessage("Favourite note added",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> unFavouriteNotes( Integer favnotesId) throws ResourceNotFound {
        log.info("NotesController : unFavouriteNotes() : Start");
        notesService.unFavouriteNotes(favnotesId);
        log.info("NotesController : unFavouriteNotes() : End");
        return CommonUtil.createBuildResponseMessage("Favourite note removed",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allFavouriteNotes() throws ResourceNotFound {
        log.info("NotesController : allFavouriteNotes() : Start");

       List<FavouriteNotesDto> favouriteNotesDtoList = notesService.allFavouriteNotes();
       if(CollectionUtils.isEmpty(favouriteNotesDtoList)){
           log.info("Favourite List not found");
        return CommonUtil.createErrorResponseMessage("List not found",HttpStatus.NOT_FOUND);
       }

        log.info("Favourite List found");
        log.info("NotesController : allFavouriteNotes() : End");
        return CommonUtil.createBuildResponse(favouriteNotesDtoList,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> copyNotes( Integer id) throws ResourceNotFound {
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
    @Override
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

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        FileDetails fileDetails=notesService.uploadFile(file);
        if(ObjectUtils.isEmpty(fileDetails)){
            return CommonUtil.createErrorResponseMessage("couldnt save the file ",HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponseMessage("upload success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> downloadFileDirect(Integer id) {
        FileDownloadDto fileDownloadDto=notesService.downloadDirectFile(id);

        byte[] fileData=fileDownloadDto.getFileData();
        FileDetails fileDetails=fileDownloadDto.getFileDetails();

        if(ObjectUtils.isEmpty(fileData)){
            return CommonUtil.createErrorResponseMessage("couldnt get the file ",HttpStatus.BAD_REQUEST);
        }
    String contentType=CommonUtil.getContentType(fileDetails.getOriginalFileName());
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment",fileDetails.getOriginalFileName());

        return   ResponseEntity.ok().headers(headers).body(fileData);
    }



}
