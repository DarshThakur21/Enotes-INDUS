package com.enotes.Enotes_INDUS.dto;


import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class LoginResponse {

    private UserResponseDto userDto;
    private String token;
    private String refreshToken;
}
