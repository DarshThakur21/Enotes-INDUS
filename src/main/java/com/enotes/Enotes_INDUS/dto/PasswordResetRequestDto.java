package com.enotes.Enotes_INDUS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDto {
    private Integer uid;
    private  String newPassword;
}
