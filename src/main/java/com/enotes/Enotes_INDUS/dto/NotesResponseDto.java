package com.enotes.Enotes_INDUS.dto;

import lombok.*;

import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotesResponseDto {
    private List<NotesDto> notesDtoList;
    private Integer pageNo;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean isFirst;
    private Boolean islast;




}
