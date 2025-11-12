package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.FavouriteNotesDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.FavouriteNotes;
import com.enotes.Enotes_INDUS.model.FileDetails;
import com.enotes.Enotes_INDUS.model.Notes;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    Boolean saveNotes(String  notes, MultipartFile file) throws ResourceNotFound, Exception;

//    NotesDto getNotesById(Integer id);

    List<NotesDto> getAllNotes();

    byte[] downloadFile(FileDetails fileDetails)throws Exception;

    FileDetails getFileDetails(Integer id) throws Exception;

    NotesResponseDto getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize);

    void deleteNotes(Integer id) throws ResourceNotFound;

    void restoreNote(Integer id) throws ResourceNotFound;

    List<NotesDto> getUserRecycleBinNotes(Integer userId);

    void deleteNotesFromRecycle(Integer id) throws ResourceNotFound;

    void deleteAllFromRecycle(int userId);

    void favouriteNotes(Integer notesId) throws ResourceNotFound;

    void unFavouriteNotes(Integer notesId) throws ResourceNotFound;

    List<FavouriteNotesDto>  allFavouriteNotes();

    Boolean copyNotes(Integer id) throws ResourceNotFound;

    ByteArrayResource exportToExcel();
}
