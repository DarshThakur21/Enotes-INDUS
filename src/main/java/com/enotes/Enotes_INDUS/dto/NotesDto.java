package com.enotes.Enotes_INDUS.dto;

import com.enotes.Enotes_INDUS.model.Category;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto  {
    private  Integer id;

    private String title;
    private String description;



    private CategoryDto category;





    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;

    private Boolean isDeleted;


    private LocalDateTime deletedOn;


    private FileDetailsDto fileDetails;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileDetailsDto{
        private  Integer id;
        private  String originalFileName;
        private  String displayFileName;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto{
        private  Integer id;
        private String name;
    }
}
