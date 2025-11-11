package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.FavouriteNotesDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ExistDataException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.model.FavouriteNotes;
import com.enotes.Enotes_INDUS.model.FileDetails;
import com.enotes.Enotes_INDUS.model.Notes;
import com.enotes.Enotes_INDUS.repository.CategoryRepository;
import com.enotes.Enotes_INDUS.repository.FavouriteNotesRepository;
import com.enotes.Enotes_INDUS.repository.FileDetailsRepository;
import com.enotes.Enotes_INDUS.repository.NotesRepository;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.Validations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class NotesServiceImpl implements NotesService {


    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private FileDetailsRepository fileRepository;

    @Autowired
    private FavouriteNotesRepository favouriteNotesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validations validations;

    @Value("${file.upload.path}")
    private String uploadPath;


    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Boolean saveNotes(String notesString, MultipartFile file) throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        NotesDto notesDto=objectMapper.readValue(notesString,NotesDto.class);
        notesDto.setIsDeleted(Boolean.FALSE);
        notesDto.setDeletedOn(null);

//trying to update save and update in one single api
        if(!ObjectUtils.isEmpty(notesDto.getId())){
        Notes updatedNote = updateNotes(notesDto, file);
            notesRepository.save(updatedNote);
            return true;

        }





//category valdation
        checkCategoryExist(notesDto.getCategory());
//        Optional<Notes> noteExists=notesRepository.findById(notesDto.getId());
//        if(noteExists.isPresent()){
//            throw new ExistDataException("note already exist");
//
//        }
        validations.notesValidation(notesDto);
        Boolean noteExist=notesRepository.existsByTitleAndCategoryId(notesDto.getTitle(), notesDto.getCategory().getId());
        if (noteExist){throw new ExistDataException("note already exist");}


        Notes notes=mapper.map(notesDto, Notes.class);

        FileDetails fileDetails=saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)){
            notes.setFileDetails(fileDetails);
        }else{
            if(ObjectUtils.isEmpty(notesDto.getId())){
//                updateNotes(notesDto,file);
            notes.setFileDetails(null );

            }

        }

       Notes savedNotes= notesRepository.save(notes);
    return  !ObjectUtils.isEmpty(savedNotes);
    }



//To update save and update in one single api
    private Notes updateNotes(NotesDto notesDto, MultipartFile file) throws ResourceNotFound, IOException {
            Notes existNote=notesRepository.findById(notesDto.getId()).orElseThrow(()->new ResourceNotFound("Invalid notes id"));


        existNote.setTitle(notesDto.getTitle());
        existNote.setDescription(notesDto.getDescription());
        existNote.setCategory(mapper.map(notesDto.getCategory(), Category.class));
        existNote.setUpdatedOn(new Date());
        existNote.setUpdatedBy(notesDto.getUpdatedBy());

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
            FileDetails fileDetails = saveFileDetails(file);
            existNote.setFileDetails(fileDetails);
        } else {
            // retain old file
            existNote.setFileDetails(existNote.getFileDetails());
        }

        return existNote;

    }


    private FileDetails saveFileDetails(MultipartFile file) throws IOException {
        if(!ObjectUtils.isEmpty(file) &&   !file.isEmpty()){
            String originalFileName=file.getOriginalFilename();
            String extension= FilenameUtils.getExtension(originalFileName);
            String randomString= UUID.randomUUID().toString();
            String uploadFileName=randomString+"."+extension;

            List<String> extentions= Arrays.asList("jpg","png","pdf","xlsx","docx","txt");
            if(!extentions.contains(extension)){
                throw new IllegalArgumentException("invalid file format: only upload .jpg .png .pdf .xlsx");
            }
            File saveFile=new File(uploadPath);
            if(!saveFile.exists()){
                saveFile.mkdir();
            }
            String storepath=uploadPath.concat(uploadFileName);
            long upload=Files.copy(file.getInputStream(), Paths.get(storepath)); //converting the files to store into the folder
            if(upload!=0){
                FileDetails fileDetails=new FileDetails();
                fileDetails.setOriginalFileName(originalFileName);
                fileDetails.setDisplayFileName(displayname(originalFileName));
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setFileSize(file.getSize());
                fileDetails.setFilePath(storepath);
                FileDetails savedFileDetails= fileRepository.save(fileDetails);

                return  savedFileDetails;
            }else{
                return null;
            }



        }

        return null;


    }

