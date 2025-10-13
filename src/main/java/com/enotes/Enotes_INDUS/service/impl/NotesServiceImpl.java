package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.exceptions.ExistDataException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.model.Notes;
import com.enotes.Enotes_INDUS.repository.CategoryRepository;
import com.enotes.Enotes_INDUS.repository.NotesRepository;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class NotesServiceImpl implements NotesService {


    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validations validations;



    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Boolean saveNotes(NotesDto notesDto) throws Exception {

//category valdation
        checkCategoryExist(notesDto.getCategory());



//        Optional<Notes> noteExists=notesRepository.findById(notesDto.getId());
//        if(noteExists.isPresent()){
//            throw new ExistDataException("note already exist");
//
//        }
        validations.notesValidation(notesDto);

        Boolean noteExist=notesRepository.existsByTitleAndCategoryId(notesDto.getTitle(), notesDto.getCategory().getId());

        if (noteExist){

            throw new ExistDataException("note already exist");
        }



      Notes notes=mapper.map(notesDto, Notes.class);


//      if(ObjectUtils.isEmpty(notes.getId())){}




       Notes savedNotes= notesRepository.save(notes);
    return  !ObjectUtils.isEmpty(savedNotes);
    }

    private void checkCategoryExist(NotesDto.CategoryDto category) throws ResourceNotFound {

        categoryRepository.findById(category.getId()).orElseThrow(()->new ResourceNotFound("categpry id is invalid"));
    }



    @Override
    public List<NotesDto> getAllNotes() {


        List<Notes> notesList=notesRepository.findAll();
        List<NotesDto> notesDtoList=notesList.stream()
                .map(notes -> mapper.map(notes, NotesDto.class)).toList();
        return notesDtoList;
//        return notesRepository.findAll().stream().map(notes -> mapper.map(notes, NotesDto.class)).toList();
    }
}
