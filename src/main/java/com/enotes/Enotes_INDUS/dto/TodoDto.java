package com.enotes.Enotes_INDUS.dto;

import com.enotes.Enotes_INDUS.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TodoDto {
    private Integer id;
    private String title;
    private String description;

    private Status status;

    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;



}
