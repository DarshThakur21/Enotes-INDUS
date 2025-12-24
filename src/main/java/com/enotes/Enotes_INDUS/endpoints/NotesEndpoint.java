package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.enotes.Enotes_INDUS.utils.Constants.*;

@RequestMapping("/api/v1/notes")
public interface NotesEndpoint {

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllNotes();

    @GetMapping("/user-notes")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo",defaultValue = DEFAULT_PAGENO) Integer pageNo,
                                               @RequestParam (name = "pageSize",defaultValue = DEFAULT_PAGESIZE) Integer pageSize);

    @GetMapping("/search-notes")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getSearchNotes(@RequestParam (name = "pageNo",defaultValue = DEFAULT_PAGENO) Integer pageNo,
                                            @RequestParam (name = "pageSize",defaultValue = DEFAULT_PAGESIZE) Integer pageSize,
                                            @RequestParam(name = "keyword") String keyword);

    @PostMapping("/save-notes")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?>  saveNotes(@RequestParam String notes,@RequestParam (required = false) MultipartFile file) throws Exception;

    @GetMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> deleteNote(@PathVariable Integer id) throws ResourceNotFound;

    @GetMapping("/restore/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> restoreNote(@PathVariable Integer id) throws ResourceNotFound;

    @GetMapping("/download/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;

    @GetMapping("/recycle")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getUserRecycleBinNotes()  throws  Exception;

    @DeleteMapping("/recycle/delete/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> deleteNoteFromRecycle(@PathVariable Integer id) throws ResourceNotFound;

    @DeleteMapping("/recycle/deleteAll")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> deleteNoteFromRecycle() throws ResourceNotFound;

    @PostMapping("/fav/{notesId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> favouriteNotes(@PathVariable Integer notesId) throws ResourceNotFound;

    @PostMapping("/unfav/{favnotesId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favnotesId) throws ResourceNotFound;


    @GetMapping("/favs")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> allFavouriteNotes() throws ResourceNotFound;


    @PostMapping("/copy/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws ResourceNotFound;

    @GetMapping("/notes-excel")
    @PreAuthorize(ROLE_USER)
    public  ResponseEntity<?> downloadExcelNotes();

    @PostMapping("/file-upload")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> uploadFile(@RequestParam("uploadFile") MultipartFile multipartFile);


    @GetMapping("/download-file-direct/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> downloadFileDirect(@PathVariable Integer id);

}
