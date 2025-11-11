package com.enotes.Enotes_INDUS.dto;



import lombok.*;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavouriteNotesDto {


    private Integer id;


    private NotesDto notesDto;

    private Integer userId;


}

