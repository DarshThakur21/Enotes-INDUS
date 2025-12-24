package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.FavouriteNotesDto;
import com.enotes.Enotes_INDUS.dto.FileDownloadDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.NotesResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ExistDataException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.*;
import com.enotes.Enotes_INDUS.repository.*;
import com.enotes.Enotes_INDUS.service.NotesService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import com.enotes.Enotes_INDUS.utils.UserExportToExcelService;
import com.enotes.Enotes_INDUS.utils.Validations;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
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


    @Autowired
    private UserExportToExcelService userExportToExcelService;




    @Override
    public Boolean saveNotes(String notesString, MultipartFile file) throws Exception {
        log.info("NotesServiceImpl : saveNotes() : Start");
        ObjectMapper objectMapper=new ObjectMapper();
        NotesDto notesDto=objectMapper.readValue(notesString,NotesDto.class);

        log.debug("Parsed NotesDto: {}", notesDto);

        notesDto.setIsDeleted(Boolean.FALSE);
        notesDto.setDeletedOn(null);

//trying to update save and update in one single api
        if(!ObjectUtils.isEmpty(notesDto.getId())){
            log.info("Updating existing note ");
        Notes updatedNote = updateNotes(notesDto, file);
            notesRepository.save(updatedNote);
            log.info("NotesServiceImpl : saveNotes() : End (Update Success)");
            return true;

        }

//category valdation
        checkCategoryExist(notesDto.getCategory());
//        Optional<Notes> noteExists=notesRepository.findById(notesDto.getId());
//        if(noteExists.isPresent()){
//            throw new ExistDataException("note already exist");
//
//        }
        log.info("Creating new note, validating category & title");
        validations.notesValidation(notesDto);
        Boolean noteExist=notesRepository.existsByTitleAndCategoryId(notesDto.getTitle(), notesDto.getCategory().getId());
        if (noteExist){
            log.error("Duplicate note  found");
            throw new ExistDataException("note already exist");
        }


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
        log.info("NotesServiceImpl : saveNotes() : End (Saved)");
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
        log.info("NotesServiceImpl : getAllNotes() : Start");


        List<Notes> notesList=notesRepository.findAll();
        List<NotesDto> notesDtoList=notesList.stream()
                .map(notes -> mapper.map(notes, NotesDto.class)).toList();

        log.info("NotesServiceImpl : getAllNotes() : End");
        return notesDtoList;
//        return notesRepository.findAll().stream().map(notes -> mapper.map(notes, NotesDto.class)).toList();
    }
    @Override
    public NotesResponseDto getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize) {
        log.info("NotesServiceImpl : getAllNotesByUser() : Start");


//        Pagination concept
        Pageable pageable=PageRequest.of(pageNo,pageSize);

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

        log.info("Sent the data with pae requests");

        log.info("NotesServiceImpl : getAllNotesByUser() : End");
        return responseDto;
    }

    @Override
    public NotesResponseDto getAllNotesBySearch( Integer pageNo, Integer pageSize,String keyword) {
        log.info("NotesServiceImpl : getAllNotesBySearch() : Start");

        Integer userId=CommonUtil.getLoggedInUser().getId();

            Pageable pageable =PageRequest.of(pageNo,pageSize);

        Page<Notes> notesList=notesRepository.searchNotes(keyword,userId,pageable);

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

        log.info("searching the notes...");

        log.info("NotesServiceImpl : getAllNotesBySearch() : End");

        return responseDto;
    }

    @Override
    @CacheEvict(value = {"allNotes","favouriteNotesList","allNotesByUser"},key = "#id")
    public void deleteNotes(Integer id) throws ResourceNotFound {
        log.info("NotesServiceImpl : deleteNotes() : Start");
        Notes existNotes= notesRepository.findById(id).orElseThrow(()-> {
            log.error("Invalid notes id: {}", id);
            return  new ResourceNotFound("Notes id invalid");
        });


        existNotes.setIsDeleted(Boolean.TRUE);
        existNotes.setDeletedOn(LocalDateTime.now());
        log.info("delete success");
        log.info("NotesServiceImpl : deleteNotes() : End");
        notesRepository.save(existNotes);

    }


    @Override
    public void restoreNote(Integer id) throws ResourceNotFound {
        log.info("NotesServiceImpl : restoreNote() : Start");

        Notes existNotes = notesRepository.findById(id).orElseThrow(() ->{
                log.error("Invalid notes id: {}", id);
            return new ResourceNotFound("Notes id invalid");
        });

        existNotes.setIsDeleted(Boolean.FALSE);
        existNotes.setDeletedOn(null);
        log.info("restored success");
        log.info("NotesServiceImpl : restoreNote() : End");
        notesRepository.save(existNotes);

    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
        log.info("NotesServiceImpl : getUserRecycleBinNotes() : Start");

      List<Notes> notesListRecycle =  notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        List<NotesDto> notesDtoListRecycle= notesListRecycle.stream().map(notes -> mapper.map(notes,NotesDto.class)).toList();

        log.info("NotesServiceImpl : getUserRecycleBinNotes() : End");
        return  notesDtoListRecycle;
    }


    @Override
    @CacheEvict(value = {"allNotes","favouriteNotesList","allNotesByUser","recycleBin"},key = "#id")
    public void deleteNotesFromRecycle(Integer id) throws ResourceNotFound {
        log.info("NotesServiceImpl : deleteNotesFromRecycle() : Start");

        Notes existNotes= notesRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Notes id invalid"));

            if(existNotes.getIsDeleted()){

                log.info("Delete successful");
                log.info("NotesServiceImpl : deleteNotesFromRecycle() : End");
                notesRepository.deleteById(id);
            }else{

                log.info("Delete from recycle failed");
                throw new IllegalArgumentException("cant hard delete directly notes");
            }
    }

    @Override
    @CacheEvict(value = "recycleBin")
    public void deleteAllFromRecycle(int userId) {
        log.info("NotesServiceImpl : deleteAllFromRecycle() : Start");

        List<Notes> deleteNoteList=notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        if(!CollectionUtils.isEmpty(deleteNoteList)){
            notesRepository.deleteAll(deleteNoteList);
            log.info("Delete successful");
            log.info("NotesServiceImpl : deleteAllFromRecycle() : End");
        }
        else{
            log.info("Delete from recycle failed");
            throw new RuntimeException("alredy empty recycle");
        }
    }

    @Override
    public void favouriteNotes(Integer notesId) throws ResourceNotFound {
        log.info("NotesServiceImpl : favouriteNotes() : Start");

        int userId= CommonUtil.getLoggedInUser().getId();
        Notes existNotes=notesRepository.findById(notesId).orElseThrow(()->new ResourceNotFound("notes id invalid notes not found"));
        FavouriteNotes favouriteNotes=FavouriteNotes.builder()
                .notes(existNotes)
                .userId(userId)
                .build();
        favouriteNotesRepository.save(favouriteNotes);
        log.info("NotesServiceImpl : favouriteNotes() : End");

    }

    @Override

    public void unFavouriteNotes(Integer favNotesId) throws ResourceNotFound {
        log.info("NotesServiceImpl : unFavouriteNotes() : Start");

        FavouriteNotes existFavouriteNotes=favouriteNotesRepository.findById(favNotesId).orElseThrow(()->new ResourceNotFound("fav notes id invalid notes not found"));
        favouriteNotesRepository.delete(existFavouriteNotes);
        log.info("NotesServiceImpl : unFavouriteNotes() : End");


    }

    @Override
    public List<FavouriteNotesDto> allFavouriteNotes() {
        log.info("NotesServiceImpl : allFavouriteNotes() : Start");

        int userId= CommonUtil.getLoggedInUser().getId();

       List<FavouriteNotes> favouriteNotesList= favouriteNotesRepository.findByUserId(userId);
        List<FavouriteNotesDto> favouriteNotesDtoList = favouriteNotesList.stream()
                .map(fav -> {
                    FavouriteNotesDto dto = mapper.map(fav, FavouriteNotesDto.class);
                    // manually map nested Notes → NotesDto
                    dto.setNotesDto(mapper.map(fav.getNotes(), NotesDto.class));
                    return dto;
                })
                .toList();
        log.info("NotesServiceImpl : allFavouriteNotes() : End");
        return favouriteNotesDtoList;

    }

    @Override
    public Boolean copyNotes(Integer id) throws ResourceNotFound {
        log.info("NotesServiceImpl : copyNotes() : Start");

        Notes notes=notesRepository.findById(id).orElseThrow(()-> {
            log.error("note to copy not found");
            return  new ResourceNotFound("no such note found");
        });

        Notes copyNotes=Notes.builder()
                .title(notes.getTitle())
                .description(notes.getDescription())
                .isDeleted(false)
                .fileDetails(notes.getFileDetails())
                .build();


            Notes saveCopyNotes=notesRepository.save(copyNotes);
            if(ObjectUtils.isEmpty(saveCopyNotes)){
                log.info("Copy Failed");
                return false;
            }
        log.info("Copy sucess");
        log.info("NotesServiceImpl : copyNotes() : End");

            return true;

    }

    @Override
    public ByteArrayResource exportToExcel() {
        log.info("NotesServiceImpl : exportToExcel() : Start");

        List<Notes> exportNotes=notesRepository.findAll();
        List<NotesDto> exportNotesDTO=exportNotes.stream()
                .map(notes -> mapper.map(notes,NotesDto.class))
                .toList();

       ByteArrayOutputStream out= userExportToExcelService.exportToExcel(exportNotesDTO);
        log.info("NotesServiceImpl : exportToExcel() : End");
        return new ByteArrayResource(out.toByteArray());
    }


    @Override
    public FileDetails uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new ResourceNotFound("File is empty or does not exist");
            }

            // 1. Ensure the directory exists using Path API
            Path rootPath = Paths.get(uploadPath);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
                log.info("Created directory at: " + rootPath.toAbsolutePath());
            }

            // 2. Generate unique filename
            String originalFileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFileName);
            String uniqueName = UUID.randomUUID().toString() + "." + extension;

            // 3. Resolve the target path correctly
            // resolve() is better than string concatenation as it manages separators automatically
            Path targetPath = rootPath.resolve(uniqueName);

            // 4. PHYSICAL UPLOAD: Copy the file stream to the destination
            Files.copy(file.getInputStream(), targetPath);

            // 5. Build and Save metadata to DB
            FileDetails fileDetails = FileDetails.builder()
                    .displayFileName(originalFileName)
                    .filePath(targetPath.toAbsolutePath().toString()) // Store the full absolute path
                    .uploadFileName(uniqueName)
                    .fileSize(file.getSize())
                    .originalFileName(originalFileName)
                    .build();

            return fileRepository.save(fileDetails);

        } catch (Exception e) {
            log.error("Error during file upload: ", e);
            throw new RuntimeException("Could not store the file: " + e.getMessage());
        }
    }

    @Override
    public FileDownloadDto downloadDirectFile(Integer id) {

      try {
          Optional<FileDetails> fileDetailsOptional =fileRepository.findById(id);
          FileDetails fileDetails=fileDetailsOptional.get();

          Path path=Paths.get(uploadPath).resolve(fileDetails.getUploadFileName()).toAbsolutePath();

          log.info("System looking for file at: {}", path);

          if (!Files.exists(path)) {
              // Log the parent directory content to see what's actually there
              log.error("File MISSING at path: {}", path);
              throw new RuntimeException("Physical file not found at: " + path);
          }


          return FileDownloadDto.builder()
                  .fileData(Files.readAllBytes(path))
                  .fileDetails(fileDetails)
                  .build();

      } catch (RuntimeException e) {
          throw new RuntimeException(e);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

    }


    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {
        log.info("NotesServiceImpl : downloadFile() : Start");
        InputStream inputStream=new FileInputStream(fileDetails.getFilePath());
        byte[] byteData= StreamUtils.copyToByteArray(inputStream);
        log.info("NotesServiceImpl : downloadFile() : End");
            return byteData;
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception{
        log.info("NotesServiceImpl : getFileDetails() : Start");

        FileDetails fileDetails= fileRepository.findById(id).orElseThrow(()-> {
            log.error("File not found");
            return new ResourceNotFound("File is not available");
        });
        log.info("NotesServiceImpl : getFileDetails() : End");

        return fileDetails;
    }

}
