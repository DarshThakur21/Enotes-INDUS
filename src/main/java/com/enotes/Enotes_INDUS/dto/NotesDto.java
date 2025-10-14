package com.enotes.Enotes_INDUS.dto;

import com.enotes.Enotes_INDUS.model.Category;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
