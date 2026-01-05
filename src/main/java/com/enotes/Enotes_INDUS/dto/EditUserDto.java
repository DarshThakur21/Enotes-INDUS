package com.enotes.Enotes_INDUS.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
}