//    Helper function for saveFileDetails
    private String displayname(String originalFileName) {
        String extension= FilenameUtils.getExtension(originalFileName);
        String fileName=FilenameUtils.removeExtension(originalFileName);
            if(fileName.length()>8){
                fileName=fileName.substring(0,7);
            }

        return fileName+"."+extension;
    }

    //    Helper function for saveFileDetails
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
    @Override
    public NotesResponseDto getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize) {

//        Pagination concept
        Pageable pageable =PageRequest.of(pageNo,pageSize);

        Page<Notes> notesList=notesRepository.findByCreatedByAndIsDeletedFalse(userId,pageable);

                  List<NotesDto> notesDtoList=notesList.stream()
                          .map(notes -> mapper.map(notes,NotesDto.class)).toList();

        NotesResponseDto responseDto=NotesResponseDto.builder()
                .notesDtoList(notesDtoList)
                .pageNo(notesList.getNumber())
                .pageSize(notesList.getSize())
                .totalElements( notesList.getTotalElements())
                .totalPages(notesList.getTotalPages())
                .isFirst(notesList.isFirst())
                .islast(notesList.isLast())
        .build();



        return responseDto;
    }

    @Override
    public void deleteNotes(Integer id) throws ResourceNotFound {
        Notes existNotes= notesRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Notes id invalid"));

        existNotes.setIsDeleted(Boolean.TRUE);
        existNotes.setDeletedOn(LocalDateTime.now());
        notesRepository.save(existNotes);

    }


    @Override
    public void restoreNote(Integer id) throws ResourceNotFound {
        Notes existNotes = notesRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Notes id invalid"));

        existNotes.setIsDeleted(Boolean.FALSE);
        existNotes.setDeletedOn(null);
        notesRepository.save(existNotes);

    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
      List<Notes> notesListRecycle =  notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        List<NotesDto> notesDtoListRecycle= notesListRecycle.stream().map(notes -> mapper.map(notes,NotesDto.class)).toList();

        return  notesDtoListRecycle;
    }

    @Override
    public void deleteNotesFromRecycle(Integer id) throws ResourceNotFound {
        Notes existNotes= notesRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Notes id invalid"));

            if(existNotes.getIsDeleted()){
                notesRepository.deleteById(id);
            }else{
                throw new IllegalArgumentException("cant hard delete directly notes");
            }
    }

    @Override
    public void deleteAllFromRecycle(int userId) {
        List<Notes> deleteNoteList=notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        if(!CollectionUtils.isEmpty(deleteNoteList)){
            notesRepository.deleteAll(deleteNoteList);
        }
        else{
            throw new RuntimeException("alredy empty recycle");
        }
    }

    @Override
    public void favouriteNotes(Integer notesId) throws ResourceNotFound {
        int userId=1;
        Notes existNotes=notesRepository.findById(notesId).orElseThrow(()->new ResourceNotFound("notes id invalid notes not found"));
        FavouriteNotes favouriteNotes=FavouriteNotes.builder()
                .notes(existNotes)
                .userId(userId)
                .build();
        favouriteNotesRepository.save(favouriteNotes);

    }

    @Override
    public void unFavouriteNotes(Integer favNotesId) throws ResourceNotFound {
        FavouriteNotes existFavouriteNotes=favouriteNotesRepository.findById(favNotesId).orElseThrow(()->new ResourceNotFound("fav notes id invalid notes not found"));
        favouriteNotesRepository.delete(existFavouriteNotes);

    }

    @Override
    public List<FavouriteNotesDto> allFavouriteNotes() {
       Integer userId=1;

       List<FavouriteNotes> favouriteNotesList= favouriteNotesRepository.findByUserId(userId);
        List<FavouriteNotesDto> favouriteNotesDtoList = favouriteNotesList.stream()
                .map(fav -> {
                    FavouriteNotesDto dto = mapper.map(fav, FavouriteNotesDto.class);
                    // manually map nested Notes → NotesDto
                    dto.setNotesDto(mapper.map(fav.getNotes(), NotesDto.class));
                    return dto;
                })
                .toList();
        return favouriteNotesDtoList;

    }


    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {

        InputStream inputStream=new FileInputStream(fileDetails.getFilePath());
        byte[] byteData= StreamUtils.copyToByteArray(inputStream);

            return byteData;
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception{
        FileDetails fileDetails= fileRepository.findById(id).orElseThrow(()->new ResourceNotFound("File is not available"));

        return fileDetails;
    }

}
