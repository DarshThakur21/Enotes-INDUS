package com.enotes.Enotes_INDUS.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailRequest {

    private String to;
    private String subject;
    private String title;

    private String message;
}
