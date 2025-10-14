package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    Boolean saveNotes(String  notes, MultipartFile file) throws ResourceNotFound, Exception;

//    NotesDto getNotesById(Integer id);

    List<NotesDto> getAllNotes();

}
