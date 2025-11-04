package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.FileDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    Boolean saveNotes(String  notes, MultipartFile file) throws ResourceNotFound, Exception;

//    NotesDto getNotesById(Integer id);

    List<NotesDto> getAllNotes();

    byte[] downloadFile(FileDetails fileDetails)throws Exception;

    FileDetails getFileDetails(Integer id) throws Exception;

    NotesResponseDto getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize);
}
