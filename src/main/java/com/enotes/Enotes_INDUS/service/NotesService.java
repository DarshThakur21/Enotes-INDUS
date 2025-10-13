package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;

import java.util.List;

public interface NotesService {

    Boolean saveNotes(NotesDto notesDto) throws ResourceNotFound, Exception;

//    NotesDto getNotesById(Integer id);

    List<NotesDto> getAllNotes();

}
